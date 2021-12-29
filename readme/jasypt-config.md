# Jasypt Configuration
Whenever you develop a service, there are secret data in the project such as Database connection information and so on.
To protect this information, encryption is essential.
[Jasypt](http://www.jasypt.org/) is a java library which allows the developer to add basic encryption capabilities to his/her projects with minimum effort, and without the need of having deep knowledge on how cryptography works.

Jasypt library has lots of advantages like below. 
* High-security, standards-based encryption techniques, both for unidirectional and bidirectional encryption. Encrypt passwords, texts, numbers, binaries...
* Transparent integration with Hibernate.
* Suitable for integration into Spring-based applications and also transparently integrable with Spring Security.
* Integrated capabilities for encrypting the configuration of applications (i.e. datasources).
* Specific features for high-performance encryption in multi-processor/multi-core systems.
* Open API for use with any JCE provider.

This document describes how to set up this library in the Spring boot framework project.

## [Dependency](https://github.com/ulisesbocchio/jasypt-spring-boot)
### pom.xml
```xml
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>${jasypt.version}</version>
</dependency>
```

## Custom Encryptor 
You can set your own encryptor using Configuration and register it into Bean.

```java
@Configuration
public class JasyptConfig {
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        String key = "auth_secret_key";
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(key);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        return encryptor;
    }
}
```

## Unit test 
In order to get encrypted message using Jasypt, unit test is useful. 
Here is a sample unit test to get encrypted and decrypted string using Jasypt.

```java
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties="spring.config.location=classpath:application-test.yaml")
public class EncryptTest {

    @Nested
    @DisplayName("Encrypt String Test")
    class EncryptStringTest {
        @Test
        @DisplayName("Generate Encrypted & Decrypted String")
        @Disabled("Disabled")
        void generateEncryptStringTest() {
            LOGGER.debug("Text: {}, Encrypted Text: {}", "test", jasyptEncoding("test"));
            LOGGER.debug("Text: {}, Decrypted Text: {}", "iHDqZi0NsMTAUUseSSXD+Q==", jasyptDecoding("iHDqZi0NsMTAUUseSSXD+Q=="));
        }
    }

    private String jasyptEncoding(String value) {
        String key = "auth_secret_key";
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(key);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        return encryptor.encrypt(value);
    }

    private String jasyptDecoding(String encrypted) {
        String key = "auth_secret_key";
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
        decryptor.setAlgorithm("PBEWithMD5AndDES");
        decryptor.setPassword(key);

        return decryptor.decrypt(encrypted);
    }
}
```

## application.yaml
If you get the encrypted message, you can use it on your application.yaml file. 
Just put the encrypted values with `ENC()`. 

```yaml
datasource:
    username: ENC(HoNk6ktJqFL3m1SlToHX8riunI5G8BUM)
    password: ENC(BMVk0F3/+SmnLhHdQkNoKHd2UaC0uNVi)
```
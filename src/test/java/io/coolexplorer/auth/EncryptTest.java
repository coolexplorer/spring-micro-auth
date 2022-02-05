package io.coolexplorer.auth;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

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
            LOGGER.debug("Text: {}, Encrypted Text: {}", "password", jasyptEncoding("password"));
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

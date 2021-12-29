# OpenApi configuration
In microservice architecture, documentation is a important work to interact with other services using APIs. 
And OpenAPI is a great tool make a API documentation automatically with little effort.

springdoc-openapi java library helps to automate the generation of API documentation using spring boot projects.
Automatically generates documentation in JSON/YAML and HTML format APIs. 
This documentation can be completed by comments using swagger-api annotations.

## Dependencies
For the integration between spring-boot and swagger-ui, add the library to the list of your project dependencies (No additional configuration is needed)

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.6.3</version>
</dependency>

```

## Swagger ui custom path
If you want to change a connection path to Swagger-ui, put a configuration in property file.

```yaml
springdoc:
  swagger-ui:
    path: /swagger-ui.html
```
> Custom Swagger ui path : http://<server>:<port>/swagger-ui.html

## Web Security permission
Normally, web server has authentication for a connection. So, you need to set up a permission for connecting to Swagger UI. 
In your WebSecurityConfig, add below `antMachers()`.  

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ...
            .authorizeRequests()
                .antMatchers("/v2/api-docs/**", "/v3/api-docs/**", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
        ...
    }
}
```

## Swagger UI
After setting all, run your application and connect to `http://localhost:8080/swagger-ui.html`. 
You can see a UI like below. 

![swagger-ui](/Users/kimseunghwan/IdeaProjects/spring-micro-auth/images/swagger-ui.png)

## Authors
Allen Kim - Initial work - [coolexplorer](https://github.com/coolexplorer)

## License
This project is licensed under the MIT License - see the LICENSE.md file for details


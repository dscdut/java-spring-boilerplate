# Spring Boot Boilerplate

*Spring Boot Boilerplate* is a **starter kit** designed to help you quickly get started with building a Spring Boot application. This project provides a simple and useful template that incorporates various essential technologies and configurations.

## Technologies Used

- **Spring Boot (v2.7.10)**: Simplifies the development of Spring applications.
- **Spring Data JPA**: Provides easy integration with relational databases using JPA.
- **Spring Validation**: Handles validation of Java Beans.
- **Spring Security + JWT Token**: Secures your application with JWT-based authentication.
- **PostgreSQL**: A powerful, open source object-relational database system.
- **MapStruct**: A code generator that simplifies the mapping of Java Beans.
- **Lombok**: Reduces boilerplate code for model objects by generating getters, setters, and other methods.
- **Swagger (Open API)**: Provides interactive API documentation.

## Project Structure

The project follows a standard Maven project layout:

```sh
spring-boot-boilerplate
├── src
│ ├── main
│ │ ├── java
│ │ │ └── com/gdsc/boilerplate/springboot
│ │ │ ├── configuration
│ │ │ ├── controller
│ │ │ ├── dto
│ │ │ ├── exception
│ │ │ ├── model
│ │ │ ├── repository
│ │ │ ├── security
│ │ │ ├── service
│ │ │ └── SpringBootBoilerplateApplication.java
│ │ └── resources
│ │ ├── application.yml
│ │ └── static
│ └── test
│ └── java
│ └── com/gdsc/boilerplate/springboot
└── pom.xml
```

## Customization

### Token Information
You can customize the JWT token information such as secret key, issuer, and expiry date in the [*application.yml*](https://github.com/dscdut/java-spring-boilerplate/blob/develop/src/main/resources/application.yml) file.

```yaml
jwt:
  secret: your_secret_key
  issuer: your_issuer
  expiry: 86400000 # in milliseconds
```

### Database Connection
You can customize the database connection information in the [*application.yml*](https://github.com/dscdut/java-spring-boilerplate/blob/develop/src/main/resources/application.yml) file.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_db
    username: your_username
    password: your_password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### Swagger Configuration
You can customize Swagger settings in the [*application.yml*](https://github.com/dscdut/java-spring-boilerplate/blob/develop/src/main/resources/application.yml) file.

```yaml
swagger:
  enabled: true
  title: Spring Boot Boilerplate API
  description: API documentation for the Spring Boot Boilerplate project
  version: 1.0.0
  contact:
    name: Your Name
    url: http://your-url.com
    email: your-email@domain.com
```

### Security Configuration
You can customize which endpoints are accessible without token information in the [*SecurityConfiguration.java*](https://github.com/dscdut/java-spring-boilerplate/blob/develop/src/main/java/com/gdsc/boilerplate/springboot/configuration/SecurityConfiguration.java) file.

```java
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
		return http.cors().and().csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(requests -> requests
                        .antMatchers("/register", "/login", "/v3/api-docs/**", "/swagger-ui/**", "/api-docs", "/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();

	}
```

## Running the Application
### Prerequisites
 - Docker: Make sure Docker is installed and running.
 - Java: Ensure you have Java 11 or higher installed.
 - Maven: Make sure Maven is installed.

### Start the Database
First, make sure the database is up. If you're using Docker, you can use the following command to start the containers in detached mode:

```sh
docker-compose up -d
```

If you have made changes in your local environment, use the local Docker Compose file:

```sh
docker-compose -f local-docker-compose.yml up -d
```

## Build the Project
Navigate to the root of the project and run the following command to build the project:

```sh
mvn clean install
```

## Run the Application
Navigate to the target directory and run the application:

```sh
java -jar target/spring-boot-boilerplate.jar
```

## Using Swagger
Once the application is running, you can access the Swagger UI at:

```sh
http://localhost:8080/api-docs
```

This provides interactive documentation for your API endpoints.

## License
This project is licensed under the Apache License 2.0. See the LICENSE file for details.

# PBL3-BE
BE repository 

## Setup
### Environment
- Install JDK, Java, Mvn
- Create a mysql database named mvh_01
- Open file *application.properties* in folder */src/main/resources/application.properties*
- On line 13, if *spring.jpa.hibernate.ddl-auto = none* , change *none* to *create* to create database on running project
- After running project successfully, change *spring.jpa.hibernate.ddl-auto = create* to *none* to avoid dropping database on next launch

## Start project
- Open terminal
- Run the command
```bash
mvn spring-boot:run
```
- Open local web at http://localhost:8080/
- Login with account: 
    - email: admin@gmail.com
    - password: admin123

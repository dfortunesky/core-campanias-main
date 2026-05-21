@echo off

rem Run the Spring Boot application
mvn clean spring-boot:run -Dspring-boot.run.profiles="dev" -Dspring-boot.run.jvmArguments="-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
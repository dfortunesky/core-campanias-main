@echo off

rem Run the Spring Boot application
mvn clean spring-boot:run -Dspring-boot.run.profiles="qa" -Dspring-boot.run.jvmArguments="-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005" -Dspring-boot.run.arguments="--spring.profiles.active=local --DB_URL=jdbc:sybase:Tds:10.35.0.46:4100/afilmed?applicationName=core-campanias&hostname=core-campanias --DB_DRIVER_CLASS=com.sybase.jdbc4.jdbc.SybDriver --DB_USER=svc_campa --DB_PASSWORD=C4mp425! --logging.level.root=INFO"
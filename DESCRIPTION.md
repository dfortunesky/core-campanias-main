# springboot-java


Template para proyectos Java + Spring Boot

## Como compilar este proyecto

podman build -t swissmedical/java-spring-boot-template .

## Como ejecutar este proyecto

podman run --rm -p 8080:8080 -e 'DB_URL=jdbc:h2:~/test;MODE=LEGACY' -e DB_USER=test -e DB_PASSWORD=test -e ENV_PROFILE=local -e DB_DRIVER_CLASS=org.h2.Driver -e DB_PLATFORM=h2 localhost/swissmedical/java-spring-boot-template:latest
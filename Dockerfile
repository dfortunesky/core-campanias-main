# Usa una imagen base con OpenJDK 17 y Maven instalados
FROM ubi9/openjdk-17:1.22 AS build-stage

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia config y código fuente al contenedor
COPY --chown=default:default . .git .mvn ./
# COPY jconn4d-16.0.jar ./

# # Instalamos jconn4d en el repo local de Maven
# RUN mvn install:install-file \
#   -Dfile=jconn4d-16.0.jar \
#   -DgroupId=sybase \
#   -DartifactId=jconn4d \
#   -Dversion=4_RELEASE \
#   -Dpackaging=jar

# Empaqueta la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM ubi9/openjdk-17:1.22

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR construido en la etapa anterior al contenedor
COPY --from=build-stage /app/target/java-sprint-boot-template-0.0.1-SNAPSHOT.jar ./app.jar

# # Copiamos el driver
# COPY jconn4d-16.0.jar ./lib/jconn4d-16.0.jar

# Copia la configuración específica del perfil al contenedor
COPY application.yml ./config/application.yml
COPY application-local.yml ./config/application-local.yml
COPY application-dev.yml ./config/application-dev.yml
COPY application-qa.yml ./config/application-qa.yml
COPY application-pre.yml ./config/application-pre.yml
COPY application-prod.yml ./config/application-prod.yml

# USER 1001

# Expone el puerto 8080
EXPOSE 8080

# Command to run the application when the container starts with the active profile and configuration file
CMD java -jar app.jar --spring.profiles.active=${ENV_PROFILE}
# CMD java -cp "./lib/jconn4d-16.0.jar:app.jar" -jar app.jar --spring.profiles.active=${ENV_PROFILE}
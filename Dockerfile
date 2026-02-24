# Usa una imagen oficial de Java con Maven
FROM maven:3.9-eclipse-temurin-17 AS build

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia los archivos del proyecto
COPY . .

# Otorga permisos de ejecución a mvnw
RUN chmod +x mvnw

# Compila el proyecto usando Maven Wrapper
RUN mvn clean package -DskipTests

# Usa una imagen más ligera para el runtime
FROM eclipse-temurin:17-jre-jammy

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR generado en la imagen final
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto
EXPOSE 8080

# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

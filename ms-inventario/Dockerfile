FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar JAR compilado (soporta nombres con versión diferente)
COPY target/*.jar app.jar

# Variables de ambiente
ENV SPRING_DATASOURCE_URL=jdbc:mysql://db-inventario:3306/db_medico_inventario?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=root
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SERVER_PORT=8084
ENV JWT_SECRET=tu_clave_secreta_muy_larga_y_segura_para_produccion_cambiar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]

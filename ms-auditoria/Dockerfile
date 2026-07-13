FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:mysql://db-auditoria:3306/db_medico_auditoria?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=root
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SERVER_PORT=8090
ENV JWT_SECRET=tu_clave_secreta_muy_larga_y_segura_para_produccion_cambiar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]

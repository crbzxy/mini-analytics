# Dockerfile para el microservicio de analítica de botones
# Basado en OpenJDK 8 para compatibilidad con Java 8

FROM eclipse-temurin:8-jre

# Etiqueta de mantenimiento
LABEL maintainer="miempresa"
LABEL description="Button Analytics Service - Microservicio de analítica de botones"

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR generado por Maven
COPY target/button-analytics-service-*.jar app.jar

# Exponer el puerto 8085
EXPOSE 8085

# Healthcheck opcional
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8085/api/stats/monthly || exit 1

# Punto de entrada: ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]


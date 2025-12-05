# Dockerfile para el microservicio de analítica de elementos UI
# Basado en OpenJDK 8 para compatibilidad con Java 8

FROM eclipse-temurin:8-jre

# Etiqueta de mantenimiento
LABEL maintainer="miempresa"
LABEL description="UI Analytics Service - Microservicio de analítica de elementos de UI"

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR generado por Maven
COPY target/ui-analytics-service-*.jar app.jar

# Exponer el puerto 8085
EXPOSE 8085

# Healthcheck opcional
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8085/v1/stats/monthly || exit 1

# Punto de entrada: ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]


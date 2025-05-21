FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app


COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .


RUN chmod +x mvnw


RUN ./mvnw dependency:go-offline

COPY src src


RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:17-jre-jammy as runtime

WORKDIR /app


COPY --from=builder /app/target/*.jar app.jar


ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"


RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]

EXPOSE 8081

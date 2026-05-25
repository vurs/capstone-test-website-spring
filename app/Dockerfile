FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app

COPY . .
RUN test -f .dev-env-stamp || (echo "Development setup not completed. Run ./dev_scripts/setup_dev.sh from the repository root before building or starting the application." >&2 && exit 1)
RUN mvn -B dependency:go-offline
RUN mvn -B package -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/.dev-env-stamp .dev-env-stamp

EXPOSE 8081

#ENTRYPOINT ["java","-jar","/app/app.jar"]
ENTRYPOINT ["sh", "-c", "sleep 30 && java -jar /app/app.jar"]
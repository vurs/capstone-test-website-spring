FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B package -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

#ENTRYPOINT ["java","-jar","/app/app.jar"]
ENTRYPOINT ["sh", "-c", "sleep 30 && java -jar /app/app.jar"]
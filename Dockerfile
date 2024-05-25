FROM gradle:7.5.1-jdk17 as build
WORKDIR /app
COPY gradle /app/gradle
COPY gradlew /app/gradlew
COPY build.gradle /app/build.gradle
COPY settings.gradle /app/settings.gradle
RUN ./gradlew dependencies --no-daemon || true

COPY . /app

RUN gradle bootJar --no-daemon

FROM openjdk:19-jdk-slim
WORKDIR /
COPY --from=build /app/build/libs/*.jar app.jar
COPY . /
EXPOSE 8080
CMD bash start.sh

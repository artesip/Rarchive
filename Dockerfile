FROM gradle as build
WORKDIR /
COPY . /
RUN bash gradlew bootJar

FROM openjdk:19-jdk-slim
WORKDIR /
COPY --from=build /build/libs/*.jar app.jar
COPY start.sh /
CMD bash start.sh

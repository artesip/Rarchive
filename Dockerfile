FROM gradle as build
WORKDIR /
COPY . /
RUN gradle clean
RUN gradle bootJar

FROM openjdk:19-jdk-slim
WORKDIR /
COPY --from=build /build/libs/*.jar app.jar
COPY . /
EXPOSE 8080
CMD bash start.sh

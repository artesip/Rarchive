#!/bin/bash
chmod +x wait-for-it.sh

bash ./wait-for-it.sh db:3306 -t 60

java -jar app.jar --spring.config.location=src/main/resources/application.properties
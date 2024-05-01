#!/bin/bash

/wait-for-it.sh db:3306 -t 60

sleep 5

java -jar app.jar
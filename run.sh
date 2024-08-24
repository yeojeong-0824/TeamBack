#!/bin/bash

PROJECT_NAME="Team-Back"
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}-.*.jar | head -n 1)

if [ -z "$CURRENT_PID" ]; then
    echo " 실행중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo " 실행중인 애플리케이션을 종료했습니다. (pid : $CURRENT_PID)"
    kill -9 $CURRENT_PID
fi

echo "\n SpringBoot 애플리케이션을 실행합니다.\n"

JAR_NAME="Team-Back-0.0.1-SNAPSHOT.jar"
sudo -E nohup java -jar /home/ubuntu/$JAR_NAME > /home/ubuntu/nohup.out 2>&1 &

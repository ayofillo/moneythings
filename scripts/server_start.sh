#!/usr/bin/env bash
cd /home/ec2-user/server
java -jar -Dspring.profiles.active=aws moneyapp.jar  > /dev/null 2> /dev/null < /dev/null &
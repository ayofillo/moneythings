#!/usr/bin/env bash

cd /home/ec2-user/server

mkdir backup
dir=backup/$(date +"%d%m%Y%H%M$S%N")
mkdir "$dir"
mv -f *.yml $dir
mv -f *.sh $dir
mv -f *.jar $dir
mv -f *.log $dir

#sudo rm -rf /home/ec2-user/server
#!/bin/sh
# @desc Update build date/time tag file with current timestamp
# @changed 2020.11.08, 01:29

buildTime=`date "+%Y.%m.%d %H:%M"`
buildTag=`date "+%y%m%d-%H%M"`

echo "Updating build tag/time: $buildTag / $buildTime"

echo $buildTag > build-timetag.txt
echo $buildTime > build-timestamp.txt

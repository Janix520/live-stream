#!/bin/bash
pid=`ps -ef | grep "live-stream-0.0.1-SNAPSHOT.jar" | grep -v grep | awk '{print $2}'`
if [ "$pid" != "" ];then
  echo "already start pid: $pid"
else
  `nohup java -jar live-stream-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &`
  pid=`ps -ef | grep "live-stream-0.0.1-SNAPSHOT.jar" | grep -v grep | awk '{print $2}'`
  echo $pid
fi
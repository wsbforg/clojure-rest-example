/bin/echo "preremove script started [$1]"

APP_NAME=clojure-rest-example
prefixDir=/usr/local/$APP_NAME
identifier=$APP_NAME.jar

isJettyRunning=`pgrep java -lf | grep $identifier | cut -d" " -f1 | /usr/bin/wc -l`
if [ $isJettyRunning -eq 0 ]
then
  /bin/echo "Clojure-rest-example is not running"
else
  sleepCounter=0
  sleepIncrement=2
  waitTimeOut=600
  /bin/echo "Timeout is $waitTimeOut seconds"
  /bin/echo "Clojure-rest-example is running, stopping service"
  /sbin/service $APP_NAME stop &
  myPid=$!

  until [ `pgrep java -lf | grep $identifier | cut -d" " -f1 | /usr/bin/wc -l` -eq 0 ]
  do
    if [ $sleepCounter -ge $waitTimeOut ]
    then
      /usr/bin/pkill -KILL -f '$identifier'
      /bin/echo "Killed Clojure-rest-example"
      break
    fi
    sleep $sleepIncrement
    sleepCounter=$(($sleepCounter + $sleepIncrement))
  done

  wait $myPid

  /bin/echo "Clojure-rest-example down"
fi

if [ "$1" = 0 ]
then
  /sbin/chkconfig --del $APP_NAME
else
  /sbin/chkconfig --list $APP_NAME
fi

/bin/echo "preremove script finished"
exit 0

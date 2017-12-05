/bin/echo "postremove script started [$1]"

if [ "$1" = 0 ]
then
  /usr/sbin/userdel -r clojure-rest-example 2> /dev/null || :
  /bin/rm -rf /usr/local/clojure-rest-example
fi

/bin/echo "postremove script finished"
exit 0

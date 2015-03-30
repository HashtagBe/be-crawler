#!/bin/sh

# Run the program as a daemon with nohup.

EXECUTABLE="sh bin/webapp"
LOGFILE="logs/nohup.log"
PIDFILE="logs/run.pid"

echo -n "Starting \`$EXECUTABLE\` in background ... "
nohup $EXECUTABLE > $LOGFILE 2>&1 & echo $! > $PIDFILE

echo "done (pid=`cat $PIDFILE`)"

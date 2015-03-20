#!/usr/bin/env bash

JAR="`pwd`/repo/com/h2database/h2/1.4.186/h2-1.4.186.jar"
DRIVER="org.h2.Driver"
URL="jdbc:h2:`pwd`/db/crawled-data"
java -cp $JAR org.h2.tools.Shell -driver $DRIVER -url $URL


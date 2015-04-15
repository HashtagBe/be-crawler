#!/usr/bin/env bash

JAR="`pwd`/repo/com/h2database/h2/1.4.186/h2-1.4.186.jar"
URL="jdbc:h2:`pwd`/db/crawled-data"
java -cp $JAR org.h2.tools.Server -web -webAllowOthers


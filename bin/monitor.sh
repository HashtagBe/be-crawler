#!/bin/sh

# Monitor the crawler on the localhost.

# usage: monitor <port>          default port: 8088

PORT=${1:-"8088"}

watch -d curl -s localhost:$PORT/article/getArticleCount.do

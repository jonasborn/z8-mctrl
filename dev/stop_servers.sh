#!/bin/sh

#       _____                      _        _
#      |  _  |                    | |      | |
#   ____\ V /______ _ __ ___   ___| |_ _ __| |
#  |_  // _ \______| '_ ` _ \ / __| __| '__| |
#   / /| |_| |     | | | | | | (__| |_| |  | |
#  /___\_____/     |_| |_| |_|\___|\__|_|  |_|
#

# Used to create remove dev servers

docker ps

docker stop z8_dev_redis
docker rm z8_dev_redis

docker stop z8_dev_mysql
docker rm z8_dev_mysql

docker stop z8_dev_phpmy
docker rm z8_dev_phpmy

docker ps
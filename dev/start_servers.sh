#!/bin/sh

#       _____                      _        _
#      |  _  |                    | |      | |
#   ____\ V /______ _ __ ___   ___| |_ _ __| |
#  |_  // _ \______| '_ ` _ \ / __| __| '__| |
#   / /| |_| |     | | | | | | (__| |_| |  | |
#  /___\_____/     |_| |_| |_|\___|\__|_|  |_|
#

# Used to create some temp servers for development

docker run --name z8_dev_redis -p 6379:6379 -d redis
docker run --name z8_dev_mysql -e MYSQL_ROOT_PASSWORD=toor -p 3306:3306 -d mysql:8.0.1
docker run --name z8_dev_phpmy -d --link z8_dev_mysql:db -p 8081:80 phpmyadmin/phpmyadmin
docker ps
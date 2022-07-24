#!/bin/sh

#       _____                      _        _
#      |  _  |                    | |      | |
#   ____\ V /______ _ __ ___   ___| |_ _ __| |
#  |_  // _ \______| '_ ` _ \ / __| __| '__| |
#   / /| |_| |     | | | | | | (__| |_| |  | |
#  /___\_____/     |_| |_| |_|\___|\__|_|  |_|
#

# Used to create some temp servers for development

source ./stop_servers.sh
docker run --name z8_dev_redis -p 6379:6379 -d redis
#docker run --name z8_dev_mysql -e MYSQL_ROOT_PASSWORD=toor -p 3306:3306 -d mysql:8.0.1
docker run --detach --name z8_dev_mariadb -p 3306:3306 --env MARIADB_USER=user --env MARIADB_PASSWORD=resu --env MARIADB_ROOT_PASSWORD=toor  mariadb:latest
docker run --name z8_dev_phpmy -d --link z8_dev_mariadb:db -p 8081:80 phpmyadmin/phpmyadmin
docker ps

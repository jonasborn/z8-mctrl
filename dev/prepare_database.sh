#!/bin/sh
# shellcheck disable=SC2154

#       _____                      _        _
#      |  _  |                    | |      | |
#   ____\ V /______ _ __ ___   ___| |_ _ __| |
#  |_  // _ \______| '_ ` _ \ / __| __| '__| |
#   / /| |_| |     | | | | | | (__| |_| |  | |
#  /___\_____/     |_| |_| |_|\___|\__|_|  |_|
#

# Used to import the database manually

trim() {
    var="$*"
    # remove leading whitespace characters
    var="${var#"${var%%[![:space:]]*}"}"
    # remove trailing whitespace characters
    var="${var%"${var##*[![:space:]]}"}"
    printf '%s' "$var"
}

file="../z8-mctrl.properties"

while IFS='=' read -r key value
do
    trimmed=$(trim "$value")
    key=$(echo $key | tr '.' '_')
    eval ${key}=\${trimmed}
done < "$file"

echo "Removing database $database_name"
docker exec z8_dev_mysql mysql "-u$database_user" "-p$database_password" "-h$database_host" "-P$database_port" \
-e "DROP DATABASE $database_name"

echo "Creating database $database_name"
docker exec z8_dev_mysql mysql "-u$database_user" "-p$database_password" "-h$database_host" "-P$database_port" \
-e "CREATE DATABASE $database_name DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci"

echo "Importing $database_name"
rm -f structure.sql
echo "USE z8;" >> structure.sql
cat ../src/main/resources/structure.sql >> structure.sql
docker cp structure.sql z8_dev_mysql:/structure.sql
docker exec z8_dev_mysql mysql "-u$database_user" "-p$database_password" "$database_name" "-h$database_host" "-P$database_port" \
-e "source /structure.sql"

echo "Finished!"
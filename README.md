# About
z8-mctrl, also known as zero eight mission control, is the main control center
of the zero eight payment system.

# Structure

## Language
This project is build using Kotlin, some parts are written in Java. Kotlin is
used for education and security reasons.

## Database
As you might have noticed, this project uses JOOQ for database operations.
therefore, mctrl should support multiple databases, see https://www.jooq.org/doc/latest/manual/reference/supported-rdbms/.
I'm currently only supporting MariaDB for now. If you want to use another datasource,
translate the structure.sql src/main/resources/structure.sql to your database type.

## Queue/Cache
I'm happy to use Redis again. All needed Queues and Caches are stored there. Be aware that
caches and queued jobs are not persistent. All devices must be build aware of this fact
and stop a pending operation after a timeout.

## JSF
This project is using JSF as interface, currently featuring Butterfaces using
Bootswatch Lux and a special high-contrast version for E-Readers.

## REST
Rest endpoints are build using Spring. A full list of available endpoints will be published here.

## WS
The communication between mctrl and the sats is based on z8-alpha, currently always
using the newest version: https://github.com/jonasborn/z8-proto/blob/master/z8-alpha.proto
Websockes are used as carrier protocol. The protocol is patched before the build, see
the prepareProtocol task in build.gradle.
 

# Development

## Env

To start some temp. development servers, feel free to use
```
cd dev
bash start_servers.sh
```
to start.

This will create a redis, mysql/mariadb and phpmyadmin instance for development
purposes. The login data is root/toor by defauld and must be added to your z8-mctrl.properties
manually. **Docker/Podman is required!**.

Use

```
cd dev
bash stop_servers.sh
```
to stop and delete all containers.

### Ports

|Tool       |Port|
|-----------|----|
|MariaDB    |3306|
|PHPMyAdmin |8081|
|Redis      |6379|

## Build

Use

```
cd dev
bash prepare_database.sh
```
to import the database structure manually. Needed because JOOQ is scanning it in order
to generate the sources. This command will use the settings from the z8-mctrl.properties.


```
./gradlew build
```
to build the project. This will also generate all required source files for JOOQ
and protobuf.


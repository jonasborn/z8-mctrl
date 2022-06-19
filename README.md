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

## JSF
This project is using JSF as interface, currently featuring PrimeFaces 11.
All pages available:

## REST
All rest endpoints are build using Spark.
All pages available:

## WS
The communication between mctrl and the sats is based on z8-alpha, currently always
using the newest version: https://github.com/jonasborn/z8-proto/blob/master/z8-alpha.proto
Websockes are used as carrier protocol.
 
# Build
use
```
./gradlew build
```
to build the project. This will also generate all required source files for JOOQ
and protobuf. You **have to** import the src/main/resources/structure.sql before manually.

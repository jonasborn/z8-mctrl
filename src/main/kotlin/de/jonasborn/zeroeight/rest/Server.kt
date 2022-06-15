package de.jonasborn.zeroeight.rest

import spark.Service.ignite

class Server {

    companion object {
        public fun start() {
            val http = ignite()
            http.get("/user", UserGetRoute())
        }
    }

}
package de.jonasborn.zeroeight

import de.jonasborn.zeroeight.data.money.Deposits
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.system.exitProcess

private val log = KotlinLogging.logger {}
class DB {

    companion object {
        fun init() {
            try {
                log.debug { "Attempting to connect to database" }
                Database.connect(
                    Config.string("database.url", "jdbc:mariadb://localhost:3306/zeroeight?user=root"),
                    driver = Config.string("database.driver", "org.mariadb.jdbc.Driver"),
                    user = Config.string("database.username", "root"),
                    password = Config.string("database.password", "")
                )
                log.info { "Database connected" }
                transaction {
                    SchemaUtils.create(Deposits)
                }
            } catch (e: Exception) {
                log.error { "Unable to connect to database" }
                e.printStackTrace()
                exitProcess(1)
            }
        }
    }

}
package de.jonasborn.zeroeight.db

import de.jonasborn.zeroeight.config.Config
import de.jonasborn.zeroeight.data.UserActions
import de.jonasborn.zeroeight.data.UserTable
import de.jonasborn.zeroeight.data.device.DeviceTable
import de.jonasborn.zeroeight.data.money.DepositTable
import de.jonasborn.zeroeight.data.money.PaymentTable
import de.jonasborn.zeroeight.data.money.PayoutTable
import de.jonasborn.zeroeight.data.token.TokenActionTable
import de.jonasborn.zeroeight.data.token.TokenTable
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
                    SchemaUtils.create(
                        DeviceTable,
                        DepositTable,
                        PaymentTable,
                        PayoutTable,
                        TokenActionTable,
                        TokenTable,
                        UserActions,
                        UserTable
                    )
                }
            } catch (e: Exception) {
                log.error { "Unable to connect to database" }
                e.printStackTrace()
                exitProcess(1)
            }
        }
    }

}
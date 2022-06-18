package z8.mctrl.db

import z8.mctrl.config.Config
import z8.mctrl.data.UserActions
import z8.mctrl.data.UserTable
import z8.mctrl.data.device.DeviceTable
import z8.mctrl.data.money.DepositTable
import z8.mctrl.data.money.PaymentTable
import z8.mctrl.data.money.PayoutTable
import z8.mctrl.data.token.TokenActionTable
import z8.mctrl.data.token.TokenTable
import denoitDuffez.ScriptRunner
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.io.BufferedReader
import java.io.InputStreamReader
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import kotlin.system.exitProcess


private val log = KotlinLogging.logger {}

class DB {


    companion object {

        private var connection: Connection? = null
        private var context: DSLContext? = null

        fun get(): DSLContext {
            return context!!
        }

        private fun url(): String {
            return Config.string("database.driver", "jdbc:mariadb") + "://" +
                    Config.string("database.host", "localhost") + ":" +
                    Config.int("database.port", 3306) + "/"
        }

        fun init() {
            try {
                log.debug { "Attempting to connect to database" }
                connection = DriverManager.getConnection(
                    url(),
                    Config.string("database.username", "root"),
                    Config.string("database.password", "")
                )

                val database = Config.string("database.database", "z8")
                var sm =
                    connection!!.prepareStatement("CREATE DATABASE IF NOT EXISTS $database DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;")
                sm.executeQuery()
                connection!!.catalog = database

                var dialect: SQLDialect? = null
                val requestedDialect = Config.string("database.dialect", "MARIADB")
                if (requestedDialect == "MARIADB") {
                    dialect = SQLDialect.MARIADB
                } else if (requestedDialect == "MYSQL") {
                    dialect = SQLDialect.MYSQL
                }

                context = DSL.using(connection, dialect)
                log.info { "Database connected" }

            } catch (e: Exception) {
                log.error { "Unable to connect to database" }
                e.printStackTrace()
                exitProcess(1)
            }
        }

        fun createStructure(dropAndCreate: Boolean) {
            var sm: PreparedStatement?
            if (dropAndCreate) {
                var database = connection!!.catalog
                sm = connection!!.prepareStatement("DROP DATABASE IF EXISTS $database")
                sm.executeQuery()
                sm =
                    connection!!.prepareStatement("CREATE DATABASE IF NOT EXISTS $database DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;")
                sm.executeQuery()
                connection!!.catalog = database
            }

            val runner = ScriptRunner(connection, false, true)
            runner.runScript(
                BufferedReader(
                    InputStreamReader(
                        DB::class.java.getResourceAsStream("/structure.sql")!!
                    )
                )
            )
        }

        fun unused() {
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
        }
    }


}
package z8.mctrl.db

import denoitDuffez.ScriptRunner
import mu.KotlinLogging
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import z8.mctrl.config.Config
import z8.mctrl.jooq.tables.daos.*
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
        private var configuration: Configuration? = null

        fun dsl(): DSLContext {
            return context!!
        }

        fun dao(): Configuration {
            return configuration!!
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

                val database = Config.string("database.name", "z8")
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
                configuration = DefaultConfiguration().set(connection).set(dialect)
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
                val runner = ScriptRunner(connection, false, true)
                runner.runScript(
                    BufferedReader(
                        InputStreamReader(
                            DB::class.java.getResourceAsStream("/structure.sql")!!
                        )
                    )
                )
            }
        }


        fun deposit(): DepositDao {
            return DepositDao(dao())
        }

        fun device(): DeviceDao {
            return DeviceDao(dao())
        }

        fun payment(): PaymentDao {
            return PaymentDao(dao())
        }

        fun payout(): PayoutDao {
            return PayoutDao(dao())
        }

        fun tokenAction(): TokenactionDao {
            return TokenactionDao(dao())
        }

        fun token(): TokenDaoExtended {
            return TokenDaoExtended(dao())
        }

        fun userActions(): UseractionsDao {
            return UseractionsDao(dao())
        }

        fun user(): UserDao {
            return UserDao(dao())
        }
    }


}
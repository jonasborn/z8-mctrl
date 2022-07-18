package z8.mctrl

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator

import z8.mctrl.db.DB
import org.jetbrains.exposed.sql.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import z8.mctrl.config.Config
import z8.mctrl.function.sn.SecurityNumber
import z8.mctrl.function.token.TokenId
import z8.mctrl.server.WSServer
import z8.mctrl.util.CardGenerator

//TODO https://stackoverflow.com/questions/51221777/failed-to-configure-a-datasource-url-attribute-is-not-specified-and-no-embedd
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@Import(CustomServletContextInitializer::class)
class App

fun main(args: Array<String>) {


     Configurator.setRootLevel(
        Level.getLevel(Config.get("logging.level"))
    )

    DB.init()
    DB.createStructure(false)

    //WSServer.startup()

    runApplication<App>(*args)
}


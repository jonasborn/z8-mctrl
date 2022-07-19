package z8.mctrl

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator

import z8.mctrl.db.RDS
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import z8.mctrl.config.Config

//TODO https://stackoverflow.com/questions/51221777/failed-to-configure-a-datasource-url-attribute-is-not-specified-and-no-embedd
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@Import(CustomServletContextInitializer::class)
class App

fun main(args: Array<String>) {


     Configurator.setRootLevel(
        Level.getLevel(Config.get("logging.level"))
    )

    RDS.init()
    RDS.createStructure(true)

    //WSServer.startup()

    runApplication<App>(*args)
}


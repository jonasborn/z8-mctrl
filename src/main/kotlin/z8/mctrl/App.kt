package z8.mctrl

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Import
import z8.mctrl.server.StaticFiles

//TODO https://stackoverflow.com/questions/51221777/failed-to-configure-a-datasource-url-attribute-is-not-specified-and-no-embedd
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@Import(CustomServletContextInitializer::class, StaticFiles::class)
class App

val logger: Logger = LogManager.getLogger()

var ctx: ConfigurableApplicationContext? = null

fun main(args: Array<String>) {

    ctx = runApplication<App>(*args)
}

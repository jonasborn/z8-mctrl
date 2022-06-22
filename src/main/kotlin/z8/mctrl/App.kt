package z8.mctrl

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator
import z8.mctrl.data.UserTable
import z8.mctrl.data.device.DeviceTable
import z8.mctrl.data.token.TokenTable
import z8.mctrl.db.DB
import org.jetbrains.exposed.sql.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import z8.mctrl.config.Config
import z8.mctrl.data.money.DepositTable
import z8.mctrl.data.money.Operator
import z8.mctrl.data.money.PaymentTable
import z8.mctrl.data.money.PayoutTable
import z8.mctrl.util.CardGenerator
import z8.mctrl.util.LuhnUtils
import java.util.*

//TODO https://stackoverflow.com/questions/51221777/failed-to-configure-a-datasource-url-attribute-is-not-specified-and-no-embedd
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@Import(CustomServletContextInitializer::class)
class App

fun main(args: Array<String>) {

    CardGenerator.generate()

    println(
        LuhnUtils.generateLuhn(10).joinToString("")
    )

    System.exit(1)

    Configurator.setRootLevel(
        Level.getLevel(Config.get("logging.level"))
    )

    DB.init()
    DB.createStructure(true)


    runApplication<App>(*args)
}

fun unused() {
    val deviceId = DeviceTable.insertAndGetId {
        it[time] = System.currentTimeMillis()
        it[secret] = "Hallo1234"
    }
    println("Created device $deviceId")

    val userId = UserTable.insertAndGetId {
        it[device] = deviceId
        it[time] = System.currentTimeMillis()
        it[recovery] = "None"
    }
    println("Created user $userId")

    val tokenId = TokenTable.insertAndGetId {
        it[device] = deviceId
        it[time] = System.currentTimeMillis()
        it[user] = userId
    }
    println("Created token $tokenId")

    val depositId = DepositTable.insertAndGetId {
        it[device] = deviceId
        it[token] = tokenId
        it[amount] = 20f
        it[time] = System.currentTimeMillis()
        it[origin] = Operator.AUTHORITY
        it[details] = "Issued by jborn"
    }
    DepositTable.insertAndGetId {
        it[device] = deviceId
        it[token] = tokenId
        it[amount] = 40f
        it[time] = System.currentTimeMillis()
        it[origin] = Operator.AUTHORITY
        it[details] = "Issued by jborn"
    }
    DepositTable.insertAndGetId {
        it[device] = deviceId
        it[token] = tokenId
        it[amount] = 80f
        it[time] = System.currentTimeMillis()
        it[origin] = Operator.AUTHORITY
        it[details] = "Issued by jborn"
    }
    println("Created deposit $depositId")

    val paymentId = PaymentTable.insertAndGetId {
        it[device] = deviceId
        it[token] = tokenId
        it[amount] = 2f
        it[time] = System.currentTimeMillis()
    }

    PaymentTable.insertAndGetId {
        it[device] = deviceId
        it[token] = tokenId
        it[amount] = 4f
        it[time] = System.currentTimeMillis()
    }

    PaymentTable.insertAndGetId {
        it[device] = deviceId
        it[token] = tokenId
        it[amount] = 8f
        it[time] = System.currentTimeMillis()
    }
    println("Created payment $paymentId")

    val payoutId = PayoutTable.insertAndGetId {
        it[device] = deviceId
        it[token] = tokenId
        it[amount] = 10f
        it[time] = System.currentTimeMillis()
        it[target] = Operator.AUTHORITY
        it[details] = "Issued by jborn"
    }

    println("Created payout $payoutId")
}
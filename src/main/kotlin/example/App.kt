package example

import de.jonasborn.zeroeight.data.User
import de.jonasborn.zeroeight.data.UserTable
import de.jonasborn.zeroeight.data.device.DeviceTable
import de.jonasborn.zeroeight.data.money.*
import de.jonasborn.zeroeight.data.token.TokenTable
import de.jonasborn.zeroeight.db.DB
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import z8.gctrl.jooq.tables.references.DEVICE
import z8.gctrl.jooq.tables.references.PAYMENT
import java.sql.DriverManager
import java.util.*
import kotlin.system.exitProcess


@SpringBootApplication
@Import(CustomServletContextInitializer::class)
class App

fun main(args: Array<String>) {
    DB.init()

    val c = DriverManager.getConnection("jdbc:mariadb://localhost:3306/zeroeight?user=root")
    val context: DSLContext = DSL.using(c, SQLDialect.MARIADB)

    context.select(DEVICE.ID).where(
        DEVICE.ID.eq(ByteArray(2))
    )


    exitProcess(1)

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
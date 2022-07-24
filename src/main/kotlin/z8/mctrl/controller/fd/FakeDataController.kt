package z8.mctrl.controller.fd

import org.jooq.meta.derby.sys.Sys
import org.springframework.beans.factory.annotation.Autowired
import z8.mctrl.db.RDS
import z8.mctrl.db.forced.PaymentProvider
import z8.mctrl.db.forced.TokenAction
import z8.mctrl.jooq.tables.pojos.*
import z8.mctrl.util.IdUtils
import java.util.concurrent.ThreadLocalRandom
import javax.annotation.PostConstruct
import javax.faces.view.ViewScoped
import javax.inject.Named

@Named("FakeDataController")
@ViewScoped
class FakeDataController {

    @Autowired
    var rds: RDS? = null

    @PostConstruct
    fun generate() {

        val terminalId = IdUtils.generateDefaultId()

        rds!!.terminalDevice().insert(
            TerminalDeviceObject(
                terminalId,
                System.currentTimeMillis(),
                "password",
                "Device1"
            )
        )

        val serverId = IdUtils.generateDefaultId()
        rds!!.serverDevice().insert(
            ServerDeviceObject(
                serverId,
                System.currentTimeMillis(),
                "localhost",
                0
            )
        )

        val userId = "aaba66673f504a8ea1c81e40fb5549f7"
        if (!rds!!.user().existsById(userId)) {
            rds!!.user().insert(
                UserObject(
                    userId, System.currentTimeMillis(), terminalId, "mail@jonasborn.de", "none"
                )
            )
        }

        val tokenId = IdUtils.generateDefaultId()
        rds!!.token().insert(
            TokenObject(
                tokenId,
                System.currentTimeMillis(),
                terminalId,
                userId
            )
        )

        val internalDeviceId = IdUtils.generateDefaultId()
        rds!!.internalDevice().insert(
            InternalDeviceObject(
                internalDeviceId, System.currentTimeMillis(), "password"
            )
        )

        val externalDeviceId = IdUtils.generateDefaultId()
        rds!!.externalDevice().insert(
            ExternalDeviceObject(
                externalDeviceId,
                System.currentTimeMillis(),
                internalDeviceId,
                "password",
                "Funny device"
            )
        )

        for (i in 1..30) {
            rds!!.payment().insert(
                PaymentObject(
                    ThreadLocalRandom.current().nextLong(1000, 999999),
                    System.currentTimeMillis(),
                    externalDeviceId,
                    tokenId,
                    ThreadLocalRandom.current().nextDouble(1.0, 30.0),
                    "Entry $i"
                )
            )
            rds!!.payout().insert(
                PayoutObject(
                    ThreadLocalRandom.current().nextLong(1000, 999999),
                    System.currentTimeMillis(),
                    terminalId,
                    tokenId,
                    ThreadLocalRandom.current().nextDouble(1.0, 30.0),
                    PaymentProvider.PAYPAL,
                    "Entry $i"
                )
            )
            rds!!.deposit().insert(
                DepositObject(
                    ThreadLocalRandom.current().nextLong(1000, 999999),
                    System.currentTimeMillis(),
                    terminalId,
                    tokenId,
                    ThreadLocalRandom.current().nextDouble(1.0, 30.0),
                    PaymentProvider.PAYPAL,
                    "Entry $i"
                )
            )
        }

        rds!!.tokenAction().insert(
            TokenActionObject(
                ThreadLocalRandom.current().nextLong(1000, 999999),
                System.currentTimeMillis(),
                tokenId,
                serverId,
                userId,
                TokenAction.ACTIVATED
            )
        )

    }



}
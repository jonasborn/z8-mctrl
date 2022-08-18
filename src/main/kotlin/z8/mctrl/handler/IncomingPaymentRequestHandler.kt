package z8.mctrl.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.companion.device.InternalDevices
import z8.mctrl.companion.payment.PaymentRequests
import z8.mctrl.db.RDS
import z8.mctrl.db.forced.PaymentRequestStatus
import z8.mctrl.exception.InvalidCredentialsException
import z8.mctrl.exception.InvalidCredentialsType
import z8.mctrl.exception.NotFoundException
import z8.mctrl.exception.NotFoundType
import z8.mctrl.jooq.tables.pojos.PaymentRequestObject
import z8.mctrl.voyager.Voyager
import java.util.*

@Component
class IncomingPaymentRequestHandler @Autowired constructor(
    val rds: RDS,
    val paymentRequests: PaymentRequests,
    val voyager: Voyager
) {

    fun registerPaymentRequest(externalDeviceId: String, amount: Double, secret: String): String {
        val sourceDevices = rds.externalDevice().fetchById(externalDeviceId)
        if (sourceDevices.isEmpty()) throw NotFoundException("Device $externalDeviceId not found", NotFoundType.ID_UNKNOWN)
        val sourceDevice = sourceDevices.first()

        if (sourceDevice.secret != secret) throw InvalidCredentialsException(
            "Access denied",
            InvalidCredentialsType.INITIATOR_DEVICE
        )

        val internalDevice = sourceDevice.target
        val status = voyager.getStatus(internalDevice!!)
        if (!status.active) throw NotFoundException("Device not connected", NotFoundType.DEVICE_UNKNOWN)

        val id = UUID.randomUUID().toString()

        rds.paymentRequest().insert(
            PaymentRequestObject(
                id, System.currentTimeMillis(), externalDeviceId, internalDevice, amount, PaymentRequestStatus.STARTED
            )
        )

        paymentRequests.addToQueue(internalDevice, id)

        return id
    }


}
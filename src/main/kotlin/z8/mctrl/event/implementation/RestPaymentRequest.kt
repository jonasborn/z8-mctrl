package z8.mctrl.event.implementation

import z8.mctrl.event.RestEvent
import z8.mctrl.event.RestHandler

class RestPaymentRequest: RestHandler {
    override fun canHandle(re: RestEvent): Boolean {
        return re.hasIncomingPaymentRequest()
    }

    override fun handle(re: RestEvent) {
        val ipm = re.incomingPaymentRequest!!

    }
}
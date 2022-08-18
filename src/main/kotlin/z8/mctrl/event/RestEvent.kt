package z8.mctrl.event

class RestEvent {

    class IncomingPaymentRequest(val externalDeviceId: String, val amount: Double, val secret: String)

    val incomingPaymentRequest: IncomingPaymentRequest? = null

    fun hasIncomingPaymentRequest(): Boolean {
        return incomingPaymentRequest != null
    }

}
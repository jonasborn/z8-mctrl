import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.*
import z8.mctrl.companion.payment.PaymentRequests
import z8.mctrl.server.WSServer

class CheckInternalDeviceListener {

    companion object {
        @BeforeAll
        fun startWS() {
            //WSServer.startup()
        }
    }

    @Test
    fun testOnOpen() {
        val pr = Mockito.mockStatic(PaymentRequests::class.java).use {  }


    }

}
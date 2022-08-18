import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import org.mockito.Mockito
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import z8.mctrl.companion.payment.PaymentRequests


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
package z8.mctrl.rest

import com.google.common.io.ByteStreams
import org.jooq.meta.derby.sys.Sys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import z8.mctrl.companion.bill.Bills
import z8.mctrl.companion.device.InternalDevices
import z8.mctrl.db.RDS
import z8.mctrl.exception.*
import z8.mctrl.companion.payment.PaymentRequests
import z8.mctrl.db.forced.PaymentRequestStatus
import z8.mctrl.jooq.tables.pojos.PaymentRequestObject
import z8.mctrl.voyager.Voyager
import java.io.FileInputStream
import java.util.*

@RestController //357

class GetBill @Autowired constructor(
    val rds: RDS,
    val bills: Bills
) {

    @GetMapping(
        value = arrayOf("/pdfbill"),
        produces = arrayOf(MediaType.APPLICATION_PDF_VALUE)
    )
    @ResponseBody
    fun request(): ByteArray {
        return bills.generate(
            rds.payment().findAll()
        )
    }


}
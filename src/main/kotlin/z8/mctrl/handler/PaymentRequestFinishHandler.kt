package z8.mctrl.handler

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.db.RDS
import z8.mctrl.server.WSServer
import z8.proto.alpha.ServerMessage
import java.util.function.Consumer
import javax.annotation.PostConstruct

@Component
class PaymentRequestFinishHandler @Autowired constructor(val rds: RDS, val wsServer: WSServer, val paymentRequestAuthHandler: PaymentRequestAuthHandler) {

    val logger: Logger = LogManager.getLogger()

    @PostConstruct
    fun init() {
        //Will be called, when ultrac auth is successfully finished
        paymentRequestAuthHandler.authenticationListeners.add(Consumer {
            val ed = rds.internalDevice().fetchOneById(it.internalDevice)
            wsServer.send(
                it.internalDevice, ServerMessage.newBuilder().build(), ed.secret
            )
        })
    }
    

}
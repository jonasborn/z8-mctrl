package z8.mctrl.server

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import org.redisson.api.RBlockingQueue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.companion.payment.PaymentRequests
import z8.mctrl.config.Config
import z8.mctrl.db.KVS
import z8.mctrl.db.RDS
import z8.mctrl.jooq.tables.daos.InternaldeviceDao
import z8.proto.alpha.*
import java.lang.Exception
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.util.*
import java.util.function.Consumer
import javax.annotation.PostConstruct
import kotlin.Comparator
import kotlin.collections.HashMap
import kotlin.math.log


@Component
open class WSServer @Autowired constructor(
    val config: Config,
    val rds: RDS,
    val paymentRequests: PaymentRequests

) : WebSocketServer(InetSocketAddress(config.int("ws.port", 8992))) {

    class WSSocket(val instance: WSServer, val ws: WebSocket) {
        private val logger: Logger = LogManager.getLogger()
        fun sendWithSecret(sm: ServerMessage, secret: String): Boolean {
            try {
                ws.send(Packer.pack(sm, secret))
                return true
            } catch (e: Exception) {
                logger.error("Unable to pack message for internal device", e)
            }
            return false
        }

        fun sendWithoutSecret(sm: ServerMessage, internalDeviceId: String): Boolean {
            val dev = instance.rds.internalDevice().fetchOneById(internalDeviceId)
            if (dev?.secret != null) {
                try {
                    ws.send(Packer.pack(sm, dev.secret!!))
                    return true
                } catch (e: Exception) {
                    logger.error("Unable to pack message for internal device", e)
                }
            } else {
                logger.error("Unable to find secret for internal device {}", internalDeviceId)
            }
            return false
        }

    }

    private val logger: Logger = LogManager.getLogger()
    private val onOpenListeners: HashMap<String, Consumer<Pair<WSSocket, ClientHandshake>>> = hashMapOf()
    private val onMessageListeners: HashMap<String, Consumer<Pair<WSSocket, ClientMessage>>> = hashMapOf()
    private val onCloseListeners: HashMap<String, Consumer<WSSocket>> = hashMapOf()

    @PostConstruct
    fun postConstruct() {
        this.start()
    }

    fun onOpen(vararg name: Any, cons: Consumer<Pair<WSSocket, ClientHandshake>>) {
        val n = name.joinToString("-")
        onOpenListeners[n] = cons
        logger.debug("Registered onOpen handler {}", n)
    }

    fun onMessage(vararg name: Any, cons: Consumer<Pair<WSSocket, ClientMessage>>) {
        val n = name.joinToString("-")
        onMessageListeners[n] = cons
        logger.debug("Registered onMessage handler {}", n)
    }

    fun onClose(vararg name: Any, consumer: Consumer<WSSocket>) {
        val n = name.joinToString("-")
        onCloseListeners[n] = consumer
        logger.debug("Registered onClose handler {}", n)
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        if (conn != null && handshake != null) {
            logger.info(
                "Connection from {}:{} opened",
                conn.remoteSocketAddress.address.hostAddress,
                conn.remoteSocketAddress.port
            )
            onOpenListeners.forEach {
                try {
                    it.value.accept(Pair(WSSocket(this, conn), handshake))
                } catch (e: Exception) {
                    logger.warn("Handler {} failed", it.key, e)
                }
            }
        }

    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        if (conn != null && conn.remoteSocketAddress != null) {
            onCloseListeners.forEach {
                try {
                    it.value.accept(WSSocket(this, conn))
                } catch (e: Exception) {
                    logger.warn("Handler {} failed", it.key, e)
                }
            }
        }
    }


    override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
        if (conn != null && message != null) {
            try {
                logger.debug("Message received")
                val cm = UnPacker.unpack(conn, message)

                if (cm != null) {
                    onMessageListeners.forEach {
                        try {
                            it.value.accept(Pair(WSSocket(this, conn), cm))
                        } catch (e: Exception) {
                            logger.warn("Handler {} failed", it.key, e)
                        }
                    }
                }

            } catch (e: Throwable) {
                logger.fatal("Message receive failed", e)
            }
        }
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        logger.warn("Direct string message received and rejected")
    }

    override fun onError(conn: WebSocket?, e: Exception?) {
        logger.warn("Error happened", e)
    }

    override fun onStart() {
        logger.info("Starting Websocket server")

        onMessage("PaymentRequestsMessageHandler") {
            paymentRequests.onMessage(it.first, it.second)
        }
    }

}
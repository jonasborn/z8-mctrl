package z8.mctrl.server

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import org.redisson.api.RBlockingDeque
import org.redisson.api.RBlockingQueue
import z8.mctrl.companion.payment.PaymentRequests
import z8.proto.alpha.*
import java.lang.Exception
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.BlockingDeque
import java.util.function.Consumer
import kotlin.collections.ArrayList

class WSServer(address: InetSocketAddress?) : WebSocketServer(address) {

    val logger: Logger = LogManager.getLogger()
    val openRequestQueues = hashMapOf<String, RBlockingQueue<WebSocket>?>()


    companion object {
        val onCloseListeners: ArrayList<Consumer<WebSocket>> = arrayListOf()

        var sockets = mutableListOf<WebSocket>()
        fun startup() {
            val s = WSServer(
                InetSocketAddress(8823)
            )
            s.connectionLostTimeout = 20
            Thread {
                s.start()
            }.start()

            while (true) {
                var l = Scanner(System.`in`).nextLine()
                if (l == "a") {
                    sockets.forEach {
                        if (it.isOpen) it.send(
                            Packer.pack(
                                ServerMessage.newBuilder().setTokenAwait(
                                    TokenAwait.newBuilder()
                                ).build(), ""
                            )
                        )
                    }
                }
                if (l == "s") {
                    s.stop()
                    break
                }
                if (l == "m") {
                    sockets.forEach {
                        if (it.isOpen) it.send(
                            Packer.pack(
                                ServerMessage.newBuilder().setModifyDisplayQueue(
                                    ModifyDisplayQueue.newBuilder()
                                        .setAnimation(ModifyDisplayQueue.Animation.NONE)
                                        .setOperation(ModifyDisplayQueue.Operation.OVERWRITE)
                                        .setLine(0)
                                        .setData("Line 1 Hallo")
                                        .setTimeout(1000)
                                        .build()
                                ).build(), ""
                            )
                        )
                    }

                    sockets.forEach {
                        if (it.isOpen) it.send(
                            Packer.pack(
                                ServerMessage.newBuilder().setModifyDisplayQueue(
                                    ModifyDisplayQueue.newBuilder()
                                        .setOperation(ModifyDisplayQueue.Operation.APPEND)
                                        .setPosition(1)
                                        .setLine(1)
                                        .setData("Line 2")
                                        .setTimeout(1000)
                                ).build(), ""
                            )
                        )
                    }

                    sockets.forEach {
                        if (it.isOpen) it.send(
                            Packer.pack(
                                ServerMessage.newBuilder().setModifyDisplayQueue(
                                    ModifyDisplayQueue.newBuilder()
                                        .setOperation(ModifyDisplayQueue.Operation.PRINT)
                                        .setAnimation(ModifyDisplayQueue.Animation.SINGLE_LINE)
                                        .setLine(0)
                                ).build(), ""
                            )
                        )
                    }

                    sockets.forEach {
                        if (it.isOpen) it.send(
                            Packer.pack(
                                ServerMessage.newBuilder().setModifyDisplayQueue(
                                    ModifyDisplayQueue.newBuilder()
                                        .setOperation(ModifyDisplayQueue.Operation.PRINT)
                                        .setAnimation(ModifyDisplayQueue.Animation.NONE)
                                        .setLine(1)
                                ).build(), ""
                            )
                        )
                    }

                    sockets.forEach {
                        if (it.isOpen) it.send(
                            Packer.pack(
                                ServerMessage.newBuilder().setDisplayQueueRun(
                                    DisplayQueueRun.newBuilder()
                                ).build(), ""
                            )
                        )
                    }
                }
            }


        }

        fun send(sm: ServerMessage, sec: String) {
            Packer.pack(sm, sec)
        }

        fun onClose(consumer: Consumer<WebSocket>) {
            onCloseListeners.add(consumer)
        }
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        if (conn != null) {
            sockets.add(conn)
            logger.info(
                "Connection from {}:{} opened",
                conn.remoteSocketAddress.address.hostAddress,
                conn.remoteSocketAddress.port
            )

        }

    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        if (conn != null && conn.remoteSocketAddress != null) {
            onCloseListeners.forEach { it.accept(conn) }
        }
    }


    override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
        if (conn != null && message != null) {
            try {
                logger.debug("Message received")
                val sm = UnPacker.unpack(conn, message)

                if (sm != null) {
                    if (sm.hasWelcomeMessage()) {
                        PaymentRequests.handleWelcome(conn, sm)
                    }
                }

            } catch (e: Throwable) {
                println("ERROR")
            }
        }
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        println(message)
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {

    }

    override fun onStart() {
        logger.info("Starting server")
    }
}
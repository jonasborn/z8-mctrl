package z8.mctrl.server

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import z8.proto.alpha.ServerMessage
import z8.proto.alpha.TokenAwait
import java.lang.Exception
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.util.*

class WSServer(address: InetSocketAddress?) : WebSocketServer(address) {

    val logger: Logger = LogManager.getLogger()



    companion object {
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
                    break;
                }
            }


        }
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        if (conn != null) {
            sockets.add(conn);
            logger.info(
                "Connection from {}:{} opened",
                conn.remoteSocketAddress.address.hostAddress,
                conn.remoteSocketAddress.port
            )

        }

    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {

    }


    override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
        if (conn != null && message != null) {
            try {
                logger.debug("Message received")
                UnPacker.unpack(conn, message)
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
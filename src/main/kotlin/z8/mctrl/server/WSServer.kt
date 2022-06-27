package z8.mctrl.server

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import z8.mctrl.controller.Welcome
import z8.proto.alpha.WelcomeMessage
import java.lang.Exception
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import kotlin.math.log

class WSServer(address: InetSocketAddress?) : WebSocketServer(address) {

    val logger: Logger = LogManager.getLogger()

    companion object {
        fun startup() {
            WSServer(
                InetSocketAddress(8823)
            ).start()
        }
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        if (conn != null) {
            logger.info("Connection from {}:{} opened", conn.remoteSocketAddress.address.hostAddress, conn.remoteSocketAddress.port)
        }
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {

    }


    override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
        println("Bytes")
        if (message != null) {
            UnPacker.unpack(message)
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
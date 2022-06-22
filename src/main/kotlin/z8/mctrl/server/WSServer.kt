package z8.mctrl.server

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import z8.mctrl.controller.Welcome
import z8.proto.alpha.WelcomeMessage
import java.lang.Exception
import java.net.InetSocketAddress

class WSServer(address: InetSocketAddress?) : WebSocketServer(address) {
    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {

    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        TODO("Not yet implemented")
    }
}
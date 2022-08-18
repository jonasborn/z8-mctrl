package z8.mctrl.handler

import z8.mctrl.server.WSServer
import z8.proto.alpha.ClientMessage
import z8.proto.alpha.ServerMessage

interface WSHandler {

    fun init(wsServer: WSServer)

}
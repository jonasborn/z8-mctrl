package z8.mctrl.handler

import z8.proto.alpha.ClientMessage
import z8.proto.alpha.ServerMessage

interface WSHandler {

    fun canHandle(cm: ClientMessage): Boolean

    fun handle(cm: ClientMessage): ServerMessage?

}
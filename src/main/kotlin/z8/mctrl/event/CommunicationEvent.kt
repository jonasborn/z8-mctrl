package z8.mctrl.event

import z8.mctrl.server.WSServer
import z8.proto.alpha.ClientMessage

class CommunicationEvent(val wsSocket: WSServer.WSSocket, val clientMessage: ClientMessage) {

}
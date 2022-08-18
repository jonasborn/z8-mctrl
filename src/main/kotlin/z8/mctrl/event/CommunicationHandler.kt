package z8.mctrl.event

import z8.proto.alpha.ClientMessage

interface CommunicationHandler {

    fun canHandle(ce: CommunicationEvent): Boolean

    fun handle(ce: CommunicationEvent)

}
package z8.mctrl.event

import org.springframework.stereotype.Component
import z8.proto.alpha.ClientMessage

@Component
class EventOrchestrator {

    private val eventHandlers: ArrayList<EventHandler> = arrayListOf()
    private val communicationHandlers: ArrayList<CommunicationHandler> = arrayListOf()
    private val restHandlers: ArrayList<RestHandler> = arrayListOf()

    fun register(eventHandler: EventHandler) {
        eventHandlers.add(eventHandler)
    }

    fun register(communicationHandler: CommunicationHandler) {
        communicationHandlers.add(communicationHandler)
    }

    fun register(restHandler: RestHandler) {
        restHandlers.add(restHandler)
    }

    fun notify(event: Event) {
        eventHandlers.forEach {
            if (it.canHandle(event)) {
                it.handle(event)
            }
        }
    }

    fun notify(communicationEvent: CommunicationEvent) {
        communicationHandlers.forEach {
            if (it.canHandle(communicationEvent)) {
                it.handle(communicationEvent)
            }
        }
    }

    fun notify(restEvent: RestEvent) {
        restHandlers.forEach {
            if (it.canHandle(restEvent)) {
                it.handle(restEvent)
            }
        }
    }
}
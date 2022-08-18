package z8.mctrl.event

interface EventHandler {

    fun canHandle(ie: Event): Boolean

    fun handle(ie: Event)

}
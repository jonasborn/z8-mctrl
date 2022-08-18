package z8.mctrl.event

interface RestHandler {

    fun canHandle(re: RestEvent): Boolean

    fun handle(re: RestEvent)

}
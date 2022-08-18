package z8.mctrl.event

class Event {

    class TokenAuthenticatedEvent(val externalDevice: String, val token: String)

    val tokenAuthenticatedEvent: TokenAuthenticatedEvent? = null

    fun hasTokenAuthenticatedEvent(): Boolean {
        return tokenAuthenticatedEvent != null
    }

}
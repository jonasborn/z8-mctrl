package z8.mctrl.exception

import java.lang.RuntimeException

class InvalidCredentialsException(message: String?, type: InvalidCredentialsType) : RuntimeException(message) {
}

enum class InvalidCredentialsType {
    INITIATOR_DEVICE
}
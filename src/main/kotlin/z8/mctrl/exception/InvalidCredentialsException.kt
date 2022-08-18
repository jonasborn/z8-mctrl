package z8.mctrl.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.FORBIDDEN)
class InvalidCredentialsException(message: String?, type: InvalidCredentialsType) : RuntimeException(message) {
}

enum class InvalidCredentialsType {
    INITIATOR_DEVICE
}
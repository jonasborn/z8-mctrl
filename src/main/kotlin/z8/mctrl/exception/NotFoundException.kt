package z8.mctrl.exception

import java.lang.RuntimeException

class NotFoundException(message: String?, nfr: NotFoundType) : RuntimeException(message) {
}

enum class NotFoundType {
    ID_UNKNOWN,
    DEVICE_UNKNOWN
}
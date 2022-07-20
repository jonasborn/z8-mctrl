package z8.mctrl.db.forced

enum class PaymentRequestStatus() {
    STARTED,
    PENDING,
    FINISHED,
    DECLINED,
    TWO_FACTOR,
    FAILURE,
    INTERNAL_DEVICE_MISSING
}
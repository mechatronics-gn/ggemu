package dev.jhyub.ggemu

enum class MessageType(val code: Int) {
    NONE(0), CLICK(1), DOUBLE_CLICK(2),
}

data class Message(val messageType: MessageType, val x: Float, val y: Float, val z: Float)

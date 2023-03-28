package dev.jhyub.ggemu

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.ArrayDeque

class Client(val ip: String, val port: Int) {
    val selectorManager = SelectorManager(Dispatchers.IO)
    var socket: Socket? = null
    var writeChannel: ByteWriteChannel? = null

    val specialMessageDeque = ArrayDeque<Message>()

    suspend fun connect() {
        withContext(Dispatchers.IO) {
            socket = aSocket(selectorManager).tcp().connect(ip, port)
            writeChannel = socket?.openWriteChannel(true)
        }
    }

    fun pushSpecialMessage(message: Message) {
        specialMessageDeque.addLast(message)
    }

    suspend fun loop() {
        withContext(Dispatchers.IO) {
            while(writeChannel?.isClosedForWrite == false) {
                while(specialMessageDeque.isNotEmpty()) {
                    val m = specialMessageDeque.first
                    writeChannel?.writeMessage(m)
                    specialMessageDeque.removeFirst()
                }

                writeChannel?.writeMessage(Gyro.makeMessage())
                delay(10L)
            }
        }
    }

    suspend fun close() {
        withContext(Dispatchers.IO) {
            writeChannel?.close()
            socket?.close()
            selectorManager.close()
        }
    }
}

suspend fun ByteWriteChannel.writeMessage(message: Message) {
    withContext(Dispatchers.IO) {
        writeInt(message.messageType.code)
        writeFloat(message.roll)
        writeFloat(message.pitch)
        writeFloat(message.yaw)
    }
}
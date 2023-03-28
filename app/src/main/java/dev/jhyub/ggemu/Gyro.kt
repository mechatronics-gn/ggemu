package dev.jhyub.ggemu

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

object Gyro {
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    var roll = 0f
    var pitch = 0f
    var yaw = 0f

    fun setSensor(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        sensor?.let {
            sensorManager.registerListener(object: SensorEventListener {
                override fun onSensorChanged(p0: SensorEvent?) {
                    roll = p0?.values?.get(0) ?: 0f
                    pitch = p0?.values?.get(1) ?: 0f
                    yaw = -(p0?.values?.get(2) ?: 0f)
                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

                }
            }, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    fun makeMessage(messageType: MessageType = MessageType.NONE): Message {
        return Message(messageType, roll, pitch, yaw)
    }
}
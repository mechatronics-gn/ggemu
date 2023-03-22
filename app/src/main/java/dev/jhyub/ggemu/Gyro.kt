package dev.jhyub.ggemu

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

object Gyro {
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    var x = 0f
    var y = 0f
    var z = 0f

    fun setSensor(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        sensor?.let {
            sensorManager.registerListener(object: SensorEventListener {
                override fun onSensorChanged(p0: SensorEvent?) {
                    x = p0?.values?.get(0) ?: 0f
                    y = p0?.values?.get(1) ?: 0f
                    z = p0?.values?.get(2) ?: 0f
                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

                }
            }, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    fun makeMessage(messageType: MessageType = MessageType.NONE): Message? {
        return Message(messageType, x, y, z)
    }
}
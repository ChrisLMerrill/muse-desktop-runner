package org.museautomation.runner.desktop

import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.Stage
import org.musetest.core.values.ValueSourceConfiguration

class CollectInputWindow(val input_list: Map<String, ValueSourceConfiguration>, val listener: (inputs: Map<String, ValueSourceConfiguration>) -> Unit)
{
    val label = Label("hello!")

    fun open()
    {
        Platform.runLater {
            val scene = Scene(label, 500.0, 350.0)
            val stage = Stage()
            stage.scene = scene
            stage.show()

            stage.setOnCloseRequest {
                val inputs = HashMap<String, ValueSourceConfiguration>()
                inputs.put("username", ValueSourceConfiguration.forValue("JoeBlow"))
                listener(inputs)
            }
        }
    }
}
package org.museautomation.runner.desktop.input

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.stage.Stage
import org.museautomation.core.MuseProject
import org.museautomation.core.values.ValueSourceConfiguration
import org.museautomation.core.values.descriptor.SubsourceDescriptor
import org.museautomation.ui.extend.edit.EditInProgress

class CollectInputWindow(private val project: MuseProject, private val input_descriptors: List<SubsourceDescriptor>, private val input_list: Map<String, ValueSourceConfiguration>, private val listener: (inputs: Map<String, ValueSourceConfiguration>) -> Unit)
{
    private lateinit var stage : Stage

    fun open()
    {
        Platform.runLater {
            stage = Stage()
            stage.scene = createScene()
            stage.show()

            stage.setOnCloseRequest {
                println("onCloseRequest()")
            }
        }
    }

    private fun createScene() : Scene
    {
        val border_pane = BorderPane()

        // editor area
        val editor = InputEditorStack(NoopEdit(), project, input_descriptors, input_list)
        editor.showSaveCancelButtons(false)
        border_pane.center = editor.node

        // button area
        val button_area = HBox()
        button_area.alignment = Pos.CENTER_RIGHT
        val ok_button = Button("Run Job")
        button_area.children.add(ok_button)
        ok_button.setOnAction {
            println("Ok")
            listener(editor.getInputs())
            stage.close()
        }
        val cancel_button = Button("Cancel")
        button_area.children.add(cancel_button)
        cancel_button.setOnAction {
            println("Cancel")
            stage.close()
        }
        border_pane.bottom = button_area

        val scene = Scene(border_pane, 500.0, 350.0)
        scene.stylesheets.add(javaClass.getResource("/runner.css").toExternalForm())
        return scene
    }

}

class NoopEdit<ValueSourceConfiguration> : EditInProgress<ValueSourceConfiguration>
{
    override fun commit(p0: ValueSourceConfiguration?)
    {
        // no-op
    }

    override fun cancel()
    {
        // no-op
    }
}
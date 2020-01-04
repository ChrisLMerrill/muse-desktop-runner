package org.museautomation.runner.desktop.input

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.stage.Stage
import org.musetest.core.MuseProject
import org.musetest.core.values.ValueSourceConfiguration
import org.musetest.core.values.descriptor.SubsourceDescriptor
import org.musetest.ui.extend.edit.EditInProgress

class CollectInputWindow(val project: MuseProject, val input_descriptors: List<SubsourceDescriptor>, val input_list: Map<String, ValueSourceConfiguration>, val listener: (inputs: Map<String, ValueSourceConfiguration>) -> Unit)
{
    val label = Label("hello!")
    lateinit var stage : Stage

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

    fun createScene() : Scene
    {
        val border_pane = BorderPane()

        // editor area
        val editor = InputEditorStack(NoopEdit(), project, input_descriptors, input_list)
        editor.showSaveCancelButtons(false);
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
        return scene
    }

}

class NoopEdit<ValueSourceConfiguration> : EditInProgress<ValueSourceConfiguration>
{
    override fun commit(p0: ValueSourceConfiguration?)
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
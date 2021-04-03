package org.museautomation.runner.desktop.authentication

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.stage.Stage

import javafx.scene.layout.GridPane

import javafx.scene.layout.HBox

import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
open class BasicCredentialsInputWindow(val stage: Stage, val validator: CredentialsValidator, val listener: CredentialsProviderListener,
    val title: String = "Muse Desktop Runner", val prompt: String = "Enter your credentials to login:")
{
    fun show()
    {
        stage.scene = createScene()

        stage.title = title
        stage.show()
        stage.setOnCloseRequest {
            stage.close()
            listener.cancelled()
        }
    }

    open fun createScene(): Scene
    {
        val border_pane = BorderPane()
        border_pane.id = "bp"
        border_pane.padding = Insets(10.0, 50.0, 50.0, 50.0)

        val box = HBox()
        border_pane.top = box
        box.padding = Insets(20.0, 20.0, 20.0, 30.0)

        val prompt_label = Label(prompt)
        box.children.add(prompt_label)

        //Adding GridPane
        val grid = GridPane()
        border_pane.center = grid
        grid.padding = Insets(20.0, 20.0, 20.0, 20.0)
        grid.hgap = 5.0
        grid.vgap = 5.0

        val id_label = Label("Username")
        grid.add(id_label, 0, 0)
        val id_field = TextField()
        grid.add(id_field, 1, 0)
        grid.id = "root"

        val pass_label = Label("Password")
        grid.add(pass_label, 0, 1)
        val pass_field = PasswordField()
        grid.add(pass_field, 1, 1)

        val message_label = Label()
        grid.add(message_label, 1, 2)

        val login_button = Button("Login")
        grid.add(login_button, 2, 1)
        login_button.id = "btnLogin"
        login_button.onAction = object : EventHandler<ActionEvent>
        {
            override fun handle(event: ActionEvent?)
            {
                GlobalScope.launch {
                    Platform.runLater {
                        login_button.disableProperty().set(true)
                        message_label.text = "validating credentials..."
                        message_label.textFill = Color.BLACK
                    }
                    validator.startValidation(BasicLoginCredentials(id_field.text, pass_field.text), object: CredentialsValidatorListener
                    {
                        override fun validationSuccess()
                        {
                            Platform.runLater {
                                message_label.text = "Login succeeded"
                                message_label.textFill = Color.GREEN
                            }
                            GlobalScope.launch {
                                Timer("SettingUp", false).schedule(1000) {
                                    Platform.runLater {
                                        stage.hide()
                                    }
                                }
                            }
                            GlobalScope.launch {
                                listener.credentialsProvided(BasicLoginCredentials(id_field.text, pass_field.text))
                            }
                        }

                        override fun validationFailure()
                        {
                            Platform.runLater {
                                login_button.disableProperty().set(false)
                                message_label.text = "Login failed"
                                message_label.textFill = Color.RED
                            }
                        }
                    })
                }
            }
        }

        val scene = Scene(border_pane)
        scene.stylesheets.add(javaClass.getResource("/runner.css").toExternalForm())
        return scene
    }
}
package org.museautomation.runner.desktop

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class SystemTrayUI(window: MainWindow)
{
    val icon = TrayIcon(Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/Mu-icon16.png")))

    init
    {
        if (!SystemTray.isSupported())
            println("SystemTray is not supported :(")
        else
        {
            icon.addMouseListener(TrayIconMouseListener())
            icon.addActionListener()
            {
                println("double-clicked!")
                window.show()
            }

            icon.popupMenu = createMenu(window)
            val tray = SystemTray.getSystemTray()
            tray.add(icon)
        }
    }

    fun createMenu(window: MainWindow): PopupMenu
    {

        val popup = PopupMenu()
        popup.add(MenuItem("About"))
        popup.addSeparator()
        popup.add(CheckboxMenuItem("Set auto size"))
        popup.add(CheckboxMenuItem("Set tooltip"))
        popup.addSeparator()

        val display_menu = Menu("Display")
        display_menu.add(MenuItem("Error"))
        display_menu.add(MenuItem("Warning"))
        display_menu.add(MenuItem("Info"))
        display_menu.add(MenuItem("None"))
        display_menu.add(MenuItem("Exit"))
        popup.add(display_menu)

        val exit_item = MenuItem("Exit")
        popup.add(exit_item)
        exit_item.addActionListener({ window.close() })

        return popup
    }

    fun teardown()
    {
        SystemTray.getSystemTray().remove(icon)
    }
}

private class TrayIconMouseListener : MouseAdapter()
{
    override fun mouseClicked(e: MouseEvent?)
    {
        println("mouse clicked!")
    }
}
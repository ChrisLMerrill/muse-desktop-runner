package org.museautomation.runner.desktop

import org.museautomation.runner.desktop.RunnerDesktopApp.Companion.APP
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class SystemTrayUI
{
    private val icon = TrayIcon(Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/Mu-icon16.png")))

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
                APP.showWindow()
            }

            icon.popupMenu = createMenu()
            val tray = SystemTray.getSystemTray()
            tray.add(icon)
        }
    }

    private fun createMenu(): PopupMenu
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
        popup.add(display_menu)
        popup.addSeparator()

        val exit_item = MenuItem("Exit")
        popup.add(exit_item)
        exit_item.addActionListener { APP.shutdown() }

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
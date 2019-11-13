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

        val open_item = MenuItem("Open")
        popup.add(open_item)
        open_item.addActionListener { APP.showWindow() }

        popup.addSeparator()
        popup.add(MenuItem("do something"))
        popup.add(MenuItem("do something else"))
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
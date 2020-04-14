package org.museautomation.runner.desktop

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class SystemTrayUI
{
    lateinit var tray_icon: TrayIcon
    var listener : SystemTrayListener = SystemTrayListener.NoopListener()


    init
    {
        if (!SystemTray.isSupported())
            println("SystemTray is not supported :(")
        else
        {
            tray_icon = TrayIcon(Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/Mu-icon16.png")))
            tray_icon.addMouseListener(TrayIconMouseListener())
            tray_icon.addActionListener { listener.openRequested() }
            val tray = SystemTray.getSystemTray()
            tray.add(tray_icon)
        }
    }

    fun teardown()
    {
        SystemTray.getSystemTray().remove(tray_icon)
    }

    fun showNotification(title: String, message: String, success: Boolean)
    {
        tray_icon.displayMessage(title, message, if (success) TrayIcon.MessageType.INFO else TrayIcon.MessageType.ERROR)
    }

    private inner class TrayIconMouseListener : MouseAdapter()
    {
        override fun mouseClicked(e: MouseEvent)
        {
            if ( e.button == MouseEvent.BUTTON1 )
                listener.openRequested()
        }
    }
}
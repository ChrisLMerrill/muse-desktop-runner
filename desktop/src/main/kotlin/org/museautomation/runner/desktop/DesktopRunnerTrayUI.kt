package org.museautomation.runner.desktop

import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

open class DesktopRunnerTrayUI(app: DesktopRunnerApp, menu_provider: DesktopRunnerTrayMenuProvider)
{
    lateinit var _tray_icon: TrayIcon

    init
    {
        if (!SystemTray.isSupported())
            println("SystemTray is not supported :(")
        else
        {
            _tray_icon = DesktopRunnerTrayIcon(getIcon(), menu_provider)
            _tray_icon.addMouseListener(TrayIconMouseListener(app))  // for single-click-show support
            _tray_icon.addActionListener { app.showMainWindow() }
            val tray = SystemTray.getSystemTray()
            tray.add(_tray_icon)
        }
    }

    open fun getIcon() = Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/Mu-icon16.png"))

    open fun shutdown()
    {
        SystemTray.getSystemTray().remove(_tray_icon)
    }

    fun showNotification(title: String, message: String, success: Boolean)
    {
        _tray_icon.displayMessage(title, message, if (success) TrayIcon.MessageType.INFO else TrayIcon.MessageType.ERROR)
    }

    private inner class TrayIconMouseListener(val app: DesktopRunnerApp) : MouseAdapter()
    {
        // show the main window on single-mouse click
        override fun mouseClicked(e: MouseEvent)
        {
            if ( e.button == MouseEvent.BUTTON1 )
                app.showMainWindow()
        }
    }
}
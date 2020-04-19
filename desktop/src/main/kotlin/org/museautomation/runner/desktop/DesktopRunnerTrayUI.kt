package org.museautomation.runner.desktop

import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class DesktopRunnerTrayUI(app: DesktopRunnerApp, menu_provider: DesktopRunnerTrayMenuProvider)
{
    lateinit var _tray_icon: TrayIcon

    init
    {
        if (!SystemTray.isSupported())
            println("SystemTray is not supported :(")
        else
        {
            _tray_icon = DesktopRunnerTrayIcon(Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/Mu-icon16.png")), menu_provider)
            _tray_icon.addMouseListener(TrayIconMouseListener(app))  // for single-click-show support
            _tray_icon.addActionListener { app.showMainWindow() }
            val tray = SystemTray.getSystemTray()
            tray.add(_tray_icon)
        }
    }

    fun shutdown()
    {
        SystemTray.getSystemTray().remove(_tray_icon)
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
package org.museautomation.runner.desktop

import org.museautomation.runner.jobs.Jobs
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class SystemTrayUI
{
    lateinit var tray_icon : TrayIcon
    init
    {
        if (!SystemTray.isSupported())
            println("SystemTray is not supported :(")
        else
        {
            tray_icon = TrayIcon(Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/Mu-icon16.png")))
            tray_icon.addMouseListener(TrayIconMouseListener())
            tray_icon.addActionListener { listener.openRequested() }
            tray_icon.popupMenu = createMenu()
            val tray = SystemTray.getSystemTray()
            tray.add(tray_icon)
        }
    }

    private fun createMenu(): PopupMenu
    {
        popup = PopupMenu()
        populateMenu(popup)
        return popup
    }

    private fun populateMenu(popup: PopupMenu)
    {
        popup.removeAll()
        val open_item = MenuItem("Open")
        popup.add(open_item)
        open_item.addActionListener{ listener.openRequested() }

        popup.addSeparator()

        if (Jobs.asList().size > 0)
        {
            for (job in Jobs.asList())
            {
                val item = MenuItem("Run " + job.id)
                item.addActionListener { listener.runJobRequsted(job) }
                popup.add(item)
            }
            popup.addSeparator()
        }

        val exit_item = MenuItem("Exit")
        popup.add(exit_item)
        exit_item.addActionListener { listener.exitRequested() }
    }

    fun teardown()
    {
        SystemTray.getSystemTray().remove(tray_icon)
    }

    fun showNotification(title: String, message: String, success: Boolean)
    {
        tray_icon.displayMessage(title, message, if (success) TrayIcon.MessageType.INFO else TrayIcon.MessageType.ERROR)
    }

    private lateinit var popup : PopupMenu
    var listener : SystemTrayListener = SystemTrayListener.NoopListener()

    private inner class TrayIconMouseListener : MouseAdapter()
    {
        override fun mouseClicked(e: MouseEvent)
        {
            if ( e.button == MouseEvent.BUTTON1 )
                listener.openRequested()
        }
    }
}
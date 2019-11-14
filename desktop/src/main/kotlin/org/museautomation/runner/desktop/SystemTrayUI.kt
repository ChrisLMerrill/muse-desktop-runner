package org.museautomation.runner.desktop

import org.museautomation.runner.jobs.Jobs
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
            icon.addActionListener { listener.openRequested() }
            icon.popupMenu = createMenu()
            val tray = SystemTray.getSystemTray()
            tray.add(icon)
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
        SystemTray.getSystemTray().remove(icon)
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
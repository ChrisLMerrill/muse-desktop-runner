package org.museautomation.runner.desktop

import java.awt.MenuItem
import java.awt.PopupMenu

class DesktopRunnerTrayMenuProvider(val app: DesktopRunnerApp)
{
    fun createMenu(): PopupMenu
    {
        val menu = PopupMenu()

        val exit_item = MenuItem()
        exit_item.label = "Exit"
        exit_item.addActionListener({ app.exitRequested() })

        menu.add(exit_item)
        return menu
    }
}
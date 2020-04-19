package org.museautomation.runner.desktop

import java.awt.Image
import java.awt.PopupMenu
import java.awt.TrayIcon

class DesktopRunnerTrayIcon(image: Image, val menu_provider: DesktopRunnerTrayMenuProvider): TrayIcon(image)
{
    override fun getPopupMenu(): PopupMenu
    {
        return menu_provider.createMenu()
    }
}
package org.museautomation.runner.desktop.widgets;

import javafx.scene.control.*;
import javafx.util.*;

import java.text.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DateCell
    {
    public static <ROW, Long> Callback<TableColumn<ROW, Long>, TableCell<ROW, Long>> create(DateFormat format)
        {
        return column -> new TableCell<>()
            {
            @Override
            protected void updateItem(Long item, boolean empty)
                {
                super.updateItem(item, empty);
                if (item == null || empty)
                    {
                    setText(null);
                    }
                else
                    {
                    setText(format.format(item));
                    }
                }
            };
        }
    }
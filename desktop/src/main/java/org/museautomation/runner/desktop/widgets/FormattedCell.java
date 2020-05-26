package org.museautomation.runner.desktop.widgets;

import javafx.scene.control.*;
import javafx.util.*;

import java.text.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FormattedCell
    {
    public static <ROW, Object> Callback<TableColumn<ROW, Object>, TableCell<ROW, Object>> create(Format format)
        {
        return column -> new TableCell<>()
            {
            @Override
            protected void updateItem(Object item, boolean empty)
                {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setText(null);
                else
                    setText(format.format(item));
                }
            };
        }
    }
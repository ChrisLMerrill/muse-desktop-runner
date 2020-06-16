import javafx.collections.*;
import javafx.collections.transformation.*;
import org.museautomation.runner.tasks.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class sample
    {
    public static void main(String[] args)
        {
        SortedList<ExecutedTask> sorted = new SortedList<>(FXCollections.observableArrayList());
        sorted.comparatorProperty().set((task1, task2) ->
            Objects.compare(task1.getStartTime(), task2.getStartTime(), Comparator.comparingLong(aLong -> aLong)));
        }
    }
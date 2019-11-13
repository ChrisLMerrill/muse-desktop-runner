package org.museautomation.runner;

import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class KotlinConversion
    {
    static void test()
        {
        MuseProject project = new SimpleProject();
        List<MuseResourceRunner> runners = project.getClassLocator().getInstances(MuseResourceRunner.class);
        for (MuseResourceRunner runner : runners)
            {

            }
        }
    }



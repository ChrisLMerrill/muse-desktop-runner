package org.museautomation.desktop.runner;

import org.museautomation.runner.jobs.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JobRunner
    {
    public static void main(String[] args)
        {
        JobRun runs = JobRuns.INSTANCE.get(args[0]);
        }

    public void run(JobRun run)
        {

        }
    }
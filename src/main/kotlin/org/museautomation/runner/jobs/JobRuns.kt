package org.museautomation.desktop.runner.jobs;

import org.museautomation.desktop.runner.settings.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JobRuns extends SettingsFolder
    {
    public List<JobRun> asList()
         {
         return _runs;
         }

     public JobRun get(String id)
         {
         for (JobRun run : _runs)
             if (id.equals(run.getId()))
                 return run;
         return null;
         }

    @Override
    protected void accept(String name, Object settings)
        {
        if (settings instanceof JobRun)
            {
            name = name.substring(0, name.indexOf("."));
            JobRun run = (JobRun) settings;
            run.setId(name);
            _runs.add(run);
            }
        }

    public static JobRuns get()
        {
        if (RUNS == null)
            {
            RUNS = new JobRuns();
            RUNS.loadFiles(FOLDER, JobRun.class,null);
            }
        return RUNS;
        }

    private List<JobRun> _runs = new ArrayList<>();

    private static JobRuns RUNS;
    private final static String FOLDER = "runner/runs";
    }
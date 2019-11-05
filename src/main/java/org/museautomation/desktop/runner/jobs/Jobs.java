package org.museautomation.desktop.runner.jobs;

import org.museautomation.desktop.runner.settings.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Jobs extends SettingsFolder
    {
    public List<Job> asList()
         {
         return _jobs;
         }

     public Job get(String id)
         {
         for (Job job : _jobs)
             if (id.equals(job.getId()))
                 return job;
         return null;
         }

    @Override
    protected void accept(String name, Object settings)
        {
        if (settings instanceof Job)
            {
            name = name.substring(0, name.indexOf("."));
            Job job = (Job) settings;
            job.setId(name);
            _jobs.add(job);
            }
        }

    public static Jobs get()
        {
        if (JOBS == null)
            {
            JOBS = new Jobs();
            JOBS.loadFiles(FOLDER, Job.class,null);
            }
        return JOBS;
        }

    private List<Job> _jobs = new ArrayList<>();

    private static Jobs JOBS;
    private final static String FOLDER = "runner/jobs";

    }
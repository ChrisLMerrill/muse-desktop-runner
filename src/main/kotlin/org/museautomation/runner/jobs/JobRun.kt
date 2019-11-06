package org.museautomation.desktop.runner.jobs;

/**
 * Represents the execution of a Job. It may not be finished (or even started).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JobRun
    {
    public String getId()
        {
        return _id;
        }

    public void setId(String id)
        {
        _id = id;
        }

    public String getJobId()
        {
        return _job_id;
        }

    public void setJobId(String job_id)
        {
        _job_id = job_id;
        }

    public long getStartTime()
        {
        return _start_time;
        }

    public void setStartTime(Long start_time)
        {
        _start_time = start_time;
        }

    public long getEndTime()
        {
        return _end_time;
        }

    public void setEndTime(Long end_time)
        {
        _end_time = end_time;
        }

    private String _id;
    private String _job_id;
    private Long _start_time;
    private Long _end_time;
    }
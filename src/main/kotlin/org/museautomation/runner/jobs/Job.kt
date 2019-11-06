package org.museautomation.desktop.runner.jobs;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Job
    {
    public String getId()
        {
        return _id;
        }

    public void setId(String id)
        {
        _id = id;
        }

    public String getTaskId()
        {
        return _task_id;
        }

    public void setTaskId(String task_id)
        {
        _task_id = task_id;
        }

    public String getProjectId()
        {
        return _project_id;
        }

    public void setProjectId(String project_id)
        {
        _project_id = project_id;
        }

    private String _id;
    private String _task_id;
    private String _project_id;
    }
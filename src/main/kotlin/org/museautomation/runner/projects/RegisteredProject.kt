package org.museautomation.desktop.runner.projects;

import com.fasterxml.jackson.annotation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class RegisteredProject
    {
    public RegisteredProject(String id, String path, String name)
        {
        _id = id;
        _path = path;
        _name = name;
        }

    private RegisteredProject() {}

    @JsonIgnore
    public String getId()
        {
        return _id;
        }

    @JsonIgnore
    public void setId(String id)
        {
        _id = id;
        }

    public String getPath()
        {
        return _path;
        }

    public void setPath(String path)
        {
        _path = path;
        }

    public String getName()
        {
        return _name;
        }

    public void setName(String name)
        {
        _name = name;
        }

    private String _id;
    private String _name;
    private String _path;
    }
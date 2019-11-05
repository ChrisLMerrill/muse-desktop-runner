package org.museautomation.desktop.runner.projects;

import org.museautomation.desktop.runner.settings.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class RegisteredProjects extends SettingsFolder
    {
    public List<RegisteredProject> asList()
        {
        return _projects;
        }

    public RegisteredProject get(String id)
        {
        for (RegisteredProject p : _projects)
            if (id.equals(p.getId()))
                return p;
        return null;
        }

    @Override
    protected void accept(String name, Object settings)
        {
        if (settings instanceof RegisteredProject)
            {
            name = name.substring(0, name.indexOf("."));
            RegisteredProject project = (RegisteredProject) settings;
            project.setId(name);
            _projects.add(project);
            }
        }

    public static RegisteredProjects get()
        {
        if (PROJECTS == null)
            {
            PROJECTS = new RegisteredProjects();
            PROJECTS.loadFiles(FILENAME, RegisteredProject.class,null);
            }
        return PROJECTS;
        }

    private List<RegisteredProject> _projects = new ArrayList<>();

    private static RegisteredProjects PROJECTS;
    private final static String FILENAME = "runner/projects";
    }
package org.museautomation.desktop.runner.projects;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

import java.io.*;

/**
 * Represents a folder of homogenous settings files, where each file represents a instance of they setting type.
 * All settings files represent the same type of settings object. The files are serialized to/from JSON.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseSettingsFolder
    {
    protected abstract void accept(String name, Object settings);

    protected void loadFiles(String path, Class type, ObjectMapper mapper)
        {
        if (mapper == null)
            mapper = new ObjectMapper();
        File folder = new File(BASE_FOLDER, path);
        if (!folder.exists())
            return;

        File[] files = folder.listFiles();
        if (files != null)
            for (File source_file : files)
                {
                try (FileInputStream instream = new FileInputStream(source_file))
                    {
                    Object settings = mapper.readValue(instream, type);
                    accept(source_file.getName(), settings);
                    }
                catch (Exception e)
                    {
                    LOG.error("Unable to load settings from " + source_file.getPath(), e);
                    }
                }
        }

    public static File BASE_FOLDER = new File(new File(System.getProperty("user.home")), ".muse");

    private final static Logger LOG = LoggerFactory.getLogger(BaseSettingsFolder.class);
    }
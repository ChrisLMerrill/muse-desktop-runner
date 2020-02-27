package org.museautomation.runner.plugins

import org.museautomation.core.MuseExecutionContext
import org.museautomation.core.plugins.MusePlugin
import org.museautomation.core.values.ValueSourceConfiguration

class InputInjectionPlugin : MusePlugin
{
    val value_sources = HashMap<String, ValueSourceConfiguration>()

    override fun conditionallyAddToContext(context: MuseExecutionContext?, automatic: Boolean): Boolean = true
    override fun shutdown() {}
    override fun getId(): String = "InputInjectionPlugin"

    override fun initialize(context: MuseExecutionContext?)
    {
        for (key in value_sources.keys)
            context?.setVariable(key, value_sources.get(key)?.createSource(context.project)?.resolveValue(context))
    }

    fun addInput(name: String, value_source: ValueSourceConfiguration)
    {
        value_sources.put(name, value_source)
    }
}
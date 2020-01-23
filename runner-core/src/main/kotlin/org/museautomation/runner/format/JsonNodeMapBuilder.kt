package org.museautomation.runner.format

import com.fasterxml.jackson.databind.JsonNode

class JsonNodeMapBuilder(val node: JsonNode)
{
    fun buildMap() : Map<String,Any>
    {
        val map = HashMap<String,Any>()

        for (entry in node.fields())
            map.put(entry.key, entry.value as Any)  // TODO convert to ensure only primitive types
        return map
    }
}
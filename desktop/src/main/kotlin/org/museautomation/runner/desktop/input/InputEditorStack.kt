package org.museautomation.runner.desktop.input

import javafx.scene.Parent
import org.musetest.core.MuseProject
import org.musetest.core.values.ValueSourceConfiguration
import org.musetest.core.values.descriptor.SubsourceDescriptor
import org.musetest.ui.extend.actions.UndoStack
import org.musetest.ui.extend.edit.EditInProgress
import org.musetest.ui.extend.edit.stack.EditorStack
import org.musetest.ui.valuesource.map.ValueSourceMapEditor

class InputEditorStack(edit: EditInProgress<Object>, val project: MuseProject, val input_descriptors: List<SubsourceDescriptor>, val input_list: Map<String, ValueSourceConfiguration>) : EditorStack(edit, UndoStack())
{
    val _editor = ValueSourceMapEditor(project, undoStack)

    init
    {
        val fake_source = ValueSourceConfiguration()
        for (name in input_list.keys)
            fake_source.sourceMap.put(name, input_list.get(name))
        _editor.setSource(fake_source, input_descriptors.toTypedArray())
    }

    override fun getNode(): Parent
    {
        return _editor.node as Parent
    }

    fun getInputs() : Map<String, ValueSourceConfiguration>
    {
        val inputs = HashMap<String, ValueSourceConfiguration>()
        for (name in _editor.source.sourceNames)
            inputs.put(name, _editor.source.getSource(name))
        return inputs
    }

    override fun notifyEditCommit()
    {
        TODO("not implemented")
    }

}
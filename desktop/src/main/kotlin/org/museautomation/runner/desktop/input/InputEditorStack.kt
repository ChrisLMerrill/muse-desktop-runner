package org.museautomation.runner.desktop.input

import javafx.scene.Parent
import org.museautomation.core.MuseProject
import org.museautomation.core.values.ValueSourceConfiguration
import org.museautomation.core.values.descriptor.SubsourceDescriptor
import org.museautomation.ui.extend.actions.UndoStack
import org.museautomation.ui.extend.edit.EditInProgress
import org.museautomation.ui.extend.edit.stack.EditorStack
import org.museautomation.ui.valuesource.map.ValueSourceMapEditor

class InputEditorStack(edit: EditInProgress<Any>, project: MuseProject, input_descriptors: List<SubsourceDescriptor>, input_list: Map<String, ValueSourceConfiguration>) : EditorStack(edit, UndoStack())
{
    private val _editor = ValueSourceMapEditor(project, undoStack)

    init
    {
        val fake_source = ValueSourceConfiguration()
        for (name in input_list.keys)
            fake_source.addSource(name, input_list[name])
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
            inputs[name] = _editor.source.getSource(name)
        return inputs
    }

    override fun notifyEditCommit()
    {
        // no-op
    }

}
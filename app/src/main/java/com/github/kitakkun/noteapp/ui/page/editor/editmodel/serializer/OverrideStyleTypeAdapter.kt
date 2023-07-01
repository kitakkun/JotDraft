package com.github.kitakkun.noteapp.ui.page.editor.editmodel.serializer

import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.OverrideStyle
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class OverrideStyleTypeAdapter : TypeAdapter<OverrideStyle>() {

    override fun write(out: JsonWriter, value: OverrideStyle) {
        out.beginObject()
        when (value) {
            is OverrideStyle.Bold -> {
                out.name("type").value("bold")
                out.name("params").beginArray().value(value.enabled.toString()).endArray()
            }

            is OverrideStyle.Italic -> {
                out.name("type").value("italic")
                out.name("params").beginArray().value(value.enabled.toString()).endArray()
            }

            is OverrideStyle.FontSize -> {
                out.name("type").value("fontSize")
                out.name("params").beginArray().value(value.fontSize.toString()).endArray()
            }

            is OverrideStyle.Color -> {
                out.name("type").value("color")
                out.name("params").beginArray().value(value.color.toArgb().toString()).endArray()
            }
        }
        out.endObject()
    }

    override fun read(input: JsonReader): OverrideStyle {
        var type: String? = null
        val params = mutableListOf<String>()

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                "type" -> type = input.nextString()
                "params" -> {
                    input.beginArray()
                    while (input.hasNext()) {
                        params.add(input.nextString())
                    }
                    input.endArray()
                }
            }
        }
        input.endObject()

        return when (type) {
            "bold" -> OverrideStyle.Bold(params[0].toBoolean())
            "italic" -> OverrideStyle.Italic(params[0].toBoolean())
            "fontSize" -> OverrideStyle.FontSize(params[0].toDouble().sp)
            "color" -> OverrideStyle.Color(androidx.compose.ui.graphics.Color(params[0].toInt()))
            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}


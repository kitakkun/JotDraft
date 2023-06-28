package com.github.kitakkun.noteapp.ui.page.editor.editmodel.serializer

import androidx.compose.ui.unit.sp
import com.github.kitakkun.noteapp.ui.page.editor.editmodel.style.OverrideStyle
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = OverrideStyle::class)
class OverrideStyleSerializer {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("OverrideStyle") {
            element<String>("type")
            element<List<String>>("params")
        }

    override fun serialize(encoder: Encoder, value: OverrideStyle) {
        val composite = encoder.beginStructure(descriptor)
        val type = when (value) {
            is OverrideStyle.Bold -> "bold"
            is OverrideStyle.Italic -> "italic"
            is OverrideStyle.FontSize -> "fontSize"
            is OverrideStyle.Color -> "color"
        }
        val params = when (value) {
            is OverrideStyle.Bold -> listOf(value.enabled.toString())
            is OverrideStyle.Italic -> listOf(value.enabled.toString())
            is OverrideStyle.FontSize -> listOf(value.fontSize.toString())
            is OverrideStyle.Color -> listOf(value.color.value.toString())
        }
        composite.encodeStringElement(descriptor, 0, type)
        composite.encodeSerializableElement(descriptor, 1, ListSerializer(String.serializer()), params)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): OverrideStyle {
        val composite = decoder.beginStructure(descriptor)
        val type = composite.decodeStringElement(descriptor, 0)
        val params = composite.decodeSerializableElement(descriptor, 1, ListSerializer(String.serializer()))
        composite.endStructure(descriptor)
        return when (type) {
            "bold" -> OverrideStyle.Bold(params[0].toBoolean())
            "italic" -> OverrideStyle.Italic(params[0].toBoolean())
            "fontSize" -> OverrideStyle.FontSize(params[0].toDouble().sp)
            "color" -> OverrideStyle.Color(androidx.compose.ui.graphics.Color(params[0].toLong()))
            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}

package com.github.kitakkun.noteapp.editor.editor.dialog.colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.kitakkun.noteapp.customview.preview.PreviewContainer
import com.github.kitakkun.noteapp.data.model.StyleColor
import com.github.kitakkun.noteapp.editor.R
import com.github.kitakkun.noteapp.editor.editor.ext.toHexCode
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun ColorPickerDialog(
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onColorConfirm: (StyleColor) -> Unit,
) {
    val controller = rememberColorPickerController()

    var useDynamicColor by remember { mutableStateOf(false) }
    var isConfiguringLightThemeColor by remember { mutableStateOf(true) }

    var staticColor by remember { mutableStateOf(Color.Transparent) }
    var lightThemeColor by remember { mutableStateOf(Color.Transparent) }
    var darkThemeColor by remember { mutableStateOf(Color.Transparent) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .scrollable(
                    state = rememberScrollState(),
                    orientation = Orientation.Vertical
                ),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            ) {
                IconButton(onClick = onCancel) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
                Button(
                    onClick = {
                        val callbackColor = if (useDynamicColor) {
                            StyleColor.Dynamic(
                                lightValue = lightThemeColor,
                                darkValue = darkThemeColor
                            )
                        } else {
                            StyleColor.Static(staticColor)
                        }
                        onColorConfirm(callbackColor)
                    }
                ) {
                    Text(text = stringResource(R.string.ok))
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "Use Dynamic Color")
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = useDynamicColor, onCheckedChange = { useDynamicColor = it })
            }
            if (useDynamicColor) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ColorTile(
                        color = lightThemeColor,
                        label = "Light Theme",
                        onClick = { isConfiguringLightThemeColor = true },
                        isSelected = isConfiguringLightThemeColor,
                    )
                    ColorTile(
                        color = darkThemeColor,
                        label = "Dark Theme",
                        onClick = { isConfiguringLightThemeColor = false },
                        isSelected = !isConfiguringLightThemeColor,
                    )
                }
            } else {
                ColorTile(
                    color = staticColor,
                    onClick = { /* do nothing */ },
                    isSelected = false,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HsvColorPicker(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                controller = controller,
                onColorChanged = { colorEnvelope ->
                    if (!useDynamicColor) {
                        staticColor = colorEnvelope.color
                        return@HsvColorPicker
                    }
                    if (isConfiguringLightThemeColor) {
                        lightThemeColor = colorEnvelope.color
                    } else {
                        darkThemeColor = colorEnvelope.color
                    }
                }
            )
            AlphaSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(35.dp),
                controller = controller,
            )
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(35.dp),
                controller = controller,
            )
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ColorTile(
    color: Color,
    label: String? = null,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box {
            Text(
                text = color.toHexCode(),
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = color.toHexCode(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                style = LocalTextStyle.current.copy(
                    drawStyle = Stroke(
                        miter = 10f,
                        width = 2f,
                        join = StrokeJoin.Round
                    )
                ),
                fontWeight = FontWeight.Bold,
            )
        }
        AlphaTile(
            selectedColor = color,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(6.dp))
                .align(Alignment.CenterHorizontally)
                .clickable { onClick() }
                .border(
                    width = 3.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                )
        )
        if (label != null) {
            Text(text = label)
        }
    }
}

@Preview
@Composable
private fun ColorTilePreview() = PreviewContainer {
    ColorTile(
        color = Color.Black,
        label = "Light Theme",
        onClick = {},
        isSelected = true,
    )
}

@Preview
@Composable
private fun ColorPickerDialogPreview() = PreviewContainer {
    ColorPickerDialog(
        onDismiss = {},
        onCancel = {},
        onColorConfirm = {},
    )
}

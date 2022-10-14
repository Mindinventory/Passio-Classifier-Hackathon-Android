package com.mindinventory.dermatai.common.extensions

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.mindinventory.dermatai.R

/**
 * Uses for set gradient color in text
 */
fun AppCompatTextView.setGradientText(context: Context) {
    val width = paint.measureText(text.toString())
    val textShader: Shader = LinearGradient(
        0f, 0f, width, textSize, intArrayOf(
            ContextCompat.getColor(context, R.color.iris),
            ContextCompat.getColor(context, R.color.iris_light)
        ), null, Shader.TileMode.CLAMP
    )
    paint.shader = textShader
}
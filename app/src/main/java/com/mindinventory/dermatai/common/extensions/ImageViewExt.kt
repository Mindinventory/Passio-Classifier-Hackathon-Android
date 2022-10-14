package com.mindinventory.dermatai.common.extensions

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.imageview.ShapeableImageView
import com.mindinventory.dermatai.R
import com.mindinventory.dermatai.common.utils.GlideApp

/**
 * Uses for load bitmap
 */
fun AppCompatImageView.loadImage(bitmap: Bitmap?) {
    GlideApp.with(this)
        .asBitmap()
        .load(bitmap)
        .centerCrop()
        .into(this)
}

/**
 * Uses for image with rounded shape
 */
fun ShapeableImageView.loadImage(path: String?) {
    GlideApp.with(this)
        .asBitmap()
        .load(path)
        .centerCrop()
        .placeholder(R.drawable.ic_placeholder)
        .into(this)
}

/**
 * Uses for rotate the bitmap
 */
fun Bitmap.getRotated(degrees:Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    val scaledBitmap = Bitmap.createScaledBitmap(this, this.width, this.height, true)
    return Bitmap.createBitmap(
        scaledBitmap,
        0,
        0,
        scaledBitmap.width,
        scaledBitmap.height,
        matrix,
        true
    )
}
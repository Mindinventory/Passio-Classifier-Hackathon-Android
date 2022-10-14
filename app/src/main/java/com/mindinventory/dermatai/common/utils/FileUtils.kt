package com.mindinventory.dermatai.common.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import androidx.core.content.FileProvider
import com.mindinventory.dermatai.BuildConfig
import com.mindinventory.dermatai.R
import java.io.*
import java.nio.charset.StandardCharsets


object FileUtils {

    /**
     * Uses for get json data from asset
     */
    fun Context.getAssetJsonData(): String? {
        val json: String? = try {
            val `is`: InputStream = assets.open("disease.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    /**
     * Uses for share bitmap
     */
    fun Context.shareImage(bitmap: Bitmap, disease: String, message: String) {
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/${disease}${System.currentTimeMillis()}.png"
        val out: OutputStream?
        val file = File(path)
        try {
            out = FileOutputStream(file)
            bitmap
                .compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()

            val bmpUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
            val subject: String = String.format(
                getString(R.string.consulting_for_skin_disease),
                disease
            )

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
            shareIntent.type = "image/*"
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (shareIntent.resolveActivity(
                    this.packageManager
                ) != null
            ) {
                startActivity(
                    Intent.createChooser(
                        shareIntent,
                        null
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
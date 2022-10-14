package com.mindinventory.dermatai.common.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mindinventory.dermatai.R
import com.mindinventory.dermatai.common.utils.FileUtils.shareImage
import com.mindinventory.dermatai.data.DiseaseModel

/**
 * Uses for share disease image and detail
 */
fun Context.shareDisease(diseaseModel: DiseaseModel) {
    val message: String = String.format(
        getString(R.string.kindly_refer_attached_disease),
        diseaseModel.diseasename
    )

    diseaseModel.image?.let { bitmap ->
        shareImage(
            bitmap,
            diseaseModel.diseasename,
            message
        )
    }
}

/**
 * Uses for search near by clinic
 */
fun Context.seeNearByClinic() {
    val gmmIntentUri =
        Uri.parse("geo:0,0?q=near+by+skin+specialist")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    startActivity(mapIntent)
}
package com.mindinventory.dermatai.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiseaseModel(
    val image: Bitmap?,
    val diseasename: String = "",
    val description: String? = "",
    val passioid: String? = "",
    val imagepath: String? = ""
) :
    Parcelable

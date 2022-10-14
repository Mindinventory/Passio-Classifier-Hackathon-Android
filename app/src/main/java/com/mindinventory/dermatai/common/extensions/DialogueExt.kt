package com.mindinventory.dermatai.common.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.mindinventory.dermatai.R
import com.mindinventory.dermatai.data.DiseaseModel

/**
 * Uses for display confirmation dialog
 */
fun Context.showConfirmationDialogue(
    message: String,
    textBtnPositive: String,
    textBtnNegative: String,
    onConfirm: (Dialog) -> Unit,
    onDeny: (Dialog) -> Unit
) {
    val factory = LayoutInflater.from(this)
    val customDialogView: View = factory.inflate(R.layout.confirmation_dialog, null)
    val dialog = AlertDialog.Builder(this).create()
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setView(customDialogView)
    customDialogView.findViewById<AppCompatTextView>(R.id.tvMessage).text = message
    val btnYes = customDialogView.findViewById<MaterialButton>(R.id.btnYes)
    val btnNo = customDialogView.findViewById<MaterialButton>(R.id.btnNo)
    btnYes.text = textBtnPositive
    btnNo.text = textBtnNegative
    btnYes.setOnClickListener {
        onConfirm(dialog)
    }
    customDialogView.findViewById<MaterialButton>(R.id.btnNo).setOnClickListener {
        onDeny(dialog)
    }

    dialog.show()
}

/**
 * Uses for display scanned disease dialog
 */
@SuppressLint("InflateParams")
fun Activity.displayDiseaseDetailsDialog(
    diseaseModel: DiseaseModel,
    onShare: (disease: DiseaseModel) -> Unit,
    onNearBy: (disease: DiseaseModel) -> Unit,
    onClose: () -> Unit
): AlertDialog {
    val factory = LayoutInflater.from(this)
    val customDialogView: View = factory.inflate(R.layout.dialog_disease_detail, null)
    val dialog = AlertDialog.Builder(this).create()
    dialog.apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setView(customDialogView)
        setCancelable(false)

        val tvDiseaseName = customDialogView.findViewById<AppCompatTextView>(R.id.tvDisease)
        val ivScanned = customDialogView.findViewById<ShapeableImageView>(R.id.ivScanned)
        val ivDisease = customDialogView.findViewById<ShapeableImageView>(R.id.ivDisease)
        val tvOverviewLabel = customDialogView.findViewById<AppCompatTextView>(R.id.tvOverviewLabel)
        val tvOverview = customDialogView.findViewById<AppCompatTextView>(R.id.tvOverview)
        val btnClose = customDialogView.findViewById<ImageButton>(R.id.ibClose)
        val btnShare = customDialogView.findViewById<MaterialButton>(R.id.btnShare)
        val btnNearBy = customDialogView.findViewById<MaterialButton>(R.id.btnNearBy)

        with(diseaseModel)
        {
            tvDiseaseName.text = diseasename
            ivScanned.loadImage(image)
            imagepath.let {
                ivDisease.loadImage(it)
            }
            description?.let {
                tvOverview.text = it
                tvOverviewLabel.isVisible = it.isNotEmpty()
                tvOverview.isVisible = it.isNotEmpty()
            }
        }

        btnClose?.setOnClickListener {
            onClose.invoke()
            dialog.dismiss()
        }
        btnShare.setOnClickListener {
            onShare.invoke(diseaseModel)
        }
        btnNearBy.setOnClickListener {
            onNearBy.invoke(diseaseModel)
        }
    }
    dialog.show()
    return dialog
}


/**
 * Uses for display scanned disease dialog
 */
@SuppressLint("InflateParams")
fun Activity.displaySearchResultDialog(
    diseaseModel: DiseaseModel,
    onNearBy: (disease: DiseaseModel) -> Unit
): AlertDialog {
    val factory = LayoutInflater.from(this)
    val customDialogView: View = factory.inflate(R.layout.dialog_search_result, null)
    val dialog = AlertDialog.Builder(this).create()
    dialog.apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setView(customDialogView)
        setCancelable(false)

        val tvDiseaseName = customDialogView.findViewById<AppCompatTextView>(R.id.tvDisease)
        val ivDisease = customDialogView.findViewById<ShapeableImageView>(R.id.ivDisease)
        val tvOverview = customDialogView.findViewById<AppCompatTextView>(R.id.tvOverview)
        val tvOverviewLabel = customDialogView.findViewById<AppCompatTextView>(R.id.tvOverviewLabel)
        val btnClose = customDialogView.findViewById<ImageButton>(R.id.ibClose)
        val btnNearBy = customDialogView.findViewById<MaterialButton>(R.id.btnNearBy)

        with(diseaseModel)
        {
            tvDiseaseName.text = diseasename
            imagepath.let {
                ivDisease.loadImage(it)
            }
            description?.let {
                tvOverview.text = it
                tvOverviewLabel.isVisible = it.isNotEmpty()
                tvOverview.isVisible = it.isNotEmpty()
            }
        }

        btnClose?.setOnClickListener {
            dialog.dismiss()
        }
        btnNearBy.setOnClickListener {
            onNearBy.invoke(diseaseModel)
        }
    }
    dialog.show()
    return dialog
}


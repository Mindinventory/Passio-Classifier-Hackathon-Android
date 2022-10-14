package com.mindinventory.dermatai.ui.scan

import ai.passio.passiosdk.platform.ClassificationCandidate
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.mindinventory.dermatai.data.DiseaseModel
import com.mindinventory.dermatai.ui.base.SingleLiveEvent

class ScanViewModel : ViewModel() {

    var scanningState = SingleLiveEvent<ScanningState>()
    private var diseases = arrayListOf<DiseaseModel>()
    private var scanningResult: Pair<ClassificationCandidate, Bitmap>? = null

    /**
     * Uses for set default scanning state
     */
    fun setDefault() {
        scanningState.postValue(ScanningState.Default)
    }

    /**
     * Uses for set start scanning state
     */
    fun startScanning() {
        scanningState.postValue(ScanningState.Started)
    }

    /**
     * Uses for set stop scanning state
     */
    fun stopScanning() {
        scanningState.postValue(ScanningState.Stopped)
    }

    /**
     * Uses for set scanning result
     */
    fun setScanningResult(result: Pair<ClassificationCandidate, Bitmap>?) {
        scanningResult = result
    }

    /**
     * Uses for get scanning result
     */
    fun getScanningResult(): Pair<ClassificationCandidate, Bitmap>? {
        return scanningResult
    }

    /**
     * Uses for set disease list
     */
    fun setDiseases(diseaseList: ArrayList<DiseaseModel>) {
        diseases.clear()
        diseases.addAll(diseaseList)
    }

    /**
     * Uses for get diseases
     */
    fun getDiseaseList(): ArrayList<DiseaseModel> {
        return diseases
    }
}
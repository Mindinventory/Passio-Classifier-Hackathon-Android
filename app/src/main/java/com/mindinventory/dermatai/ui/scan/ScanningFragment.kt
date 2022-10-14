package com.mindinventory.dermatai.ui.scan

import ai.passio.passiosdk.core.camera.PassioCameraViewProvider
import ai.passio.passiosdk.platform.*
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.mindinventory.dermatai.BuildConfig
import com.mindinventory.dermatai.R
import com.mindinventory.dermatai.common.extensions.*
import com.mindinventory.dermatai.common.utils.Constant.DEFAULT_RESET_SCANNING
import com.mindinventory.dermatai.common.utils.Constant.SCANNING_TIME_LIMIT_IN_MILLIS
import com.mindinventory.dermatai.common.utils.Constant.STORAGE_PERMISSION
import com.mindinventory.dermatai.common.utils.FileUtils.getAssetJsonData
import com.mindinventory.dermatai.data.DiseaseModel
import com.mindinventory.dermatai.databinding.FragmentScanBinding
import com.mindinventory.dermatai.ui.base.BaseFragment


/**
 * This class uses for scan disease and display the result
 */
class ScanningFragment : BaseFragment<FragmentScanBinding>(), PassioCameraViewProvider {

    private var lastScanningStarted = System.currentTimeMillis()
    private var resultDialog: AlertDialog? = null

    private val scanViewModel: ScanViewModel by lazy {
        ViewModelProvider(requireActivity())[ScanViewModel::class.java]
    }

    override fun getClassName(): String {
        return this::class.java.simpleName
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): FragmentScanBinding {
        return FragmentScanBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        binding.tvTitle.setGradientText(requireContext())

        // Keep device awake when damage scanner module is on
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        configureSDK()

        initClickListener()

        askForPermission()

        fetchDiseaseLocalData()
    }

    /**
     * Uses for fetch disease local data from asset
     */
    private fun fetchDiseaseLocalData() {
        val jsonData = requireContext().getAssetJsonData()
        jsonData?.apply {
            val diseases = Gson().fromJson(this, Array<DiseaseModel>::class.java).toList()
            scanViewModel.setDiseases(diseases as ArrayList<DiseaseModel>)
        }
    }

    /**
     * Uses for ask camera permission
     */
    private fun askForPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onCameraPermissionGranted()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                onCameraPermissionGranted()
            } else {
                onCameraPermissionDenied()
            }
        }

    /**
     * Uses for configure the Passio SDK
     */
    private fun configureSDK() {
        val config = PassioConfiguration(
            requireActivity(),
            BuildConfig.API_KEY
        ).apply {
            projectId = BuildConfig.PROJECT_ID
        }
        PassioSDK.instance.configure(config, ::onConfigComplete)
    }

    /**
     * Uses for start Passio SDK Camera
     */
    private fun onCameraPermissionGranted() {
        PassioSDK.instance.startCamera(this)
    }

    /**
     * Uses for display confirmation pop-up on permission denied
     */
    private fun onCameraPermissionDenied() {
        openConfirmationPopup(
            getString(R.string.allow_to_access_camera),
            onDeny = {
                launchInstructionScreen()
            })
    }

    /**
     * Uses for get the SDK configuration status
     */
    private fun onConfigComplete(status: PassioStatus) {
        requireActivity().runOnUiThread {
            when (status.mode) {
                PassioMode.NOT_READY -> Toast.makeText(
                    requireContext(),
                    getString(R.string.sdk_not_ready),
                    Toast.LENGTH_SHORT
                )
                    .show()
                PassioMode.FAILED_TO_CONFIGURE -> Toast.makeText(
                    requireContext(),
                    getString(R.string.sdk_failed_to_configure),
                    Toast.LENGTH_SHORT
                ).show()
                PassioMode.IS_BEING_CONFIGURED -> Toast.makeText(
                    requireContext(),
                    getString(R.string.sdk_being_configured),
                    Toast.LENGTH_SHORT
                ).show()
                PassioMode.IS_READY_FOR_DETECTION -> {

                }
            }
        }
    }

    /**
     * Uses for initialize click listener
     */
    private fun initClickListener() {
        with(binding) {
            btnScan.setOnClickListener {
                lastScanningStarted = System.currentTimeMillis()
                scanViewModel.startScanning()
            }

            ibInfo.setOnClickListener {
                refreshScanning()
                launchInstructionScreen()
            }

            ibRefresh.setOnClickListener {
                refreshScanning()
            }

            ibSearch.setOnClickListener {
                refreshScanning()
                launchSearchScreen()
            }
        }
    }

    /**
     * Uses for launch search Screen
     */
    private fun launchSearchScreen() {
        findNavController().navigate(R.id.searchFragment)
    }

    /**
     * Uses for refresh the scanning state
     */
    private fun refreshScanning() {
        stopDetection()
        stopScanAnimation()
        show(binding.btnScan)
        scanViewModel.setScanningResult(null)
        scanViewModel.setDefault()
        if (resultDialog != null && resultDialog?.isShowing == true) {
            resultDialog!!.dismiss()
        }
    }

    override fun initObservers() {
        super.initObservers()
        scanViewModel.scanningState.safeObserve(viewLifecycleOwner, ::manageScanningState)
    }

    /**
     * Uses for manage scanning status
     */
    private fun manageScanningState(scanningState: ScanningState) {
        when (scanningState) {
            ScanningState.Default -> {

            }
            ScanningState.Started -> {
                with(binding)
                {
                    scanViewModel.setScanningResult(null)
                    startDiseaseScanning()
                    onStartScanning()
                    if (!lottieViewScanning.isAnimating) {
                        lottieViewScanning.playAnimation()
                    }
                }
            }

            ScanningState.Stopped -> {
                onStopScanning()
            }
        }
    }

    /**
     * Uses for start scanning
     */
    private fun onStartScanning() {
        with(binding) {
            lottieViewScanning.playAnimation()
            hide(btnScan)
        }
    }

    override fun onStart() {
        super.onStart()
        if (scanViewModel.scanningState.value == ScanningState.Started) {
            scanViewModel.startScanning()
        }
    }

    /**
     * Uses for start disease scanning
     */
    private fun startDiseaseScanning() {
        PassioSDK.instance.startDetection(listener)
    }

    override fun onStop() {
        stopDetection()
        super.onStop()
    }

    private val listener = object : ClassificationListener {
        override fun onClassificationResult(candidate: ClassificationCandidate, bitmap: Bitmap) {
            if (lastScanningStarted == DEFAULT_RESET_SCANNING) return
            val lastMilliSecond: Long = System.currentTimeMillis() - lastScanningStarted
            if (lastMilliSecond > SCANNING_TIME_LIMIT_IN_MILLIS) {
                scanViewModel.stopScanning()
            } else {
                val previousResult = scanViewModel.getScanningResult()
                if (previousResult != null) {
                    if (candidate.confidence > previousResult.first.confidence) {
                        scanViewModel.setScanningResult(Pair(candidate, bitmap))
                    }
                } else {
                    scanViewModel.setScanningResult(Pair(candidate, bitmap))
                }
            }
        }
    }

    /**
     * Uses for stop scanning
     */
    private fun onStopScanning() {
        stopDetection()
        stopScanAnimation()

        val scannedResult = scanViewModel.getScanningResult()
        if (scannedResult != null) {
            val scannedConfidence = scannedResult.first.confidence * 100
            if (scannedConfidence > 50.0) {
                resultDialog = requireActivity().displayDiseaseDetailsDialog(
                    getScannedData(),
                    onShare = {
                        show(binding.btnScan)
                        if (hasPermissions(
                                requireContext(),
                                STORAGE_PERMISSION
                            )
                        ) {
                            requireContext().shareDisease(it)
                        } else {
                            storagePermissionReq.launch(STORAGE_PERMISSION)
                        }
                    }, onNearBy = {
                        show(binding.btnScan)
                        requireContext().seeNearByClinic()
                    }, onClose = {
                        show(binding.btnScan)
                    }
                )
            } else {
                show(binding.btnScan)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_result_found),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Uses for get disease data
     */
    private fun getScannedData(): DiseaseModel {
        val scannedResult = scanViewModel.getScanningResult()
        val diseases = scanViewModel.getDiseaseList()
        val disease = diseases.filter { it.passioid == scannedResult?.first?.label?.passioID }
        var description: String? = ""
        var imagePath: String? = ""
        if (disease.isNotEmpty()) {
            disease[0].apply {
                description = this.description
                imagePath = this.imagepath
            }
        }

        return DiseaseModel(
            image = scannedResult?.second?.getRotated(90F),
            diseasename = scannedResult?.first?.label?.displayName?.capitalized() ?: "",
            description = description,
            imagepath = imagePath
        )
    }

    /**
     * Uses for check permission
     */
    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    /**
     * Uses for request storage permission
     */
    private val storagePermissionReq =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            when (granted) {
                true -> {
                    requireContext().shareDisease(getScannedData())
                }
                false -> {
                    openConfirmationPopup(
                        getString(R.string.allow_to_access_photos_medias),
                        onDeny = {

                        })
                }
            }
        }

    /**
     * Uses for stop scanning animation
     */
    private fun stopScanAnimation() {
        with(binding)
        {
            lottieViewScanning.pauseAnimation()
            lottieViewScanning.progress = 0F
        }
    }

    /**
     * Uses for stop scanning
     */
    private fun stopDetection() {
        PassioSDK.instance.stopDetection()
    }

    override fun requestCameraLifecycleOwner(): LifecycleOwner = this

    override fun requestPreviewView(): PreviewView = binding.viewFinder

    /**
     * Uses for launch setting screen to allow permissions
     */
    private fun openConfirmationPopup(message: String, onDeny: () -> Unit) {
        requireContext().showConfirmationDialogue(
            message,
            getString(R.string.allow),
            getString(R.string.cancel),
            onConfirm = {
                launchInstructionScreen()
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    )
                )
                it.dismiss()
            },
            onDeny = {
                onDeny.invoke()
                it.dismiss()
            }
        )
    }

    /**
     * Uses for launch Instruction screen
     */
    private fun launchInstructionScreen() {
        findNavController().navigate(R.id.instructionFragment)
    }

}
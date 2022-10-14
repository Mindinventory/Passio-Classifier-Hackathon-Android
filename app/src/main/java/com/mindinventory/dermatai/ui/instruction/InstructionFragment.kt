package com.mindinventory.dermatai.ui.instruction

import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mindinventory.dermatai.R
import com.mindinventory.dermatai.common.utils.VerticalItemDecoration
import com.mindinventory.dermatai.data.InstructionModel
import com.mindinventory.dermatai.database.DataStoreManager
import com.mindinventory.dermatai.database.PreferencesKey
import com.mindinventory.dermatai.databinding.FragmentInstructionBinding
import com.mindinventory.dermatai.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This class uses for display instruction
 */
@AndroidEntryPoint
class InstructionFragment : BaseFragment<FragmentInstructionBinding>() {

    override fun getClassName(): String {
        return this::class.java.simpleName
    }

    @Inject lateinit var dataStoreManager: DataStoreManager
    private lateinit var instructionAdapter: InstructionAdapter

    override fun inflateLayout(layoutInflater: LayoutInflater): FragmentInstructionBinding {
        return FragmentInstructionBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        with(binding)
        {
            instructionAdapter = InstructionAdapter(getInstruction())
            val verticalItemDecoration = VerticalItemDecoration(requireContext(), 8)
            rvInstruction.addItemDecoration(verticalItemDecoration)
            rvInstruction.adapter = instructionAdapter
        }

        initClickListener()
    }

    /**
     * Uses for initialize click listener
     */
    private fun initClickListener() {
        binding.btnScan.setOnClickListener {
            launchScanFlow()
        }
    }

    /**
     * Uses for launch scanning screen
     */
    private fun launchScanFlow() {
        lifecycleScope.launch(Dispatchers.Main) {
            dataStoreManager.setValue(PreferencesKey.TUTORIAL_KEY, true)
        }
        findNavController().navigate(R.id.scanningFragment)
    }

    /**
     * Uses for get a list of instructions
     */
    private fun getInstruction(): ArrayList<InstructionModel> {
        return arrayListOf(
            InstructionModel(
                title = getString(R.string.scan),
                description = getString(R.string.scan_detail)
            ),
            InstructionModel(
                title = getString(R.string.result),
                description = getString(R.string.result_detail)
            ),
            InstructionModel(
                title = getString(R.string.reach_out_doctor),
                description = getString(R.string.reach_doctor_detail)
            ),
        )
    }
}
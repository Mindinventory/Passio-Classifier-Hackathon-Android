package com.mindinventory.dermatai.ui.search

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mindinventory.dermatai.common.extensions.displaySearchResultDialog
import com.mindinventory.dermatai.common.extensions.hideKeyboard
import com.mindinventory.dermatai.common.extensions.seeNearByClinic
import com.mindinventory.dermatai.common.utils.VerticalItemDecoration
import com.mindinventory.dermatai.data.DiseaseModel
import com.mindinventory.dermatai.databinding.FragmentSearchBinding
import com.mindinventory.dermatai.ui.base.BaseFragment
import com.mindinventory.dermatai.ui.scan.ScanViewModel

/**
 * This class provides list of skin diseases along with search feature
 */
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val scanViewModel: ScanViewModel by lazy {
        ViewModelProvider(requireActivity())[ScanViewModel::class.java]
    }

    private lateinit var searchAdapter: SearchAdapter

    override fun getClassName(): String {
        return this::class.java.simpleName
    }

    override fun inflateLayout(layoutInflater: LayoutInflater): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        registerSearchTextListener()
        initClickListener()

        with(binding)
        {
            searchAdapter = SearchAdapter(::handleDiseaseClick)
            searchAdapter.addData(scanViewModel.getDiseaseList())
            val verticalItemDecoration = VerticalItemDecoration(requireContext(), 8)
            rvSearch.addItemDecoration(verticalItemDecoration)
            rvSearch.adapter = searchAdapter
        }
    }

    /**
     * Uses for initialize click listener
     */
    private fun initClickListener() {
        with(binding)
        {
            ibClear.setOnClickListener {
                it.hideKeyboard()
                etSearch.setText("")
            }

            ibPrev.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    /**
     * Uses for register text change listener to get the search text
     */
    private fun registerSearchTextListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                searchAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    /**
     * Uses for handle disease click
     */
    private fun handleDiseaseClick(diseaseModel: DiseaseModel) {
        requireActivity().displaySearchResultDialog(
            diseaseModel,
            onNearBy = {
                requireContext().seeNearByClinic()
            }
        )
    }
}
package com.mindinventory.dermatai.ui.scan

import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.mindinventory.dermatai.R
import com.mindinventory.dermatai.database.DataStoreManager
import com.mindinventory.dermatai.database.PreferencesKey
import com.mindinventory.dermatai.databinding.ActivityScanBinding
import com.mindinventory.dermatai.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This class uses for disease scanning feature
 */
@AndroidEntryPoint
class ScanningActivity : BaseActivity<ActivityScanBinding>() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityScanBinding {
        return ActivityScanBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_main)

        lifecycleScope.launch(Dispatchers.Main) {
            dataStoreManager.getValue(PreferencesKey.TUTORIAL_KEY, false)
                .collect { isTutorialWatched ->
                    if (isTutorialWatched) {
                        navGraph.setStartDestination(R.id.scanningFragment)
                        navController.setGraph(navGraph, null)
                    } else {
                        navGraph.setStartDestination(R.id.instructionFragment)
                        navController.setGraph(navGraph, null)
                    }
                }
        }
    }
}
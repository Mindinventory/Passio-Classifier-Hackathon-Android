package com.mindinventory.dermatai.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.mindinventory.dermatai.common.utils.Constant
import com.mindinventory.dermatai.databinding.ActivitySplashBinding
import com.mindinventory.dermatai.ui.base.BaseActivity
import com.mindinventory.dermatai.ui.scan.ScanningActivity

/**
 * This class uses for display splash screen
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun inflateLayout(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        Handler(Looper.getMainLooper()).postDelayed({
            launchNextScreen()
        }, Constant.SPLASH_DEFAULT_TIME)
    }

    /**
     * If instruction remain to viewed then launch Instruction screen otherwise launch scanning screen
     */
    private fun launchNextScreen() {
        startActivity(Intent(this@SplashActivity, ScanningActivity::class.java))
        finish()
    }
}
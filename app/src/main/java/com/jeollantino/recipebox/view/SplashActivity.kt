package com.jeollantino.recipebox.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.jeollantino.recipebox.R
import com.jeollantino.recipebox.view.recipelist.RecipeListActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class SplashActivity : com.jeollantino.recipebox.base.BaseActivity() {

    val TAG: String? = SplashActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        init()
    }

    private fun init() {
        val intent = Intent(this, RecipeListActivity::class.java)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN_TIME)
    }

    companion object {
        /**
         * Splash screen time set to 2 seconds
         */
        const val SPLASH_SCREEN_TIME: Long = 2 * 1000
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}

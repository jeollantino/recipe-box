package com.jeollantino.recipebox

import android.app.Application
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class RecipeBoxApplication : Application() {

    companion object {
    }

    override fun onCreate() {
        super.onCreate()
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/montserrat_regular.ttf")
                            // .setFontAttrId(R.attr.font)
                            .build()
                    )
                )
                .build()
        )
    }
}

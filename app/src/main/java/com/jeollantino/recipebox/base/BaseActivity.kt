package com.jeollantino.recipebox.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseActivity : AppCompatActivity() {

    fun setInitialFragment(id: Int, fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(id, fragment)
        ft.commit()
    }

    fun switchFragment(id: Int, fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(id, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    fun slideFragment(id: Int, fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()

        ft.replace(id, fragment)
        ft.addToBackStack(null)
        ft.commitAllowingStateLoss()
    }
}

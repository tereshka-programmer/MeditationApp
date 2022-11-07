package com.example.tranyapp.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.tranyapp.R

class MainActivity : AppCompatActivity() {

    // nav controller of the current screen
    private var navController: NavController? = null

    // fragment listener is sued for tracking current nav controller
    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment) return
            onNavControllerActivated(f.findNavController())
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this,R.color.gray_back)

        val navController = getRootNavController()
        onNavControllerActivated(navController)
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (navController?.currentDestination?.id == getCoursesDestination()) {
            super.onBackPressed()
        } else {
            navController?.popBackStack()
        }
    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        return navHost.navController
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController = navController
    }

    private fun getCoursesDestination(): Int = R.id.coursesFragment
}
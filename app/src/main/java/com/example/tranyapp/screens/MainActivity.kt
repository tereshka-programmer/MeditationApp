package com.example.tranyapp.screens

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.tranyapp.App
import com.example.tranyapp.R
import com.example.tranyapp.services.PlayerService
import com.example.tranyapp.utils.MusicState
import com.example.tranyapp.utils.PlayerRemote


class MainActivity : AppCompatActivity() {

    // nav controller of the current screen
    private var navController: NavController? = null


    private val boundServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder: PlayerService.MusicBinder = service as PlayerService.MusicBinder
            PlayerRemote.playerService = binder.getService()
            PlayerRemote.mBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            PlayerRemote.playerService?.runAction(MusicState.STOP)
            PlayerRemote.playerService = null
            PlayerRemote.mBound = false
        }
    }


    // fragment listener is sued for tracking current nav controller
    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?,
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment) return
            onNavControllerActivated(f.findNavController())
        }
    }


    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.songRepository.initializeSongRepository(this)
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkStoragePermission()) {
                //it's okay
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    RQ_PERMISSION_FOR_STORAGE
                )
            }
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.gray_back)

        val navController = getRootNavController()
        onNavControllerActivated(navController)
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    override fun onStart() {
        super.onStart()
        if (!PlayerRemote.mBound) bindToMusicService()
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
//        unbindMusicService()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RQ_PERMISSION_FOR_STORAGE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //it's okay
                } else {
                    this.finishAffinity()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (navController?.currentDestination?.id == getCoursesDestination()) {
            super.onBackPressed()
        } else {
            navController?.popBackStack()
        }
    }

    private fun getRootNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        return navHost.navController
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController = navController
    }

    private fun getCoursesDestination(): Int = R.id.coursesFragment

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun bindToMusicService() {
        Intent(this, PlayerService::class.java).also {
            bindService(it, boundServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindMusicService() {
        PlayerRemote.playerService?.runAction(MusicState.STOP)
        unbindService(boundServiceConnection)
    }

    private companion object {
        const val RQ_PERMISSION_FOR_STORAGE = 1
    }
}
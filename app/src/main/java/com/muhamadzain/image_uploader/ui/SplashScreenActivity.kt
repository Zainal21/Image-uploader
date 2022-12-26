package com.muhamadzain.image_uploader.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.muhamadzain.image_uploader.R
import com.muhamadzain.image_uploader.base.BaseActivity
import com.muhamadzain.image_uploader.ui.home.HomeActivity

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {

    override fun getLayoutResource(): Int  = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed(
            {
                val i = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                startActivity(i)
                finish()
            }, 2000L
        )
    }

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (!hasNetworkStatePermission()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_NETWORK_STATE)
        }

        if (!hasChangeNetworkStatePermission()) {
            permissionsToRequest.add(Manifest.permission.CHANGE_NETWORK_STATE)
        }

        if (!hasCameraPermission()) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            var grantAll : Boolean = true
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    grantAll = false
                }
            }

        }
    }

    private fun hasNetworkStatePermission() : Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasChangeNetworkStatePermission() : Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasCameraPermission() : Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

}
package com.muhamadzain.image_uploader.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.muhamadzain.image_uploader.BuildConfig

class ManagePermissions(private val activity: Activity, private val list: MutableList<String>, val code: Int) {

    lateinit var listener: Listener

    fun checkPermissions(mListener: Listener) {
        listener = mListener
        if (isPermissionsGranted() != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
        } else {
            mListener.granted()
        }
    }

    private fun isPermissionsGranted(): Int {
        var counter = 0
        for (permission in list) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter
    }

    private fun deniedPermission(): String {
        for (permission: String in list) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED
            ) return permission
        }
        return ""
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Ubah Setelan Sistem")
        builder.setMessage("Beberapa perizinan diperlukan untuk membantu sistem berjalan efektif")
        builder.setPositiveButton("Perbaiki!") { _, _ ->
            activity.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                )
            )
        }
        builder.setNegativeButton("Batal") { _, _ ->
            listener.cancel()
        }
        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermission())) {
            showAlert()
        } else {
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)
        }
    }

    interface Listener {
        fun granted()
        fun cancel()
    }
}
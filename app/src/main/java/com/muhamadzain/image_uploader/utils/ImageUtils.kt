package com.muhamadzain.image_uploader.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    const val IMG_EXT = ".jpg"
    fun compressImage(imageFilePathFrom: File, imageFilePathTo: File): File {
        val targetW = 600
        val targetH = 800
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePathFrom.absolutePath, bmOptions)
        val scaleFactor = Math.min(bmOptions.outWidth / targetW, bmOptions.outHeight / targetH)
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        val bitmap = BitmapFactory.decodeFile(imageFilePathFrom.absolutePath, bmOptions)
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(imageFilePathFrom.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        assert(exif != null)
        val orientString = exif!!.getAttribute(ExifInterface.TAG_ORIENTATION)
        val orientation = orientString?.toInt() ?: ExifInterface.ORIENTATION_NORMAL
        var rotationAngle = 0
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270
        val matrix = Matrix()
        matrix.setRotate(
            rotationAngle.toFloat(),
            bitmap.width.toFloat() / 2,
            bitmap.height.toFloat() / 2
        )
        val rotatedBitmap = Bitmap.createBitmap(
            bitmap, 0, 0,
            bmOptions.outWidth, bmOptions.outHeight, matrix, true
        )
        try {
            FileOutputStream(imageFilePathTo, false).use { out ->
                rotatedBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    50,
                    out
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return imageFilePathTo
    }

    private fun getPhotoFileDir(context: Context): File? {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    }

    fun createPhotoFile(context: Context): File {
        return File(getPhotoFileDir(context), generatePhotoName())
    }

    fun getFile(context: Context, name: String?): File {
        return File(getPhotoFileDir(context), name)
    }

    private fun generatePhotoName(): String {
        return "IMG_" + timestamp().toString() + IMG_EXT
    }

    interface OnDeleteImageListener {
        fun onDeleteImage(path: String?)
    }

    private fun timestamp(): Long {
        val date = Calendar.getInstance().time
        val sto =
            SimpleDateFormat("dd/MM/yyyy H:m", Locale.getDefault())
        return date.time
    }
}
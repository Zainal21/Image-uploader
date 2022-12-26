package com.muhamadzain.image_uploader.ui.home

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.droidman.ktoasty.KToasty
import com.muhamadzain.image_uploader.R
import com.muhamadzain.image_uploader.base.BaseActivity
import com.muhamadzain.image_uploader.dataclass.ResponseModel
import com.muhamadzain.image_uploader.utils.ImageUtils
import com.muhamadzain.image_uploader.utils.ManagePermissions
import com.muhamadzain.image_uploader.utils.PreferencesManager
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

class HomeActivity : BaseActivity() , HomeContract.View {

    private lateinit var presenter: HomePresenter
    private var pd: ProgressDialog? = null
    private lateinit var managePermission: ManagePermissions
    private lateinit var btnTakephoto : AppCompatButton
    private lateinit var btnOpenedGallery : AppCompatButton
    private lateinit var btnUploadPhoto : AppCompatButton
    private lateinit var previewImage : ImageView

    companion object{
        private val REQUIRED_PERMISSIONS = mutableListOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_CAMERA = 101
        private const val REQUEST_CODE_PHOTO = 202
        var fileImg: File? = null
    }

    private fun getImagePath(): String = PreferencesManager.getImagePath(this)

    override fun getLayoutResource(): Int = R.layout.activity_home

    private fun initComponent(){
        btnTakephoto = findViewById(R.id.btn_take_photo)
        btnOpenedGallery = findViewById(R.id.btn_opened_galery)
        btnUploadPhoto = findViewById(R.id.btn_upload)
        previewImage = findViewById(R.id.previewImage)
    }

    private fun initEventListener(){

        btnOpenedGallery.setOnClickListener {
            EasyImage.openGallery(this@HomeActivity, REQUEST_CODE_PHOTO)
        }

        btnTakephoto.setOnClickListener {
            EasyImage.openCamera(this@HomeActivity, REQUEST_CODE_PHOTO)
        }

        btnUploadPhoto.setOnClickListener {
            sendTransaction()
        }
    }

    private fun sendTransaction() {
        if(fileImg == null) {
            KToasty.info(this, "No Image Uploaded.", Toast.LENGTH_SHORT, true).show()
        }else{
            presenter.onSubmit(fileImg!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        managePermission = ManagePermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_CAMERA)
        presenter = HomePresenter(this)
        setContentView(R.layout.activity_home)
        initComponent()
        initEventListener()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, this,
            object : DefaultCallback() {
                override fun onImagePicked(
                    imageFile: File,
                    source: EasyImage.ImageSource,
                    type: Int
                ) {
                    if (type == REQUEST_CODE_PHOTO) {
                        deleteRecursive(getImagePath())
                        val fileImage = ImageUtils.compressImage(
                            imageFile, ImageUtils.createPhotoFile(applicationContext)
                        )
                        if (imageFile.delete()) Log.i("status", "Image Deleted")
                        val imageName =
                            fileImage.absolutePath.substring(fileImage.absolutePath.lastIndexOf("/") + 1)
                        Log.i("path: $fileImage", imageName)
                        Glide.with(applicationContext)
                            .load(fileImage)
                            .apply(RequestOptions.fitCenterTransform())
                            .into(previewImage)
                        PreferencesManager.setImagePath(applicationContext, fileImage.toString())
                        fileImg = fileImage
                        btnTakephoto.text = getString(R.string.take_photo)
                    }
                }
            })
    }

    private fun deleteRecursive(strPath: String) {
        val fileOrDirectory = File(strPath)
        if (fileOrDirectory.isDirectory) {
            for (child in fileOrDirectory.listFiles()!!) deleteRecursive(child.path)
            fileOrDirectory.delete()
        } else {
            fileOrDirectory.delete()
        }
        PreferencesManager.setImagePath(this, "")
    }

    override fun onsucces(response: ResponseModel) {
        Glide.with(applicationContext)
            .load(R.color.shimmerColor)
            .apply(RequestOptions.fitCenterTransform())
            .into(previewImage)
        fileImg = null
        KToasty.success(this@HomeActivity, response.message.toString(), Toast.LENGTH_SHORT, true).show()
    }

    override fun onreject(reject: ResponseModel) {
        KToasty.error(this, reject.message.toString(), Toast.LENGTH_SHORT, true).show()
    }

    override fun showLoading() {
        this.pd = ProgressDialog.show(this, "","Loading....", true, false);
    }

    override fun hideLoading() {
        this.pd?.dismiss()
    }
}
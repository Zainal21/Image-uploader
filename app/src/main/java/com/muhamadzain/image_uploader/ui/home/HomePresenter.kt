package com.muhamadzain.image_uploader.ui.home

import android.util.Log
import com.muhamadzain.image_uploader.network.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class HomePresenter (
    private val view : HomeContract.View
) : HomeContract.Presenter{

    private val mCompositeDisposable : CompositeDisposable?

    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun onSubmit(file: File) {
        view.showLoading()
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val disposable = Api.endpoint.postImageUpload(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.hideLoading()
                if(it.success == true){
                    view.onsucces(it)
                }else{
                    view.onreject(it)
                }
                Log.d(" success ", " Success ${it.toString()}")
            },{
                view.hideLoading()
                Log.e("error", it.toString())
            })
        mCompositeDisposable!!.add(disposable)
    }

    override fun subscribe() {
    }

    override fun unsubscribe() {
        mCompositeDisposable!!.clear()
    }

}
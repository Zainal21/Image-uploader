package com.muhamadzain.image_uploader.network

import com.muhamadzain.image_uploader.dataclass.ResponseModel
import com.muhamadzain.image_uploader.utils.Constant
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiInterface {
    @POST("upload")
    @Multipart
    @Headers("api_key: ${Constant.API_KEY}")
    fun postImageUpload(
        @Part image: MultipartBody.Part
    ) : Flowable<ResponseModel>
}
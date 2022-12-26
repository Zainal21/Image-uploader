package com.muhamadzain.image_uploader.dataclass

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResponseModel(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

package com.muhamadzain.image_uploader.ui.home

import com.muhamadzain.image_uploader.base.BaseContract
import com.muhamadzain.image_uploader.base.BasePresenter
import com.muhamadzain.image_uploader.dataclass.ResponseModel
import java.io.File

interface HomeContract {
    interface View : BaseContract {
        fun onsucces(response: ResponseModel)
        fun onreject(reject: ResponseModel)
    }

    interface Presenter : HomeContract, BasePresenter{
        fun onSubmit(file : File)
    }
}

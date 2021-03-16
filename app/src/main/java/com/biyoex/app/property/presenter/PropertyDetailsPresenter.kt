package com.biyoex.app.property.presenter

import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.property.datas.CoinRecordBean
import com.biyoex.app.property.view.PropertyDetailsView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PropertyDetailsPresenter:BasePresent<PropertyDetailsView>() {
    fun getRecordData(coidId:Int,page:Int,pageSize:Int){
        RetrofitHelper
                .getIns()
                .zgtopApi
                .getRecordData(coidId,page,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object:BaseObserver<RequestResult<CoinRecordBean>>(this.lifecycleOwner){
                    override fun success(t: RequestResult<CoinRecordBean>) {
                        mView.getRecordDataSuccess(t!!.data,t!!.page,t!!.totalCount)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                       mView.showToast(msg)
                    }

                })
    }
}
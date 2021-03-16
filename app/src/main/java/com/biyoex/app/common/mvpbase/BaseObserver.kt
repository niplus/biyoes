package com.biyoex.app.common.mvpbase

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.gson.JsonParseException
import com.biyoex.app.common.Constants.ExceptionReason
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.common.utils.log.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * Created by xxx on 2018/8/8.
 */
abstract class BaseObserver<T> : Observer<T> {
    private var mState: Lifecycle.State? = null
    private var mDisposable: Disposable? = null

    constructor() {}
    constructor(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(Component())
    }

    override fun onSubscribe(d: Disposable) {
        mDisposable = d
    }

    override fun onComplete() {}
    override fun onNext(t: T) {
        //组件已经销毁
        if (mState == Lifecycle.State.DESTROYED) {
            return
        }
        //若返回其中包含了code，则需判断是否为200
        if (t is RequestResult<*>) {
            val requestResult = t as RequestResult<*>
            if (requestResult.code != 200) {
                //401未鉴权就退出登录
//                 TODO: 2019/5/4 可能会频繁退出登录
                if (requestResult.code == 401) {
                    val faildString: String? = if (requestResult.data is String)requestResult.data as String else null
                    failed(requestResult.code, faildString, requestResult.msg)
                    SessionLiveData.getIns().postValue(null)
                    //                    ToastUtils.showToast("未登录");
                    endOperate()
                    return
                }
                //判断data里面是否有数据
                if (requestResult.data is String) {
                    failed(requestResult.code, requestResult.data as String, requestResult.msg)
                } else {
                    failed(requestResult.code, "", requestResult.msg)
                }
                endOperate()
                return
            }
        }
        success(t)
        endOperate()
    }

    override fun onError(e: Throwable) {
        Log.e(e, "baseObserver")
        if (e is HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK)
        } else if (e is ConnectException
                || e is UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR)
        } else if (e is InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT)
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException
                || e is IllegalStateException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR)
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR)
        }
        error()
        endOperate()
    }

    /**
     * 请求异常
     *
     * @param reason
     */
    fun onException(reason: Int) {
        when (reason) {
            ExceptionReason.CONNECT_ERROR -> ToastUtils.showToast("连接服务器错误")
            ExceptionReason.CONNECT_TIMEOUT -> ToastUtils.showToast("连接服务器超时")
            ExceptionReason.BAD_NETWORK -> ToastUtils.showToast("请检查网络")
            ExceptionReason.PARSE_ERROR -> ToastUtils.showToast("数据解析错误")
            ExceptionReason.UNKNOWN_ERROR -> {
            }
        }
    }

    protected abstract fun success(t: T)
    protected open fun failed(code: Int, data: String?, msg: String?) {
//        RxBusData rxBusData = new RxBusData();
//        rxBusData.setMsgName("httpError");
//        RxBus.get().post(rxBusData);
//        ToastUtils.showToast(msg);
    }

    protected open fun error() {}
    protected open fun endOperate() {}
    internal inner class Component : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun ON_CREATE() {
            mState = Lifecycle.State.CREATED
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun ON_START() {
            mState = Lifecycle.State.STARTED
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun ON_RESUME() {
            mState = Lifecycle.State.RESUMED
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun ON_PAUSE() {
            mState = Lifecycle.State.STARTED
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun ON_STOP() {
            mState = Lifecycle.State.CREATED
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun ON_DESTROY() {
            //生命周期结束后直接解除监听
            if (mDisposable != null && !mDisposable!!.isDisposed) {
                mDisposable!!.dispose()
                mDisposable = null
            }
            mState = Lifecycle.State.DESTROYED
        }
    }
}
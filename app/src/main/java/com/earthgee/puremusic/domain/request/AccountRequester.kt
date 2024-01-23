package com.earthgee.puremusic.domain.request

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.earthgee.architecture.data.response.DataResult
import com.earthgee.architecture.data.response.ResponseStatus
import com.earthgee.architecture.data.response.ResultSource
import com.earthgee.architecture.domain.request.Requester
import com.earthgee.puremusic.data.bean.User
import com.earthgee.puremusic.data.repository.DataRepository
import com.kunminx.architecture.domain.message.MutableResult
import com.kunminx.architecture.domain.message.Result
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *  Created by earthgee on 2024/1/9
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class AccountRequester : Requester(), DefaultLifecycleObserver {

    //TODO tip 3：👆👆👆 让 accountRequest 可观察页面生命周期，
    // 从而在页面即将退出、且登录请求由于网络延迟尚未完成时，
    // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期问题。

    private var _tokenResult = MutableResult<DataResult<String>>()

    //TODO tip 4：应顺应 "响应式编程"，做好 "单向数据流" 开发，
    // MutableResult 应仅限 "鉴权中心" 内部使用，且只暴露 immutable Result 给 UI 层，
    // 通过 "读写分离" 实现数据从 "领域层" 到 "表现层" 的单向流动，

    //如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
    //https://xiaozhuanlan.com/topic/6017825943
    val tokenResult: Result<DataResult<String>> = _tokenResult

    //TODO tip 5：模拟可取消的登录请求：
    //
    // 配合可观察页面生命周期的 accountRequest，
    // 从而在页面即将退出、且登录请求由于网络延迟尚未完成时，
    // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期的问题。
    private var mDisposable: Disposable? = null

    //TODO tip 6: requester 作为数据的生产者，职责应仅限于 "请求调度 和 结果分发"，
    //
    // 换言之，此处只关注数据的生成和回推，不关注数据的使用，
    // 改变 UI 状态的逻辑代码，只应在表现层页面中编写，例如 Jetpack Compose 的使用，
    fun requestLogin(user: User) {
        DataRepository.login(user).subscribe(object : Observer<DataResult<String>> {

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onError(e: Throwable) {
                _tokenResult.postValue(
                    DataResult(
                        "",
                        ResponseStatus(e.message?: "", false, ResultSource.NETWORK)
                    )
                )
            }

            override fun onComplete() {
                mDisposable = null
            }

            override fun onNext(result: DataResult<String>) {
                _tokenResult.postValue(result)
            }

        })
    }

    fun cancelLogin() {
        if(mDisposable?.isDisposed == false) {
            mDisposable?.dispose()
            mDisposable = null
        }
    }

    //TODO tip 7：让 accountRequest 可观察页面生命周期，
    // 从而在页面即将退出、且登录请求由于网络延迟尚未完成时，
    // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期问题。

    // 关于 Lifecycle 组件的存在意义，详见《为你还原一个真实的 Jetpack Lifecycle》解析
    // https://xiaozhuanlan.com/topic/3684721950
    override fun onStop(owner: LifecycleOwner) {
        cancelLogin()
    }

}






















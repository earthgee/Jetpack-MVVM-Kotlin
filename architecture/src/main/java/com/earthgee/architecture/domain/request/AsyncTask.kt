package com.earthgee.architecture.domain.request

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 *  Created by earthgee on 2024/1/5
 *  CopyRight (c) earthgee.com
 *  功能：
 */
object AsyncTask {

    fun <T> doIO(action: ((emitter: ObservableEmitter<T>) -> Unit)) =
        Observable.create(action).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun <T> doCalculate(action: ((emitter: ObservableEmitter<T>) -> Unit)) =
        Observable.create(action).subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

    abstract class Observer<T>: io.reactivex.Observer<T> {

        override fun onSubscribe(d: Disposable) {}

        override fun onNext(t: T) {}

        override fun onError(e: Throwable) {}

        override fun onComplete() {}

    }

}


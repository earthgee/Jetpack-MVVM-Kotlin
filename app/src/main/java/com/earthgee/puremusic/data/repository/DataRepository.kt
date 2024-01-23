package com.earthgee.puremusic.data.repository

import com.earthgee.architecture.data.response.DataResult
import com.earthgee.architecture.data.response.ResponseStatus
import com.earthgee.architecture.data.response.ResultSource
import com.earthgee.architecture.domain.request.AsyncTask
import com.earthgee.architecture.utils.Utils
import com.earthgee.puremusic.R
import com.earthgee.puremusic.data.api.APIs
import com.earthgee.puremusic.data.api.AccountService
import com.earthgee.puremusic.data.bean.LibraryInfo
import com.earthgee.puremusic.data.bean.TestAlbum
import com.earthgee.puremusic.data.bean.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayInputStream
import java.util.concurrent.TimeUnit

/**
 *  Created by earthgee on 2024/1/5
 *  CopyRight (c) earthgee.com
 *  功能：
 */
object DataRepository {

    private var retrofit: Retrofit

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .writeTimeout(8, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(APIs.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //TODO tip: 通过 "响应式框架" 往领域层回推数据，
    // 与此相对应，kotlin 下使用 flow{ ... emit(...) }.flowOn(Dispatchers.xx)
    fun getFreeMusic(): Observable<DataResult<TestAlbum>> {
        return AsyncTask.doIO { emitter ->
            val gson = Gson()
            val testAlbum = gson.fromJson(
                Utils.getApp().getString(R.string.free_music_json),
                TestAlbum::class.java
            )
            emitter.onNext(DataResult(testAlbum))
        }
    }

    fun getLibraryInfo(): Observable<DataResult<List<LibraryInfo>>> {
        return AsyncTask.doIO { emitter ->
            val gson = Gson()
            val list: List<LibraryInfo> = gson.fromJson(Utils.getApp().getString(R.string.library_json),
                object : TypeToken<List<LibraryInfo>>() {/*nothing*/}.type)
            emitter.onNext(DataResult(list))
        }
    }

    /**
     * TODO：模拟下载任务:
     */
    fun downloadFile(): Observable<Int> {
        return AsyncTask.doIO { emitter ->
            //在内存中模拟 "数据读写"，假装是在 "文件 IO"，
            val bytes: ByteArray =
                byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
            bytes.inputStream().use { bis ->
                var b: Int
                while (bis.read().also { b = it } != -1) {
                    Thread.sleep(500)
                    emitter.onNext(b)
                }
            }
        }
    }

    /**
     * TODO 模拟登录的网络请求
     *
     * @param user ui 层填写的用户信息
     */
    fun login(user: User): Observable<DataResult<String>> {

        // 使用 retrofit 或任意你喜欢的库实现网络请求。此处以 retrofit 写个简单例子，
        // 并且如使用 rxjava，还可额外依赖 RxJavaCallAdapterFactory 来简化编写，具体自行网上查阅，此处不做累述，

        return AsyncTask.doIO { emitter ->
            val call = retrofit.create(AccountService::class.java).login(user.name, user.password)
            val response: Response<String>
            try {
                response = call.execute()
                val responseStatus = ResponseStatus(
                    response.code().toString(),
                    response.isSuccessful,
                    ResultSource.NETWORK
                )
                emitter.onNext(DataResult(response.body() ?: "", responseStatus))
            } catch (ex: Exception) {
                emitter.onNext(
                    DataResult(
                        "",
                        ResponseStatus(ex.message ?: "", false, ResultSource.NETWORK)
                    )
                )
            }
        }
    }

}



























package com.earthgee.puremusic.data.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 *  Created by earthgee on 2024/1/9
 *  CopyRight (c) earthgee.com
 *  功能：
 */
interface AccountService {

    @POST("xxx/login")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<String>

}
package com.earthgee.architecture.data.response

/**
 *  Created by earthgee on 2024/1/5
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class ResponseStatus(
    val responseCode: String = "",
    val success: Boolean = true,
    val source: ResultSource = ResultSource.NETWORK
)

enum class ResultSource {

    NETWORK, DATABASE, LOCAL_FILE

}
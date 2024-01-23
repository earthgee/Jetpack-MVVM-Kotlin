package com.earthgee.puremusic.data.config

import com.earthgee.puremusic.data.bean.User
import com.kunminx.architecture.data.config.keyvalue.KeyValueBoolean
import com.kunminx.architecture.data.config.keyvalue.KeyValueInteger
import com.kunminx.architecture.data.config.keyvalue.KeyValueSerializable
import com.kunminx.architecture.data.config.keyvalue.KeyValueString
import com.kunminx.keyvalue.annotation.KeyValueX

/**
 *  Created by earthgee on 2024/1/9
 *  CopyRight (c) earthgee.com
 *  功能：
 */
@KeyValueX
interface Configs {

    fun token(): KeyValueString
    fun isLogin(): KeyValueBoolean
    fun alive(): KeyValueInteger
    fun user(): KeyValueSerializable<User>

}
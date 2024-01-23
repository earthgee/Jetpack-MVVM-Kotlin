package com.earthgee.architecture.data.response

/**
 * TODO: 专用于数据层返回结果至 domain 层或 ViewModel，原因如下：
 * <p>
 * liveData 专用于页面开发、解决生命周期安全问题，
 * 有时数据并非通过 liveData 分发给页面，也可是通过其他方式通知非页面组件，
 * 这时 repo 方法中内定通过 liveData 分发便不合适，不如一开始就规定不在数据层通过 liveData 返回结果。
 * <p>
 * 如这么说无体会，详见《这是一份 “架构模式” 自驾攻略》解析
 * https://xiaozhuanlan.com/topic/8204519736
 * <p>
 * Created by earthgee on 2024/1/2
 * CopyRight (c) earthgee.com
 */
class DataResult<T>(val mEntity: T, val mResponseStatus: ResponseStatus = ResponseStatus())
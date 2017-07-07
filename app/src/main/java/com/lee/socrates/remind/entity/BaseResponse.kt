package com.lee.socrates.remind.entity

/**
 * Created by lee on 2017/6/29.
 */
data class BaseResponse<E>(var isSuccess: Boolean, var status: String, var message: String, var data: List<E>? = null)
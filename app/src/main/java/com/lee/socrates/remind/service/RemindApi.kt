package com.lee.socrates.remind.service

import com.lee.socrates.remind.entity.AccountRecord
import com.lee.socrates.remind.entity.BaseResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by lee on 2017/6/29.
 */
interface RemindApi {

    @POST("register")
    fun register(@Body bodyMap: Map<String, @JvmSuppressWildcards Any?>): Observable<BaseResponse<Nothing>>

    @POST("login")
    fun login(@Body bodyMap: Map<String, @JvmSuppressWildcards Any?>): Observable<BaseResponse<Nothing>>

    @POST("addAccountRecord")
    fun addAccount(@Body accountRecord: AccountRecord): Observable<BaseResponse<Nothing>>

    @GET("accountRecordList")
    fun getAccountList(): Observable<BaseResponse<AccountRecord>>

    @Multipart
    @POST("UploadRecognized?uid=0cebde2135134f36&type=1")
    fun uploadImage(@Part("file\"; filename=\"uploadfile.png\"") file: RequestBody): Observable<BaseResponse<Nothing>>

    @POST("upload/image")
    fun updateHeadIcon(@Body bytes: ByteArray): Observable<BaseResponse<Nothing>>

    @GET("getJson/{fileName}")
    fun download(@Path("fileName") fileName: String): Observable<ResponseBody>
}
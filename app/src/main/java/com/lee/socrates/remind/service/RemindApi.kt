package com.lee.socrates.remind.service

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
    fun register(@Body bodyMap: Map<String, @JvmSuppressWildcards Any?>): Observable<BaseResponse>

    @POST("login")
    fun login(@Body bodyMap: Map<String, @JvmSuppressWildcards Any?>): Observable<BaseResponse>

    @Multipart
    @POST("UploadRecognized?uid=0cebde2135134f36&type=1")
    fun uploadImage(@Part("file\"; filename=\"uploadfile.png\"") file: RequestBody): Observable<BaseResponse>

    @POST("upload/image")
    fun updateHeadIcon(@Body bytes: ByteArray): Observable<BaseResponse>

    @Multipart
    @POST("UploadRecognized?uid=0cebde2135134f36&type=1")
    fun uploadTestImage(@Body bytes: ByteArray): Observable<BaseResponse>

    @GET("getJson/{fileName}")
    fun download(@Path("fileName") fileName: String): Observable<ResponseBody>
}
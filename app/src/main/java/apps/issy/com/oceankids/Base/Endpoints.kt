package apps.issy.com.oceankids.Base

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface Endpoints{

    @POST("/api/v4/message/send")
    fun sendSms(@Header("Authorization") authorization : String, @Body r : RequestBody) : Call<Any>

}
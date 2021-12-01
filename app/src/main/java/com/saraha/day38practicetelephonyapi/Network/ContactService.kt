package com.saraha.day38practicetelephonyapi.Network

import com.saraha.day38practicetelephonyapi.Model.Contact
import retrofit2.Response
import retrofit2.http.*

interface ContactService {

    @GET("contacts")
    suspend fun getContactByNumber(@Query("phone") phone: String): Response<List<Contact>>

    @POST("contacts")
    suspend fun postContacts(@Body contactList: List<Contact>): Response<List<Contact>>

}
package com.saraha.day38practicetelephonyapi.Repository

import com.saraha.day38practicetelephonyapi.Model.Contact
import com.saraha.day38practicetelephonyapi.Network.Api

class ContactRepository {

    private val retrofitService = Api.getInstance()

    suspend fun getContactByNumber(phone: String) = retrofitService.getContactByNumber(phone)

    suspend fun postContacts(contacts: List<Contact>) = retrofitService.postContacts(contacts)

}
package com.saraha.day38practicetelephonyapi.View.Main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saraha.day38practicetelephonyapi.Model.Contact
import com.saraha.day38practicetelephonyapi.Repository.ContactRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {

    val contactLiveData = MutableLiveData<List<Contact>?>()
    val boolLiveData = MutableLiveData<Boolean?>()

//    fun getContactByNumber(phone: String){
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = ContactRepository().getContactByNumber(phone)
//            withContext(Dispatchers.Main){
//                if (!response.isSuccessful) {
//                    contactLiveData.value = null
//                    return@withContext
//                }
//
//                response.body()?.let { contactInfo ->
//                    val contact = if (contactInfo.isNotEmpty()) contactInfo else null
//                    contactLiveData.value = contact.takeIf { contact != null }
//                }
//            }
//        }
//    }

    fun postContacts(contacts: List<Contact>){
        CoroutineScope(Dispatchers.IO).launch {
                    val response = ContactRepository().postContacts(contacts)
                    withContext(Dispatchers.Main){
                        if (!response.isSuccessful) {
                            boolLiveData.value = false
                            return@withContext
                        }

                        response.body()?.let { it ->
                            val isSuccessful = if (it.isNotEmpty()) true else false
                            boolLiveData.value = isSuccessful
                        }
                    }
                }
    }

}
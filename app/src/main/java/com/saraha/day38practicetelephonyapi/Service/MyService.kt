package com.saraha.day38practicetelephonyapi.Service

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.saraha.day38practicetelephonyapi.Model.Contact
import com.saraha.day38practicetelephonyapi.Repository.ContactRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyService : Service() {

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val phone = intent?.getStringExtra("phone")

        if (phone != null) {
            checkPhoneNumber(phone)
        }
        return START_STICKY
    }

    private fun checkPhoneNumber(phone: String){
        CoroutineScope(Dispatchers.IO).launch {
                    val response = ContactRepository().getContactByNumber(phone)
                    withContext(Dispatchers.Main){
                        var result: Contact?
                        val serviceIntent = Intent("phone_number_result")
                        if (!response.isSuccessful) {
                            result = null
                            return@withContext
                        }

                        response.body()?.let { it ->
                            var receivedCaller = phone.takeLast(9)
                            val list = it.find { it.phone.takeLast(9).equals(receivedCaller)}
                            result = list.takeIf { list != null }
                            serviceIntent.putExtra("phone", result)
                        }

                        sendBroadcast(serviceIntent)

                    }
                }

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
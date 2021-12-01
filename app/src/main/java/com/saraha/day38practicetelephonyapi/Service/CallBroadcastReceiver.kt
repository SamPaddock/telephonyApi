package com.saraha.day38practicetelephonyapi.Service

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import android.telephony.PhoneStateListener

class CallBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        var callState = intent?.extras?.get(TelephonyManager.EXTRA_STATE)
        if (callState == "RINGING"){
            val telephony = context!!
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephony.listen(object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, incomingNumber: String) {
                    super.onCallStateChanged(state, incomingNumber)
                    println("incomingNumber : $incomingNumber")
                    val serviceIntent = Intent(context, MyService::class.java)
                    serviceIntent.putExtra("phone", incomingNumber)
                    context.startService(serviceIntent)
                }
            }, PhoneStateListener.LISTEN_CALL_STATE)

        }
    }

}
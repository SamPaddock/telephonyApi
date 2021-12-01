package com.saraha.day38practicetelephonyapi.View.Main

import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.saraha.day38practicetelephonyapi.databinding.ActivityMainBinding
import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.widget.Toast
import com.saraha.day38practicetelephonyapi.Model.Contact
import com.saraha.day38practicetelephonyapi.Model.ContactsInfo
import com.saraha.day38practicetelephonyapi.Service.CallBroadcastReceiver

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var contactArray: ArrayList<ContactsInfo>

    var callBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val phone = intent?.getSerializableExtra("phone") as Contact
            Toast.makeText(this@MainActivity, phone.fullname, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        checkPhonePermission()
        binding.buttonSyncContact.setOnClickListener {
            checkContactPermission()
        }

        setContentView(binding.root)
    }

    override fun onStart() {
        registerReceiver(callBroadcastReceiver, IntentFilter("phone_number_result"))
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(callBroadcastReceiver)
        super.onStop()
    }

    @SuppressLint("Range")
    private fun onSyncContacts() {
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor?.getCount()!! > 0) {
            while (cursor.moveToNext()) {
                val hasPhoneNumber = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt()
                if (hasPhoneNumber > 0) {

                    val contactId = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val displayName = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    val phoneCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf<String?>(contactId),
                        null
                    )

                    if (phoneCursor?.moveToNext() == true) {
                        val phoneNumber = phoneCursor?.getString(
                            phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        contactArray.add(ContactsInfo(contactId, displayName, phoneNumber))
                    }
                    phoneCursor?.close()
                }
            }
        }
        cursor.close()
    }

    private fun checkPhonePermission(){
        val callStatePermission = applicationContext.checkSelfPermission(
            permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        val readCallPermission = applicationContext.checkSelfPermission(
            permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
        val readNumberPermission = applicationContext.checkSelfPermission(
            permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED
        if (!callStatePermission && !readCallPermission && !readNumberPermission){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.READ_PHONE_STATE,permission.READ_CALL_LOG, permission.READ_PHONE_NUMBERS),
                7)
        }
    }

    private fun checkContactPermission(){
        val readContactPermission = applicationContext.checkSelfPermission(
            permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        val writeContactPermission = applicationContext.checkSelfPermission(
            permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED

        if (readContactPermission && writeContactPermission){
            onSyncContacts()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.READ_CONTACTS,permission.WRITE_CONTACTS),
             6)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 6){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onSyncContacts()
            } else {
                Toast.makeText(
                    this,
                    "Permission must be granted in order to sync contacts information",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
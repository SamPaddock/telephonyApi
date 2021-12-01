package com.saraha.day38practicetelephonyapi.Model

import java.io.Serializable

data class Contact(
    val fullname: String,
    val id: String,
    val phone: String
): Serializable
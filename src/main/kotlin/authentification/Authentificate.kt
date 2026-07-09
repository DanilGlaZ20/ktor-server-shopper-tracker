package com.project.authentification

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


private val hashKey = (System.getenv("HASH_KEY") ?: "default_local_hash_key").toByteArray()
private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")
fun hash(password: String): String {
    val hmac: Mac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)

    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}
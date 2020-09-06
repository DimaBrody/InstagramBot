package com.bot.insta.tools.hash

import com.bot.insta.data.variables.AppValues.API_KEY
import com.bot.insta.data.variables.AppValues.API_KEY_VERSION
import org.apache.commons.codec.binary.Hex
import java.net.URLEncoder
import java.nio.charset.Charset
import java.security.Key
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

const val XLATE = "0123456789abcdef"

fun digest(codec: String, source: String): String {
    return try {
        val digest: MessageDigest = MessageDigest.getInstance(codec)
        val digestBytes: ByteArray = digest.digest(source.toByteArray(Charset.forName("UTF-8")))
        hexlate(digestBytes, digestBytes.size)
    } catch (nsae: NoSuchAlgorithmException) {
        throw RuntimeException("$codec codec not available")
    }
}

fun md5hex(source: String): String {
    return digest("MD5", source)
}

fun hexlate(bytes: ByteArray?, initialCount: Int): String {
    if (bytes == null) {
        return ""
    }
    val count = initialCount.coerceAtMost(bytes.size)
    val chars = CharArray(count * 2)
    for (i in 0 until count) {
        var `val` = bytes[i].toInt()
        if (`val` < 0) {
            `val` += 256
        }
        chars[2 * i] = XLATE[`val` / 16]
        chars[2 * i + 1] = XLATE[`val` % 16]
    }
    return String(chars)
}

fun generateDeviceId(username: String, password: String): String {
    val seed = md5hex(username + password)
    val volatileSeed = "12345"
    return "android-" + md5hex(seed + volatileSeed).substring(0, 16)
}

fun generateHash(key: String, string: String?): String? {
    val `object` = SecretKeySpec(key.toByteArray(), "HmacSHA256")
    try {
        val mac: Mac = Mac.getInstance("HmacSHA256")
        mac.init(`object` as Key)
        val byteArray: ByteArray = mac.doFinal(string?.toByteArray(charset("UTF-8")))
        return String(Hex().encode(byteArray), Charset.forName("ISO-8859-1"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun generateSignature(payload: String?): String {
    val parsedData: String = URLEncoder.encode(payload, "UTF-8")
    val signedBody = generateHash(API_KEY, payload)
    return "ig_sig_key_version=$API_KEY_VERSION&signed_body=$signedBody" + '.'
        .toString() + parsedData
}
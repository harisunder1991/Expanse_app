package com.expanse.autopilot.data.local

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class DatabaseKeyManager(private val context: Context) {

    companion object {
        private const val KEY_ALIAS = "ExpanseSqlcipherKeyAlias"
        private const val PREFS_NAME = "ExpansePrefsSecure"
        private const val DB_KEY_PREF = "encrypted_db_key"
        private const val DB_IV_PREF = "encrypted_db_iv"
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val AES_MODE = "AES/GCM/NoPadding"
    }

    fun getOrCreatePassphrase(): ByteArray {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val encryptedKeyBase64 = prefs.getString(DB_KEY_PREF, null)
        val ivBase64 = prefs.getString(DB_IV_PREF, null)

        return if (encryptedKeyBase64 != null && ivBase64 != null) {
            // Decrypt key using Android KeyStore key
            try {
                val encryptedKey = Base64.decode(encryptedKeyBase64, Base64.DEFAULT)
                val iv = Base64.decode(ivBase64, Base64.DEFAULT)
                decryptKey(encryptedKey, iv)
            } catch (e: Exception) {
                // Key got corrupted or lost, generate a new one
                val newKey = generateRandomKey()
                encryptAndSaveKey(newKey)
                newKey
            }
        } else {
            // First time setup, generate high entropy random key
            val newKey = generateRandomKey()
            encryptAndSaveKey(newKey)
            newKey
        }
    }

    private fun generateRandomKey(): ByteArray {
        val secureRandom = SecureRandom()
        val key = ByteArray(32) // 256 bits
        secureRandom.nextBytes(key)
        return key
    }

    private fun getKeystoreSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply { load(null) }
        val secretKey = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        if (secretKey != null) return secretKey

        // Generate a new key in Android Keystore
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER)
        val keyGenSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(keyGenSpec)
        return keyGenerator.generateKey()
    }

    private fun encryptAndSaveKey(key: ByteArray) {
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, getKeystoreSecretKey())
        val encryptedKey = cipher.doFinal(key)
        val iv = cipher.iv

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(DB_KEY_PREF, Base64.encodeToString(encryptedKey, Base64.DEFAULT))
            .putString(DB_IV_PREF, Base64.encodeToString(iv, Base64.DEFAULT))
            .apply()
    }

    private fun decryptKey(encryptedKey: ByteArray, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getKeystoreSecretKey(), spec)
        return cipher.doFinal(encryptedKey)
    }
}

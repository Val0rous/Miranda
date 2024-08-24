package com.cashflowtracker.miranda.data.repositories

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object LoginRepository {
    private const val KEY_ALIAS = "email_encryption_key"
    private const val PREFS_FILE_NAME = "secure_email_prefs"
    private const val EMAIL_KEY = "encrypted_email"
    private const val USER_ID_KEY = "encrypted_user_id"

    private fun generateEncryptionKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    private fun encryptString(string: String, key: SecretKey): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)

        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(string.toByteArray())

        return iv + encryptedBytes
    }

    private fun decryptString(encryptedData: ByteArray, key: SecretKey): String {
        val iv = encryptedData.copyOfRange(0, 12)
        val encryptedBytes = encryptedData.copyOfRange(12, encryptedData.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)

        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

    /** Saves logged user email in shared preferences
     * @param email email of logged user
     */
    fun Context.saveLoggedUserEmail(email: String) {
        val key = generateEncryptionKey()
        val encryptedEmail = encryptString(email, key)

        // Using Base64 to store the byte array in SharedPreferences
        val encryptedEmailBase64 = Base64.encodeToString(encryptedEmail, Base64.DEFAULT)
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(EMAIL_KEY, encryptedEmailBase64).apply()
    }

    /** Use this function for login */
    fun Context.getLoggedUserEmailOrNull(): String? {
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val encryptedEmailBase64 = sharedPrefs.getString(EMAIL_KEY, null) ?: return null

        val encryptedEmail = Base64.decode(encryptedEmailBase64, Base64.DEFAULT)
        val key = generateEncryptionKey()
        return decryptString(encryptedEmail, key)
    }

    /** Use this function when you're sure that user is logged in */
    fun Context.getCurrentUserEmail(): String {
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val encryptedEmailBase64 = sharedPrefs.getString(EMAIL_KEY, null)

        val encryptedEmail = Base64.decode(encryptedEmailBase64, Base64.DEFAULT)
        val key = generateEncryptionKey()
        return decryptString(encryptedEmail, key)
    }

    fun Context.clearLoggedUserEmail() {
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().remove(EMAIL_KEY).apply()
    }

    fun Context.saveLoggedUserId(userId: UUID) {
        val key = generateEncryptionKey()
        val encryptedUserId = encryptString(userId.toString(), key)

        // Using Base64 to store the byte array in SharedPreferences
        val encryptedUserIdBase64 = Base64.encodeToString(encryptedUserId, Base64.DEFAULT)
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(USER_ID_KEY, encryptedUserIdBase64).apply()
    }

    /** Use this function for login, when you're not sure user id is saved in SharedPreferences */
    fun Context.getLoggedUserIdOrNull(): UUID? {
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val encryptedUserIdBase64 = sharedPrefs.getString(USER_ID_KEY, null) ?: return null

        val encryptedUserId = Base64.decode(encryptedUserIdBase64, Base64.DEFAULT)
        val key = generateEncryptionKey()
        return UUID.fromString(decryptString(encryptedUserId, key))
    }

    /** Use this function when you're sure that user is logged in */
    fun Context.getCurrentUserId(): UUID {
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val encryptedUserIdBase64 = sharedPrefs.getString(USER_ID_KEY, null)

        val encryptedUserId = Base64.decode(encryptedUserIdBase64, Base64.DEFAULT)
        val key = generateEncryptionKey()
        return UUID.fromString(decryptString(encryptedUserId, key))
    }

    fun Context.clearLoggedUserId() {
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().remove(USER_ID_KEY).apply()
    }

//    private const val LOGGED_USER_EMAIL_KEY = "logged_user_email"
//    //private val LOGGED_USER_PROFILE_PICTURE_KEY = "logged_user_profile_picture" // TODO
//
//    /** Saves logged user email in shared preferences
//     * @param email email of logged user
//     */
//    fun Context.saveLoggedUserEmail(email: String) {
//        val sharedPrefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
//        val editor = sharedPrefs.edit()
//        editor.putString(LOGGED_USER_EMAIL_KEY, email)
//        editor.apply()
//    }
//
//    fun Context.clearLoggedUserEmail() {
//        val sharedPrefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
//        val editor = sharedPrefs.edit()
//        editor.remove(LOGGED_USER_EMAIL_KEY)
//        editor.apply()
//    }
//
//    fun Context.getLoggedUserEmail(): String? {
//        val sharedPrefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
//        return sharedPrefs.getString(LOGGED_USER_EMAIL_KEY, null)
//    }

//    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")
//    private val LOGGED_USER_EMAIL_KEY = stringPreferencesKey("logged_user_email")
//    //private val LOGGED_USER_PROFILE_PICTURE_KEY = stringPreferencesKey("logged_user_profile_picture") // TODO
//
//    suspend fun Context.saveLoggedUserEmail(email: String) {
//        dataStore.edit { preferences ->
//            preferences[LOGGED_USER_EMAIL_KEY] = email
//        }
//    }
//
//    suspend fun Context.clearLoggedUserEmail() {
//        dataStore.edit { preferences ->
//            preferences.remove(LOGGED_USER_EMAIL_KEY)
//        }
//    }
//
//    /** Gets logged user email from dataStore
//     *  @return email if user is logged in, null otherwise
//     */
//    fun Context.getLoggedUserEmail(): Flow<String?> {
//        return dataStore.data.map { preferences ->
//            preferences[LOGGED_USER_EMAIL_KEY]
//        }
//    }
}
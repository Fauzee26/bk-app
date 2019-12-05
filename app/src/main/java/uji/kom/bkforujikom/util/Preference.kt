package uji.kom.bkforujikom.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class Preference(val context: Context) {
    private val FULLNAME = "FULLNAME"
    private val EMAIL = "EMAIL"
    private val UID = "UID"

    protected fun getSharedPreferences(key: String): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun getString(key: String, def: String?): String {
        val prefs = getSharedPreferences(key)
        return prefs.getString(key, def).toString()
    }

    private fun setString(key: String, `val`: String) {
        val prefs = getSharedPreferences(key)
        val e = prefs.edit()
        e.putString(key, `val`)
        e.apply()
    }

    private fun getBoolean(key: String, def: Boolean): Boolean {
        val prefs = getSharedPreferences(key)
        return prefs.getBoolean(key, def)
    }

    private fun setBoolean(key: String, `val`: Boolean) {
        val prefs = getSharedPreferences(key)
        val e = prefs.edit()
        e.putBoolean(key, `val`)
        e.apply()
    }

    private fun getInt(key: String, def: Int): Int {
        val prefs = getSharedPreferences(key)
        return Integer.parseInt(prefs.getString(key, def.toString())!!)
    }

    private fun setInt(key: String, `val`: Int) {
        val prefs = getSharedPreferences(key)
        val e = prefs.edit()
        e.putString(key, `val`.toString())
        e.apply()
    }

    private fun getFloat(key: String, def: Float): Float {
        val prefs = getSharedPreferences(key)
        return prefs.getString(key, def.toString())!!.toFloat()
    }

    private fun setFloat(key: String, `val`: Float) {
        val prefs = getSharedPreferences(key)
        val e = prefs.edit()
        e.putString(key, `val`.toString())
        e.apply()
    }

    private fun getLong(key: String, def: Long): Long {
        val prefs = getSharedPreferences(key)
        return java.lang.Long.parseLong(prefs.getString(key, def.toString())!!)
    }

    private fun setLong(key: String, `val`: Long) {
        val prefs = getSharedPreferences(key)
        val e = prefs.edit()
        e.putString(key, `val`.toString())
        e.apply()
    }

    fun setFullname(key: String) {
        setString(FULLNAME, key)
    }

    fun getFullname(): String {
        return getString(FULLNAME, "")
    }

    fun setUid(key: String) {
        setString(UID, key)
    }

    fun getUid(): String {
        return getString(UID, null)
    }

    fun setEmail(key: String) {
        setString(EMAIL, key)
    }

    fun getEmail(): String {
        return getString(EMAIL, null)
    }
}
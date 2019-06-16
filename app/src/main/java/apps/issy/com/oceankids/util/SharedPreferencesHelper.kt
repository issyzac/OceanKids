package apps.issy.com.oceankids.util

import android.content.Context
import android.content.SharedPreferences

/**
 * @author @issyzac
 * @Date Sunday 16th June 2019
 */

/**
 * @class A sharedPreferences helper class
 */
class SharedPreferencesHelper(val context: Context) {

    private val PREF_NAME = "ocean-kids-preferences"
    val sharedPreferences : SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * An overloaded function to save a String value to @android.content.SharedPreferences
     * @Params String, String
     */
    fun save(KEY_NAME : String, text: String){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_NAME, text)
        editor!!.commit()
    }

    /**
     * An overloaded function to save a Integer value to @android.content.SharedPreferences
     * @Params String, Int
     */
    fun save(KEY_NAME : String, value: Int){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(KEY_NAME, value)
        editor!!.commit()
    }

    /**
     * An overloaded function to save a Boolean value to @android.content.SharedPreferences
     * @Params String, Boolean
     */
    fun save(KEY_NAME : String, status: Boolean){
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(KEY_NAME, status)
    }

    /**
     * A function to get a String value from @android.content.SharedPreferences
     * @Params String : The key name to get from sharedpreferences
     * @return String : The Value in sharedPreferences
     */
    fun getValueString(KEY_NAME: String) : String?{
        return sharedPreferences.getString(KEY_NAME, null)
    }

    /**
     * A function to get an Integer value from @android.content.SharedPreferences
     * @Params String : The key name to get from sharedpreferences
     * @return Int : The Value in sharedPreferences
     */
    fun getValueInt(KEY_NAME: String) : Int? {
        return  sharedPreferences.getInt(KEY_NAME, 0)
    }

    /**
     * A function to get a String value from @android.content.SharedPreferences
     * @Params String : The key name to get from sharedpreferences
     * @return String : The Value in sharedPreferences
     */
    fun  getValueBoolean(KEY_NAME: String, defaultValue: Boolean) : Boolean?{
        return sharedPreferences.getBoolean(KEY_NAME, defaultValue)
    }

    /**
     * A function to lear everything in the sharedPreferences
     */
    fun clearSharedPreferences(){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.commit()
    }

    fun removeValue(KEY_NAME: String){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(KEY_NAME)
        editor.commit()
    }

}
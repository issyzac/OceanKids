package apps.issy.com.oceankids.util

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;


class Karibusms {

    /**
     * --------------------------------------------------------------------------
     *
     * @var final Integer karibusmspro
     * --------------------------------------------------------------------------
     * This define if you want to send SMS via INTERNET based or you want your
     * mobile phone to send SMS.
     *
     * N:B; karibuSMS MUST be installed in your android phone to enable SMS to
     * be sent from android phone
     *
     * karibusmspro = 1 , Internet SMS, karibusmspro = 0, Phone SMS
     */
    internal val karibusmspro = 1

    /**
     * --------------------------------------------------------------------------
     *
     * @var String API_KEY
     * --------------------------------------------------------------------------
     *
     * Obtained directly from http://karibusms.com/api after creating your APP
     * name
     */
    internal val API_KEY = "74478661445646499"
    /**
     * --------------------------------------------------------------------------
     *
     * @var String API_SECRET
     * --------------------------------------------------------------------------
     *
     * Obtained directly from http://karibusms.com/api after creating your APP
     * name
     */
    internal val API_SECRET = "5ed1c4c170a811ac24b52c7b444c8bed928e5967"

    /**
     * -------------------------------------------------------------------------
     *
     * @var String name;
     * -------------------------------------------------------------------------
     *
     * A name that will appear in text SMS when user receives SMS. This will
     * only be valid if you set variable karibusmspro=1
     */
    internal val name = "KARIBU"

    /**
     *
     * @return JSON Object
     * @throws Exception
     */
    val _statistics: String?
        @Throws(Exception::class)
        get() = this.curl(this.writeBufferStatistic())

    /**
     * @param phone_number
     * @param message
     * @return JSON Object
     * @throws java.lang.Exception
     */
    @Throws(Exception::class)
    fun send_sms(phone_number: String, message: String): String? {
        return this.curl(this.writeBufferSms(phone_number, message))
    }

    @Throws(Exception::class)
    private fun curl(param: String): String? {
        var returns: String? = null
        val url = URL("http://karibusms.com/api")
        val conn = url.openConnection()
        conn.setDoOutput(true)
        var reader = BufferedReader(InputStreamReader(conn.getInputStream()))
        OutputStreamWriter(conn.getOutputStream()).use { writer ->
            writer.write(param)
            writer.flush()
            reader = BufferedReader(InputStreamReader(conn.getInputStream()))
            val line = reader.readLine()
            while (line != null) {
                returns = line
            }
        }
        reader.close()
        return returns
    }

    /**
     *
     * @param phone_number
     * @param message
     * @return String
     */
    private fun writeBufferSms(phone_number: String, message: String): String {
        return "api_key=$API_KEY&karibusmspro=$karibusmspro&message=$message&phone_number=$phone_number&name=$name&api_secret=$API_SECRET"
    }

    /**
     *
     * @return String
     */
    private fun writeBufferStatistic(): String {
        return "api_key=$API_KEY&tag=get_statistics&api_secret=$API_SECRET"
    }
}
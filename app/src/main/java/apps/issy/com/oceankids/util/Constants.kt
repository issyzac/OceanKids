package apps.issy.com.oceankids.util

class Constants{
    companion object {
        val genderMale : String = "Male"
        val genderFemale : String = "Female"

        val baseURL : String = "https://smsgateway.me/"
        val apiKey : String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhZG1pbiIsImlhdCI6MTU0Nzg1MzkzMCwiZXhwIjo0MTAyNDQ0ODAwLCJ1aWQiOjY2OTU4LCJyb2xlcyI6WyJST0xFX1VTRVIiXX0.uVDluFP1gi7WIrFjki5avJXlaQ8fG5vegjdgwq9qPTE"

        //Default text values
        val KIDS_LIST_DEFAULT_MESSAGE = "Type the Child's name above to search!"

        //SharedPreferences Keys
        val CHECKING_IN = "CHEKING_IN"

    }

    class databaseConstants {

        companion object {
            val NURSERY_ROLE : Int = 1
            val PRESCHOOL_ROLE : Int = 2
            val PRIMARY_ROLE : Int = 3
        }
    }

    class  CALENDAR {
        companion object {
            val TWELVE_YEARS_AGO : Long = 378683112000
            val TEN_YEARS_AGO : Long = 315569260000
            val SIX_YEARS_AGO : Long = 189341556000
            val THREE_YEARS_AGO : Long = 94670778000
        }
    }
}
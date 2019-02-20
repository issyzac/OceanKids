package apps.issy.com.oceankids.database.typeconverters

import androidx.room.TypeConverter
import apps.issy.com.oceankids.database.entities.Kid
import com.google.gson.Gson

class AttendanceTypeConverter {

    @TypeConverter
    fun fromString(fielsString: String): Kid.Atendance {
        return Gson().fromJson<Kid.Atendance>(fielsString, Kid.Atendance::class.java)
    }

    @TypeConverter
    fun fromAtendance(atendance: Kid.Atendance): String {
        return Gson().toJson(atendance)
    }

}
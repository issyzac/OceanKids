package apps.issy.com.oceankids.database.typeconverters

import androidx.room.TypeConverter
import apps.issy.com.oceankids.database.entities.Kid
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class ParentConverter{
    @TypeConverter
    fun fromString(fielsString: String): List<Kid.Parent>? {
        val listType = object : TypeToken<ArrayList<Kid.Parent>>() {

        }.type
        return Gson().fromJson<List<Kid.Parent>>(fielsString, listType)
    }

    @TypeConverter
    fun fromParent(parentList: List<Kid.Parent>): String {
        return Gson().toJson(parentList)
    }
}
package apps.issy.com.oceankids.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import apps.issy.com.oceankids.database.typeconverters.AttendanceTypeConverter
import apps.issy.com.oceankids.database.typeconverters.ParentConverter
import com.google.firebase.database.IgnoreExtraProperties

import java.io.Serializable

/**
 *  Created by issy on 23/02/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

@IgnoreExtraProperties
@Entity(tableName = "child")
@TypeConverters(AttendanceTypeConverter::class, ParentConverter::class)
class Kid : Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id : String = ""

    var firstName : String = ""

    var lastName : String = ""

    var gender : String = ""

    var nationality : String = ""

    var dob : Long = 0

    @TypeConverters (AttendanceTypeConverter::class)
    var attendance : Atendance = Atendance()

    var aggregatePoints: Int = 0

    var parents : List<Parent> = emptyList()

    var allergies: String = ""

    var checkedIn: String = "0"

    constructor()

    class Parent {
        var id : String = ""
        var relationship : String = ""
    }

    /**
     * Check-in/Check-out
     */
    class Atendance {
        var cardNumber : String = ""
        var service : String = "0"
        var checkedIn : String = "0"
    }

}
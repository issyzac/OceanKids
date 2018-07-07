package apps.issy.com.oceankids.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import java.io.Serializable

/**
 * Created by issy on 21/02/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project OceanKids
 */

@Entity
class Kid (
        @ColumnInfo(name = "child_uuid") var childUUID: String,
        @ColumnInfo(name = "first_name") var firstName : String,
        @ColumnInfo(name = "last_name") var lastName :String,
        @ColumnInfo(name = "date_of_birth") var dateOfBirth : Long = 0,
        /*
        0 - Nursery
        1 - Preschool
        2 - Primary
        3 - Preteen
        4 - Youth
        */
        @ColumnInfo(name = "grade_level") var gradeLevel: Int = 0,
        @ColumnInfo(name = "parent_phone_number") var parentPhoneNumber : String = "",
        /*
        M - Male
        F - Female
         */
        @ColumnInfo(name = "gender") var gender : Char
    )
{

    constructor() : this("","", "", 0, 0, "", 'M')

    @PrimaryKey (autoGenerate = false)
    @ColumnInfo(name = "id")
    var id : Int = 0

    @ColumnInfo(name = "middle_name")
    var middleName : String = ""

}

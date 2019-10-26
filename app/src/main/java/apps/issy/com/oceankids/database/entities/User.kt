package apps.issy.com.oceankids.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Author : Isaya Mollel on 2019-10-26.
 */

@Entity(tableName = "user")
class User: Serializable {

    @PrimaryKey
    var id : String = ""
    var email : String = ""
    var role : Int = 0

    constructor()
}
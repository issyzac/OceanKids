package apps.issy.com.oceankids.database.entities

import androidx.room.Entity
import java.io.Serializable

/**
 * Author : Isaya Mollel on 2019-10-26.
 */

@Entity(tableName = "user")
class User: Serializable {

    var id : String = ""
    var email : String = ""
    var role : Int = 0

    constructor()
}
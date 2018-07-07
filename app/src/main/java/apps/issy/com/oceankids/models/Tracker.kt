package apps.issy.com.oceankids.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import java.io.Serializable
import java.util.*

/**
 * Created by issy on 21/02/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project OceanKids
 */

@Entity(tableName = "Tracker")
data class Tracker (@ColumnInfo(name = "date") var date: Long = 0) {

    constructor() : this(0)

    @ColumnInfo(name = "id") @PrimaryKey (autoGenerate = true) var id : Long = 0
    @ColumnInfo(name = "trackerUUID") var trackerUUID : String = ""
    @ColumnInfo(name = "kidUUID") var kidUUID: String = ""
    @ColumnInfo(name = "hasBible") var hasBible : Boolean = false
    @ColumnInfo(name = "hasAttended") var hasAttended : Boolean = false
    @ColumnInfo(name = "knowsMemoryVerse") var knowsMemoryVerse : Boolean = false
    @ColumnInfo(name = "broughtOffering") var broughtOffering : Boolean = false
    @ColumnInfo(name = "broughtAFriend") var broughtAfriend : Boolean = false
}

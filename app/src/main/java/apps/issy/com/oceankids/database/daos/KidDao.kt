package apps.issy.com.oceankids.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import apps.issy.com.oceankids.database.entities.Kid

@Dao
interface KidDao {

    @Query("select * from child")
    fun getAllKids() : List<Kid>

    @Query("select * from child order by firstName")
    fun getAllKidsLiveData() : LiveData<List<Kid>>

    @Query("select * from child where dob > :dobTo and dob <= :dobFrom order by firstName")
    fun getChildrenByClass(dobFrom : Long, dobTo : Long) : LiveData<List<Kid>>

    @Query("select * from child where checkedIn = :checkedIn order by firstName")
    fun getAllCheckedInKids(checkedIn : Int) : LiveData<List<Kid>>

    @Query("select * from child where id = :id")
    fun getChildById(id : String) : LiveData<Kid>

    @Insert(onConflict = REPLACE)
    fun InsertKid(kid : Kid?)

    @Update
    fun updateChild(kid: Kid?)

    @Delete
    fun deleteAKid (kid : Kid)

}
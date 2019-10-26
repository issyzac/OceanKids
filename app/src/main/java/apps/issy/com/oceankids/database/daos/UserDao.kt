package apps.issy.com.oceankids.database.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import apps.issy.com.oceankids.database.entities.User

/**
 * Author : Isaya Mollel on 2019-10-26.
 */

@Dao
interface UserDao {

    @Query("select * from user where id = :userId")
    fun getUserById(userId : String)

    @Insert(onConflict = REPLACE)
    fun insertUser(user: User?)

    @Delete
    fun deleteUser(user: User?)

    @Update
    fun updateUser(user: User?)

}
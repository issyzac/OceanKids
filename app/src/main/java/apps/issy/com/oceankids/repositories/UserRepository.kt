package apps.issy.com.oceankids.repositories

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import apps.issy.com.oceankids.database.daos.UserDao
import apps.issy.com.oceankids.database.entities.User
import com.androidhuman.rxfirebase2.database.data
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Author : Isaya Mollel on 2019-10-26.
 */

class UserRepository(private val userDao: UserDao){

    suspend fun getCurrentUser(userId: String) : User{
        return userDao.getUserById(userId)
    }

    @SuppressLint("CheckResult")
    suspend fun loadAllUsers(uuid : String){

        val reference : DatabaseReference = FirebaseDatabase.getInstance()
                .reference
                .child("users")
                .child(uuid)

        reference.data()
                .subscribe({
                    if (it.exists()){
                        val user : User = User()
                        val userId = it.key.toString()
                        val userEmail = it.child("email").value
                        val userRole = it.child("role").value

                        user.id = userId
                        user.role = userRole.toString().toInt()
                        user.email = userEmail.toString()

                        GlobalScope.launch (IO){
                            insertUser(user)
                        }

                    }
                }){}
    }

    fun insertUser( u : User){
        userDao.insertUser(u)
    }

}
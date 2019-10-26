package apps.issy.com.oceankids.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import apps.issy.com.oceankids.Base.AppDatabase
import apps.issy.com.oceankids.database.entities.User
import apps.issy.com.oceankids.repositories.UserRepository
import kotlinx.coroutines.GlobalScope

/**
 * Author : Isaya Mollel on 2019-10-26.
 */
class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository : UserRepository

    init {
        val userDao = AppDatabase.getDatabase(application, scope = GlobalScope).userDao()
        userRepository = UserRepository(userDao)
    }

     suspend fun getCurrentUser(userID : String) : User {
        val user = userRepository.getCurrentUser(userID)
        return user
    }

    suspend fun loadAllUsers(uuid: String){
        userRepository.loadAllUsers(uuid)
    }

}
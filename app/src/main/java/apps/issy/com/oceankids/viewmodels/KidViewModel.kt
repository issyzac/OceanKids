package apps.issy.com.oceankids.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import apps.issy.com.oceankids.Base.AppDatabase
import apps.issy.com.oceankids.data.Parent
import apps.issy.com.oceankids.database.entities.Kid
import apps.issy.com.oceankids.repositories.KidsRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class KidViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext : CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val kidRepository : KidsRepository

    val allKids : LiveData<List<Kid>>
    val nurseryKids : LiveData<List<Kid>>
    val preSchoolKids : LiveData<List<Kid>>
    val primaryKids : LiveData<List<Kid>>

    val checkedInKids : LiveData<List<Kid>>

    init {
        val kidDao = AppDatabase.getDatabase(application, scope).kidDao()
        kidRepository = KidsRepository(kidDao)

        allKids = kidRepository.allKids
        nurseryKids = kidRepository.nurseryKids
        preSchoolKids = kidRepository.preSchoolKids
        primaryKids = kidRepository.primaryKids

        checkedInKids = kidRepository.checkedInKids

    }

    fun insertKid(kid : Kid) = scope.launch(Dispatchers.IO){
        kidRepository.insert(kid)
    }

    suspend fun loadAllKids() = scope.launch (Dispatchers.IO){
        kidRepository.loadAllKids()
    }

    /**
     * A suspended funtion in called within a coroutinescope to check in child in a background thread
     */
    suspend fun checkingInChild(kid: Kid) {
        kidRepository.insertAttendanceData(kid)
    }

    /**
     * A suspended function called within a coroutinescope to check out a child in the background
     */
    suspend fun checkingOutChild(kid: Kid) {
        kidRepository.checkoutChildAttendance(kid)
    }

    suspend fun updateChildDetails(kid: Kid, parent: Parent?) = scope.launch (Dispatchers.IO) {
        kidRepository.updateChildDetails(kid, parent)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}
package apps.issy.com.oceankids.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import apps.issy.com.oceankids.Base.AppDatabase
import apps.issy.com.oceankids.database.entities.Kid
import apps.issy.com.oceankids.repositories.KidsRepository
import com.androidhuman.rxfirebase2.database.ChildAddEvent
import com.androidhuman.rxfirebase2.database.ChildChangeEvent
import com.androidhuman.rxfirebase2.database.childEvents
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 *
 *  This service is responsible to observe the changes on the firebase nodes
 *  and update Room on any data changes and the service will be started upon opening of
 *  the application
 *
 */
class ServerSyncService : Service() {

    private var parentJob = Job()
    private val coroutineContext : CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    var childReference : DatabaseReference? = FirebaseDatabase.getInstance().getReference("kids").child("kids_list")

    var database : AppDatabase = AppDatabase.getDatabase(this, scope)

    val kidDao = database.kidDao()

    val kidRepository = KidsRepository(kidDao)

    override fun onCreate() {
        super.onCreate()

        childReference!!.childEvents()
                .ofType(ChildChangeEvent::class.java)
                .subscribe({ data ->
                    Log.d("child_event_changes", "Changes : "+data.dataSnapshot().value.toString())
                    val currentChild : Kid? = data.dataSnapshot().getValue<Kid>(Kid::class.java)
                    updateKid(currentChild)
                }){

                }

        childReference!!.childEvents()
                .ofType(ChildAddEvent::class.java)
                .subscribe({
                    Log.d("child_event_changes", "Child added!")
                    val newChild : Kid? = it.dataSnapshot().getValue<Kid>(Kid::class.java)
                    insertKid(newChild)
                }){

                }

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun updateKid(kid : Kid?) = scope.launch(Dispatchers.IO){
        kidRepository.updateKid(kid)
    }

    private fun insertKid(kid : Kid?) = scope.launch (Dispatchers.IO) {
        kidRepository.insertNewKid(kid)
    }

}
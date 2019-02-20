package apps.issy.com.oceankids.Base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import apps.issy.com.oceankids.database.daos.KidDao
import apps.issy.com.oceankids.database.entities.Kid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Kid::class], version = 1 )
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(
                context : Context,
                scope : CoroutineScope ) : AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){

                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                )
                        .addCallback(AppDatabaseCallback(scope))
                        .build()

                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun kidDao() : KidDao

    private class AppDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback(){
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.kidDao())
                }
            }
        }

        fun populateDatabase( kidDao : KidDao ){

            /*
            you may use this to clear the database and insert clean data whenever the database
            is being opened
            */
        }

    }

}
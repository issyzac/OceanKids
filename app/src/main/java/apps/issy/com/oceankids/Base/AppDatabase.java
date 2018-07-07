package apps.issy.com.oceankids.Base;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import apps.issy.com.oceankids.models.Kid;
import apps.issy.com.oceankids.models.Tracker;

/**
 * Created by issy on 21/02/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project OceanKids
 */

@Database(entities = {Kid.class, Tracker.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {

        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "ocean_kids_db").build();
        }
        return INSTANCE;
    }

}

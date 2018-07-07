package apps.issy.com.oceankids.Base

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by issy on 22/02/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project OceanKids
 */

open class BaseActivity : AppCompatActivity() {

     var mDatabase : DatabaseReference? = null
     var childReference : DatabaseReference? = null
     var userReference : DatabaseReference? = null
     var attendanceReference : DatabaseReference? = null

     var nurseryKidsReference : DatabaseReference? = null
     var primaryKidsReference : DatabaseReference? = null
     var preschoolKidsReference : DatabaseReference? = null
     var preteenKidsReference : DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance().reference

        childReference = FirebaseDatabase.getInstance().getReference("kids").child("kids_list")
        userReference = FirebaseDatabase.getInstance().getReference("users")
        attendanceReference = FirebaseDatabase.getInstance().getReference("attendance")

        nurseryKidsReference = FirebaseDatabase.getInstance().getReference("kids").child("kids_list").child("nursery")
        primaryKidsReference = FirebaseDatabase.getInstance().getReference("kids").child("kids_list").child("preschool")
        preschoolKidsReference = FirebaseDatabase.getInstance().getReference("kids").child("kids_list").child("preteen")
        preteenKidsReference = FirebaseDatabase.getInstance().getReference("kids").child("kids_list").child("primary")

    }

}

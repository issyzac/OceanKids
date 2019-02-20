package apps.issy.com.oceankids.Base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

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
     var parentsReference : DatabaseReference? = null

     var nurseryKidsReference : DatabaseReference? = null
     var primaryKidsReference : DatabaseReference? = null
     var preschoolKidsReference : DatabaseReference? = null
     var preteenKidsReference : DatabaseReference? = null

    companion object {

        val formatter : SimpleDateFormat =  SimpleDateFormat("dd/MM/yyy")

        var fromNurseryAge : Long =  0
        var toNurseryAge : Long = 0
        var fromPreSchoolAge : Long =  0
        var toPreSchoolAge : Long =  0

        var fromPrimaryAge : Long =  0
        var toPrimaryAge : Long =  0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance().reference

        childReference = FirebaseDatabase.getInstance().getReference("kids").child("kids_list")
        userReference = FirebaseDatabase.getInstance().getReference("users")
        attendanceReference = FirebaseDatabase.getInstance().getReference("attendance")
        parentsReference = FirebaseDatabase.getInstance().getReference("parents")

        nurseryKidsReference = FirebaseDatabase.getInstance().getReference("kids").child("kids_list").child("nursery")
        primaryKidsReference = FirebaseDatabase.getInstance().getReference("kids").child("kids_list").child("preschool")
        preschoolKidsReference = FirebaseDatabase.getInstance().getReference("kids").child("kids_list").child("preteen")
        preteenKidsReference = FirebaseDatabase.getInstance().getReference("kids").child("kids_list").child("primary")

        //Get age range for Nursery kids
        fromNurseryAge = Calendar.getInstance().timeInMillis //Inclusively
        toNurseryAge = getYearsAgo(3).timeInMillis //Exclusively


        //Get age range for Pre school kids
        fromPreSchoolAge = getYearsAgo(3).timeInMillis //Inclusively
        toPreSchoolAge = getYearsAgo(6).timeInMillis //Exclusively


        //Get age range for Primary kids
        fromPrimaryAge = getYearsAgo(6).timeInMillis //Inclusively
        toPrimaryAge = getYearsAgo(11).timeInMillis //Exclusively


        Log.d("sizex", "Nursery = "+toNurseryAge+" \nPreschool = "+fromPreSchoolAge+"-"+toPreSchoolAge+"\nPrimary = "+fromPrimaryAge+"-"+toPrimaryAge)

    }

    fun getYearsAgo(yearsAgo: Int): Calendar {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -yearsAgo)

        return calendar
    }

}

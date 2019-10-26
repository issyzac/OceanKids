package apps.issy.com.oceankids

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProviders
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.data.Attendance
import apps.issy.com.oceankids.data.User
import apps.issy.com.oceankids.services.ServerSyncService
import apps.issy.com.oceankids.viewmodels.KidViewModel
import com.androidhuman.rxfirebase2.database.dataChanges
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity() {

    private lateinit var kidViewModel : KidViewModel

    private val auth : FirebaseAuth? = FirebaseAuth.getInstance()
    private var attendanceReferenceSecondService : DatabaseReference = FirebaseDatabase.getInstance().reference
    private var attendanceReferenceThirdService : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val backgroundServiceIntent = Intent(this, ServerSyncService::class.java)
        startService(backgroundServiceIntent)

        if (auth?.currentUser == null){
            val intent  = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Wrap the viewModel in a viewModelProvider for configuration changes sake
        kidViewModel = ViewModelProviders.of(this).get(KidViewModel::class.java)

        setSupportActionBar(toolbar)

        //Configure UI options based on user roles
        val userValueListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                Log.d("TAG", "Data snapshot value : "+p0.toString())

                val user : User? = p0?.getValue<User>(User::class.java)
                var userRole : Int? = user?.role
                configureLayout(userRole)
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }

        val calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = calendar.get(Calendar.MONTH)+1
        var year = calendar.get(Calendar.YEAR)
        val todaysDateKey = day.toString()+"-"+month+"-"+year

        attendanceReferenceSecondService = FirebaseDatabase.getInstance().getReference("attendance").child(todaysDateKey).child("2")
        attendanceReferenceThirdService = FirebaseDatabase.getInstance().getReference("attendance").child(todaysDateKey).child("3")

        attendanceReferenceSecondService.dataChanges()
                .subscribe({
                    for (data in it.children){

                    }
                }){

                }

        val secondServiceAttendanceListener = object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){

                    val attendanceList : ArrayList<Attendance> = ArrayList()
                    var totalCheckedInSecondService : Int = 0
                    var totalCheckedOutSecondService : Int = 0

                    for (data in p0.children){
                        totalCheckedInSecondService++
                        try {
                            val att = data.getValue<Attendance>(Attendance::class.java)
                            val singleAttendance = att.let { it } ?: continue

                            if (singleAttendance.checkedOut == 1){
                                totalCheckedOutSecondService++
                            }

                            attendanceList.add(singleAttendance)

                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }

                    //Set the summary values
                    s_pr_in.text = totalCheckedInSecondService.toString()
                    s_pr_out.text = totalCheckedOutSecondService.toString()

                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        }

        val thirdServiceAttendanceListener = object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){

                    var totalCheckedInThirdService : Int = 0
                    var totalCheckedOutThirdService : Int = 0

                    val attendanceList : ArrayList<Attendance> = ArrayList()
                    for (data in p0.children){
                        totalCheckedInThirdService++
                        try {
                            val att = data.getValue<Attendance>(Attendance::class.java)
                            val singleAttendance = att.let { it } ?: continue

                            if (singleAttendance.checkedOut == 1){
                                totalCheckedOutThirdService++
                            }

                            attendanceList.add(singleAttendance)

                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }

                    //Set the summary values
                    t_pr_in.text = totalCheckedInThirdService.toString()
                    t_pr_out.text = totalCheckedOutThirdService.toString()

                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        }

        attendanceReferenceSecondService?.addValueEventListener(secondServiceAttendanceListener)
        attendanceReferenceThirdService?.addValueEventListener(thirdServiceAttendanceListener)

        if (auth?.currentUser != null){
            val currentUserReference : DatabaseReference? = userReference?.child(auth.currentUser!!.uid)
            currentUserReference?.addValueEventListener(userValueListener)
        }

        register_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent  = Intent(this@MainActivity, RegisterChildActivity::class.java)
                startActivity(intent)
            }
        })

        check_in_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent  = Intent(this@MainActivity, KidsListActivity::class.java)
                startActivity(intent)
            }
        })

        //Classes Actions
        /*nursery_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent  = Intent(this@MainActivity, NurseryKidsActivity::class.java)
                startActivity(intent)
            }
        })

        preschool_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent  = Intent(this@MainActivity, PreSchoolActivity::class.java)
                startActivity(intent)
            }
        })

        primary_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent  = Intent(this@MainActivity, PrimaryKidsActivity::class.java)
                startActivity(intent)
            }
        }) */

    }

    private fun configureLayout(userRole : Int?) {

        when (userRole){
            0 -> {
                //TODO
            }
            1 -> {
                preschool_card.isEnabled = false
                preschool_card.setCardBackgroundColor(resources.getColor(R.color.card_separator))
                pre_school_class_title.setTextColor(resources.getColor(R.color.card_grid_tex))
                primary_card.isEnabled = false
                primary_card.setCardBackgroundColor(resources.getColor(R.color.card_separator))
                primary_class_title.setTextColor(resources.getColor(R.color.card_grid_tex))
            }
            2 -> {
                nursery_card.isEnabled = false
                nursery_card.setCardBackgroundColor(resources.getColor(R.color.card_separator))
                nursery_class_title.setTextColor(resources.getColor(R.color.card_grid_tex))
                primary_card.isEnabled = false
                primary_card.setCardBackgroundColor(resources.getColor(R.color.card_separator))
                primary_class_title.setTextColor(resources.getColor(R.color.card_grid_tex))
            }
            3 -> {
                nursery_card.isEnabled = false
                nursery_card.setCardBackgroundColor(resources.getColor(R.color.card_separator))
                nursery_class_title.setTextColor(resources.getColor(R.color.card_grid_tex))
                preschool_card.isEnabled = false
                preschool_card.setCardBackgroundColor(resources.getColor(R.color.card_separator))
                pre_school_class_title.setTextColor(resources.getColor(R.color.card_grid_tex))
            }
            4 -> {
                primary_card.isEnabled = false
                nursery_card.isEnabled = false
                preschool_card.isEnabled = false
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.logout -> {
                auth?.signOut()
                val intent  = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}

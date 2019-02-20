package apps.issy.com.oceankids

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.data.Child
import apps.issy.com.oceankids.data.Parent
import apps.issy.com.oceankids.util.Constants.Companion.genderFemale
import apps.issy.com.oceankids.util.Constants.Companion.genderMale
import com.afollestad.materialdialogs.MaterialDialog
import com.github.angads25.toggle.widget.LabeledSwitch
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.mcxiaoke.koi.ext.inflate
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog

/**
 *  Created by issy on 04/03/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

import kotlinx.android.synthetic.main.activity_register_child.*
import kotlinx.android.synthetic.main.individual_child_register.*
import kotlinx.android.synthetic.main.kid_list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.contentView
import java.util.*
import kotlin.collections.ArrayList

class RegisterChildActivity : BaseActivity(){

    var dateOfBirth : Long = 0
    val levelList : ArrayList<String> = ArrayList()

    var datePickerDialog = DatePickerDialog()
    var dobCalendar : Calendar = Calendar.getInstance()

    var children : ArrayList<Child> = ArrayList()

    var allFirstNames : ArrayList<EditText> = ArrayList()
    var allLastNames : ArrayList<EditText> = ArrayList()
    var allDateOfBirths : ArrayList<EditText> = ArrayList()
    var allNationalities : ArrayList<EditText> = ArrayList()
    var allGenders : ArrayList<LabeledSwitch> = ArrayList()
    var allBirthDatesInLong : ArrayList<Long> = ArrayList()
    var allAllergies : ArrayList<EditText> = ArrayList()

    var parentFirstName : String = ""
    var parentLastName : String = ""
    var parentPhoneNumber : String = ""
    var parentEmail : String = ""
    var parentRelationship : String = ""
    var parentAddress : String = ""

    var childrenCount : Int = 0

    val firebaseData : DatabaseReference = FirebaseDatabase.getInstance().reference

    var cm: ConnectivityManager? = null
    var activeNetwork: NetworkInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_child)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        levelList.add("Pre-School")
        levelList.add("Primary")
        levelList.add("Pre-teen")
        levelList.add("Youth")


        date_of_birth.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                datePickerDialog.show(supportFragmentManager, "dateOfBirth")
                datePickerDialog.setOnDateSetListener(object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {

                        Log.d("saving", "Date Selected clicked "+(if (dayOfMonth < 10) "0" + dayOfMonth else dayOfMonth).toString() + "-" + (if (monthOfYear + 1 < 10) "0" + (monthOfYear + 1) else monthOfYear + 1) + "-" + year)
                        dobCalendar.set(year, monthOfYear, dayOfMonth)
                        dateOfBirth = dobCalendar.timeInMillis
                        val dayString : String = dayOfMonth.toString()
                        val monthString : String = monthOfYear.toString()
                        val yearString : String = year.toString()
                        date_of_birth.setText(dayString+"-"+monthString+"-"+yearString)

                    }

                })
            }
        })

        register_button_container.setOnClickListener( object : View.OnClickListener{
            override fun onClick(p0: View?) {

                if (getNetworkStatus()){
                    Log.d("saving", "Saving child clicked")
                    progress_view.visibility = View.VISIBLE
                    register_text.visibility = View.GONE
                    if(getInputs()){
                        pushChildren(pushParent(), children) //pass @ParentKey to pushChildren
                    }
                }else{
                    val alertSaved = AlertView("Error, \nPlease check your network and try again", "", AlertStyle.DIALOG)
                    alertSaved.addAction(AlertAction("Cancel", AlertActionStyle.DEFAULT, { action ->  }))
//                    alertSaved.show(this@RegisterChildActivity)
                }
                //saveChild()
            }
        })

        add_child_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                childrenCount++
                addNewChildView()
            }
        })

    }

    fun addDatePickerListeners(v : EditText) {
        v.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                datePickerDialog.show(supportFragmentManager, "dateOfBirth")
                datePickerDialog.setOnDateSetListener(object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                        var dateInLong: Long
                        dobCalendar.set(year, monthOfYear, dayOfMonth)
                        dateInLong = dobCalendar.timeInMillis
                        allBirthDatesInLong.add(dateInLong)

                        val dayString: String = dayOfMonth.toString()
                        val monthString: String = (monthOfYear+1).toString()
                        val yearString: String = year.toString()
                        v.setText(dayString + "-" + monthString + "-" + yearString)
                    }
                })
            }
        })
    }

    fun addNewChildView() {

        var childView : View = LayoutInflater.from(this).inflate(R.layout.individual_child_register, root_layout, false)

        var childNumber : TextView = childView.findViewById(R.id.child_number)
        childNumber.setText("Child "+childrenCount)

        var firstName : EditText = childView.findViewById(R.id.first_name)
        var lastName : EditText = childView.findViewById(R.id.last_name)

        var dateOfBirth : EditText = childView.findViewById(R.id.date_of_birth)
        dateOfBirth.isFocusableInTouchMode = false
        addDatePickerListeners(dateOfBirth)

        var nationality : EditText = childView.findViewById(R.id.nationality)
        var genderSwitch : LabeledSwitch = childView.findViewById(R.id.gender)
        var allergies : EditText = childView.findViewById(R.id.allergies)

        allFirstNames.add(firstName)
        allLastNames.add(lastName)
        allDateOfBirths.add(dateOfBirth)
        allNationalities.add(nationality)
        allGenders.add(genderSwitch)
        allAllergies.add(allergies)

        children_table.addView(childView)
    }

    fun getInputs() : Boolean {

        var i = 0
        while (i < childrenCount){
            var childFirstName = allFirstNames.get(i).text.toString()
            var childLastName = allLastNames.get(i).text.toString()
            var dobValue : Long = 0
            if (allBirthDatesInLong.size > 0){
                dobValue = allBirthDatesInLong.get(i)
            }
            var nationality = allNationalities.get(i).text.toString()
            var childAllergies = allAllergies.get(i).text.toString()

            var childGenderValue : String
            if (allGenders.get(i).isOn){
                childGenderValue = genderMale
            }else{
                childGenderValue = genderFemale
            }

            //Create a child object
            var newChild : Child = Child()
            newChild.gender = childGenderValue
            newChild.firstName = childFirstName
            newChild.lastName = childLastName
            newChild.nationality = nationality
            newChild.dob = dobValue
            newChild.allergies = childAllergies

            val key = firebaseData.child("kids").child("kids_list").push().key
            newChild.id = key+""
            children.add(newChild)
            i++
        }

        //Get parents information
        parentFirstName = parent_first_name.text.toString()
        parentLastName = parent_last_name.text.toString()
        parentRelationship = parent_relationship.text.toString()
        parentPhoneNumber = parent_phone_number.text.toString()
        parentEmail = parent_email.text.toString()
        parentAddress = "" //TODO create parent address field in UI

        return true

    }

    fun pushParent() : List<Child.Parent> {

        var parents : ArrayList<Child.Parent> = ArrayList()

        val parent  = Parent()
        parent.firstName = parentFirstName
        parent.lastName = parentLastName
        parent.email = parentEmail
        parent.phoneNumber = parentPhoneNumber
        parent.relationshipToChild = parentRelationship
        val _parentKey = firebaseData.child("parents").push().key
        parent.id = _parentKey+""
        firebaseData.child("parents").child(_parentKey+"").setValue(parent)

        var par = Child.Parent()
        par.id = _parentKey+""
        par.relationship = parentRelationship
        parents.add(par)

        //Returning the parent firebase Key
        return parents
    }

    fun  pushChildren(parents : List<Child.Parent>, children : List<Child>){

        //All children are being stored here
        children.forEach {
            val parent : Child.Parent = parents.get(0)
            it.parents.add(parent)
            firebaseData.child("kids").child("kids_list").child(it.id).setValue(it)
        }

        MaterialDialog(this).show {
            title(text = "Child Registered")
            message(text = "Successfully Registered a child")
            positiveButton(text = "OK") { dialog ->

                //Checking Out Child
                //progress_view.visibility = View.GONE
                //register_text.visibility = View.VISIBLE

                finish()

            }
        }
    }

    fun clearFields(){
        first_name.setText("")
        last_name.setText("")
        parent_phone_number.setText("")
        nationality.setText("")

        finish()
    }

    fun getNetworkStatus(): Boolean {
        cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        activeNetwork = cm!!.getActiveNetworkInfo()
        return activeNetwork != null && activeNetwork!!.isConnectedOrConnecting()

    }

    fun ViewGroup.inflate (@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

}
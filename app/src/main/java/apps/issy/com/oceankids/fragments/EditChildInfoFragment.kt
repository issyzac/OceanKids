package apps.issy.com.oceankids.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.data.Parent
import apps.issy.com.oceankids.database.entities.Kid
import apps.issy.com.oceankids.util.Constants.Companion.genderFemale
import apps.issy.com.oceankids.util.Constants.Companion.genderMale
import apps.issy.com.oceankids.viewmodels.KidViewModel
import com.androidhuman.rxfirebase2.database.data
import com.google.firebase.database.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_edit_child_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class EditChildInfoFragment : DialogFragment() {

    private lateinit var kidViewModel: KidViewModel

    val datePickerDialog = DatePickerDialog()
    var dobCalendar : Calendar = Calendar.getInstance()

    var currentChild : Kid = Kid()
    var currentParent : Parent? = Parent()

    companion object {
        fun newInstance(kid : Kid) : EditChildInfoFragment {
            val arguments : Bundle? = Bundle()
            val editChildInfoFragmentInstance = EditChildInfoFragment()
            arguments?.putSerializable("kid", kid)
            editChildInfoFragmentInstance.arguments = arguments
            return editChildInfoFragmentInstance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentChild = arguments!!.getSerializable("kid") as Kid
        kidViewModel = ViewModelProviders.of(this).get(KidViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_edit_child_info, container, false)
        this.dialog.requestWindowFeature(STYLE_NO_TITLE)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayChildInformation(currentChild)

        date_of_birth.isFocusableInTouchMode = false

        date_of_birth.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                //Date of birth has been clicked to be changed
                datePickerDialog.show(activity!!.supportFragmentManager, "dateOfBirth")
                datePickerDialog.setOnDateSetListener(object : DatePickerDialog.OnDateSetListener{
                    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                        dobCalendar.set(year, monthOfYear, dayOfMonth)

                        val selectedDateInLong = dobCalendar.timeInMillis
                        currentChild.dob = selectedDateInLong

                        date_of_birth.setText(BaseActivity.formatter.format(dobCalendar.time))

                    }
                })
            }
        })

        register_button_container.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

                update_text.visibility = View.GONE
                filter_indicator.visibility = View.VISIBLE

                getEditedFields()

                GlobalScope.launch(Dispatchers.IO) {
                    saveUpdatedInformation()
                }

                update_text.visibility = View.VISIBLE
                filter_indicator.visibility = View.GONE

                dismiss()
            }
        })

        cancel_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                dismiss()
            }
        })
    }

    fun getEditedFields(){

        currentChild.firstName = first_name.text.toString()
        currentChild.lastName = last_name.text.toString()
        currentChild.nationality = nationality.text.toString()
        currentChild.allergies = allergies.text.toString()

        if (gender.isOn){
            //Gender Male
            currentChild.gender = genderMale
        }else{
            //Gender Female
            currentChild.gender = genderFemale
        }

        currentParent?.firstName = parent_first_name.text.toString()
        currentParent?.lastName = parent_last_name.text.toString()
        currentParent?.relationshipToChild = parent_relationship.text.toString()
        currentParent?.phoneNumber = parent_phone_number.text.toString()
        currentParent?.email = parent_email.text.toString()

    }

    suspend fun saveUpdatedInformation(){
        kidViewModel.updateChildDetails(currentChild, currentParent)
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    private fun displayChildInformation(kid : Kid) {

        first_name.setText(kid.firstName)
        last_name.setText(kid.lastName)
        allergies.setText(kid.allergies)

        gender.isOn = (kid.gender == genderMale)

        val calendar : Calendar = Calendar.getInstance()
        calendar.timeInMillis = kid.dob

        val dateOfBirth  = BaseActivity.formatter.format(calendar.time)
        date_of_birth.setText(dateOfBirth)

        //Get details of the child's parent or guardian
        getParentDetails(kid.parents.get(0).id)

    }

    private fun getParentDetails(parentId : String) {

        val currentParentReference = FirebaseDatabase.getInstance().getReference("parents").child(parentId)
        currentParentReference.data()
                .subscribe({
                    currentParent = it.getValue<Parent>(Parent::class.java)
                    if (currentParent != null){
                        parent_first_name.setText(currentParent!!.firstName)
                        parent_last_name.setText(currentParent!!.lastName)
                        parent_relationship.setText(currentParent!!.relationshipToChild)
                        parent_phone_number.setText(currentParent!!.phoneNumber)
                        parent_email.setText(currentParent!!.email)
                    }
                }){
                    Log.d("", "Error getting the parent")
                }

    }

}
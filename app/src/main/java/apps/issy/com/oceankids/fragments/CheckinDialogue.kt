package apps.issy.com.oceankids.fragments

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.data.Attendance
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 *  Created by issy on 10/03/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class CheckinDialogue : DialogFragment () {

    private var attendanceReference : DatabaseReference? = null
    private var thisChildRef : DatabaseReference? = null
    private var okButton : Button? = null
    private var cardNumberInput : EditText? = null

    private var secondService : RelativeLayout? = null
    private var secondServiceCheck : RelativeLayout? = null
    private var thirdService : RelativeLayout? = null
    private var thirdServiceCheck : RelativeLayout? = null

    private var selectedService : Int = 2
    private var todaysDateKey : String = ""

    companion object {

        var childId : String = ""

        fun newInstance(id : String) : CheckinDialogue {
            childId = id
            val arguments : Bundle? = Bundle()
            val chekinDialogueInstance : CheckinDialogue = CheckinDialogue()
            arguments?.putString("id", childId!!.toString())
            chekinDialogueInstance.arguments = arguments
            return chekinDialogueInstance
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        var myView = inflater!!.inflate(R.layout.fragment_checkin, container, false)
        this.dialog.requestWindowFeature(STYLE_NO_TITLE)

        okButton = myView.findViewById(R.id.ok_button)
        cardNumberInput = myView.findViewById(R.id.card_number)

        secondService = myView.findViewById(R.id.second_service)
        secondServiceCheck = myView.findViewById(R.id.second_service_selected_check)
        thirdService = myView.findViewById(R.id.third_service)
        thirdServiceCheck = myView.findViewById(R.id.third_service_selected_check)

        secondService?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                secondServiceCheck?.visibility = View.VISIBLE
                thirdServiceCheck?.visibility = View.GONE
                selectedService = 2
            }
        })

        thirdService?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                thirdServiceCheck?.visibility = View.VISIBLE
                secondServiceCheck?.visibility = View.GONE
                selectedService = 3
            }
        })

        okButton?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

                thisChildRef = FirebaseDatabase.getInstance().getReference("kids").child("kids_list").child(childId)
                val calendar = Calendar.getInstance()
                var day = calendar.get(Calendar.DAY_OF_MONTH)
                var month = calendar.get(Calendar.MONTH)+1
                var year = calendar.get(Calendar.YEAR)
                todaysDateKey = day.toString()+"-"+month+"-"+year

                var attendance = Attendance()
                attendance.cardNumber = cardNumberInput?.text.toString()
                attendance.checkedOut = 0

                attendanceReference = FirebaseDatabase.getInstance().getReference("attendance")
                        .child(todaysDateKey)
                        .child(selectedService.toString())

                attendanceReference?.child(childId)?.setValue(attendance)

                thisChildRef?.
                        child("attendance")?.
                        child("checkedIn")?.
                        setValue(1)
                thisChildRef?.
                        child("attendance")?.
                        child("cardNumber")?.
                        setValue(cardNumberInput?.text?.toString())
                thisChildRef?.
                        child("attendance")?.
                        child("service")?.
                        setValue(selectedService)

                dismiss()

            }
        })

        return myView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id : String = arguments.getString("id")
        childId = id

    }

}
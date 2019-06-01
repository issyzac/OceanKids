package apps.issy.com.oceankids.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import apps.issy.com.oceankids.KidsListActivity
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.fragments.EditChildInfoFragment
import kotlinx.android.synthetic.main.kid_list_item.view.*
import java.util.*
import apps.issy.com.oceankids.database.entities.Kid
import apps.issy.com.oceankids.util.KidsDiffCallback
import apps.issy.com.oceankids.viewmodels.KidViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *  Created by issy on 23/02/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class AllKidsAdapter internal constructor(val activity: KidsListActivity) : RecyclerView.Adapter<AllKidsAdapter.ViewHolder>() {

    private var kids = emptyList<Kid>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kid_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(kids[position], activity)
    }

    override fun getItemCount(): Int {
        return kids.size
    }

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        fun bindForecast(kid: Kid, activity: KidsListActivity) {
            with(kid) {
                itemView.kids_names.text = kid.firstName+ " " + kid.lastName



                val kidViewModel = ViewModelProviders.of(activity).get(KidViewModel::class.java)

                var childYears : Int
                val todayCal : Calendar = Calendar.getInstance()
                val dobCal : Calendar = Calendar.getInstance()
                dobCal.timeInMillis = kid.dob
                childYears = todayCal.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR)
                if (todayCal.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR)){
                    childYears--
                }

                if (childYears in 0..2){
                    itemView.kids_class.text = "Nursery"
                    itemView.class_color_reflector.setBackgroundColor(activity.resources.getColor(R.color.yellow_600))
                }else if (childYears in 3..5){
                    itemView.kids_class.text = "Pre-School"
                    itemView.class_color_reflector.setBackgroundColor(activity.resources.getColor(R.color.purple_600))
                }else if (childYears in 6..10){
                    itemView.kids_class.text = "Primary"
                    itemView.class_color_reflector.setBackgroundColor(activity.resources.getColor(R.color.light_blue_600))
                }else if (childYears in 11..12){
                    itemView.kids_class.text = "Pre-Teens"
                    itemView.class_color_reflector.setBackgroundColor(activity.resources.getColor(R.color.blue_grey_900))
                }

                if (kid.attendance.checkedIn == 1){
                    itemView.checkin_child_id.visibility = View.GONE
                    itemView.checkout_child_id.visibility = View.VISIBLE
                    itemView.checkout_child_id.text = kid.attendance.cardNumber
                }else{
                    itemView.checkout_child_id.visibility = View.GONE
                    itemView.checkin_child_id.visibility = View.VISIBLE
                }

                var selectedService : Int = 2
                var cardNumberGiven : Int = 0
                var checkInChild : Boolean = false

                itemView.checkin_child_id.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        //CheckInClicked
//                        itemView.checkin_child_id.visibility = View.GONE
//                        itemView.checkout_child_id.visibility = View.VISIBLE

                        val mDialog = MaterialDialog(activity)
                                .customView(R.layout.fragment_checkin, scrollable = true)
                        val customView = mDialog.getCustomView()
                        val secondService = customView!!.findViewById<RelativeLayout>(R.id.second_service)
                        val secondServiceCheck = customView.findViewById<RelativeLayout>(R.id.second_service_selected_check)
                        val thirdService = customView.findViewById<RelativeLayout>(R.id.third_service)
                        val thirdServiceCheck = customView.findViewById<RelativeLayout>(R.id.third_service_selected_check)
                        val cardNumberInput = customView.findViewById<EditText>(R.id.card_number)

                        secondService.setOnClickListener(object : View.OnClickListener{
                            override fun onClick(p0: View?) {
                                secondServiceCheck.visibility = View.VISIBLE
                                thirdServiceCheck.visibility = View.GONE
                                selectedService = 2
                            }
                        })

                        thirdService.setOnClickListener(object : View.OnClickListener{
                            override fun onClick(p0: View?) {
                                thirdServiceCheck?.visibility = View.VISIBLE
                                secondServiceCheck.visibility = View.GONE
                                selectedService = 3
                            }
                        })

                        mDialog .positiveButton (text = "CHECK-IN") { dialog ->
                            if (cardNumberInput.text.isEmpty()){
                                Toast.makeText(activity, "Card Number cannot be empty", LENGTH_LONG).show()
                            }else{
                                cardNumberGiven = cardNumberInput.text.toString().toInt()
                                checkInChild = true
                                dialog.dismiss()
                            }
                        }

                        mDialog.negativeButton (text = "Cancel") { dialog ->
                            checkInChild = false
                            dialog.dismiss()
                        }

                        mDialog.onDismiss { dialog ->

                            if (checkInChild){
                                //Fire Check in this child
                                itemView.checkin_child_id.visibility = View.GONE
                                itemView.checkout_child_id.visibility = View.VISIBLE
                                itemView.checkout_child_id.text = cardNumberGiven.toString()

                                val kidAttendance  = Kid.Atendance()
                                kidAttendance.cardNumber = cardNumberGiven.toString()
                                kidAttendance.checkedIn = 1
                                kidAttendance.service = selectedService

                                kid.attendance = kidAttendance
                                kid.checkedIn = 1

                                GlobalScope.launch (Dispatchers.IO) {
                                    kidViewModel.checkinChild(kid)
                                }

                            }else{
                                //Do not check in this child
                                itemView.checkout_child_id.visibility = View.GONE
                                itemView.checkin_child_id.visibility = View.VISIBLE
                            }
                        }

                        mDialog.show()

                    }
                })

                itemView.checkout_child_id.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {


                        MaterialDialog(activity).show {

                            title(text = "Checkout Child")

                            message(text = "Are you sure you want to checkout "+kid.firstName+" "+kid.lastName+"?")

                            positiveButton(text = "Yes") { dialog ->

                                //Checking Out Child
                                itemView.checkin_child_id.visibility = View.VISIBLE
                                itemView.checkout_child_id.visibility = View.GONE

                                GlobalScope.launch (Dispatchers.IO) {
                                    kidViewModel.checkoutChild(kid)
                                }

                            }
                            negativeButton(text = "No") { dialog ->
                                dismiss()
                            }
                        }

                    }
                })

                itemView.edit_child_info_button.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        val editPopup = EditChildInfoFragment.newInstance(kid)
                        Log.d("sizex", "Child ID is "+kid.id)
                        editPopup.show(activity.supportFragmentManager, "edit_info")
                    }
                })
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return kids[position].id.hashCode().toLong()
    }


    fun setKids(updatedList : List<Kid> ){

        val kidsDiffCallback = KidsDiffCallback(this.kids, updatedList)
        val diffResult : DiffUtil.DiffResult = DiffUtil.calculateDiff (kidsDiffCallback)

        this.kids = emptyList()
        this.kids = updatedList
        diffResult.dispatchUpdatesTo(this)

    }

}

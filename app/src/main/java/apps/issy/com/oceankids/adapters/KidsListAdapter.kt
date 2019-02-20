//package apps.issy.com.oceankids.adapters
//
//import android.content.Intent
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import apps.issy.com.oceankids.ChildDetailsActivity
//import apps.issy.com.oceankids.KidsListActivity
//import apps.issy.com.oceankids.R
//import apps.issy.com.oceankids.data.Child
//import apps.issy.com.oceankids.database.entities.Kid
//import apps.issy.com.oceankids.fragments.CheckinDialogue
//import apps.issy.com.oceankids.fragments.EditChildInfoFragment
//import com.afollestad.materialdialogs.MaterialDialog
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.irozon.alertview.AlertActionStyle
//import com.irozon.alertview.AlertStyle
//import com.irozon.alertview.AlertView
//import com.irozon.alertview.objects.AlertAction
//import kotlinx.android.synthetic.main.kid_list_item.view.*
//import java.util.*
//import kotlin.collections.ArrayList
//
///**
// *  Created by issy on 23/02/2018.
// *  @issyzac
// *  issyzac.iz@gmail.com
// *  On Project OceanKids
// */
//
//class KidsListAdapter (var kids : List<Kid>, val firebaseData : DatabaseReference?, val activity: KidsListActivity, val itemClick: (Kid) -> Unit) : RecyclerView.Adapter<KidsListAdapter.ViewHolder>(){
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.kid_list_item, parent, false)
//        return ViewHolder(view, itemClick)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bindForecast(kids[position], firebaseData, activity)
//    }
//
//    override fun getItemCount(): Int {
//        return kids.size
//    }
//
//    class ViewHolder (view : View, val itemClick: (Kid) -> Unit) : RecyclerView.ViewHolder(view) {
//        fun bindForecast(kid: Kid, firebaseData : DatabaseReference?, activity: KidsListActivity) {
//            with(kid) {
//                itemView.kids_names.text = kid.firstName+ " " + kid.lastName
//
//
//                //Commented to disable detail viewing on check-in/out screen
//                /*itemView.setOnClickListener(object : View.OnClickListener{
//                    override fun onClick(p0: View?) {
//                        val intent = Intent(infoActivity, ChildDetailsActivity::class.java)
//                        intent.putExtra("kid_id", kid.id)
//                        infoActivity.startActivity(intent)
//                    }
//                })*/
//
//                itemView.card_number.text = kid.attendance.cardNumber
//
//               var childYears : Int
//                val todayCal : Calendar = Calendar.getInstance()
//                val dobCal : Calendar = Calendar.getInstance()
//                dobCal.timeInMillis = kid.dob
//                childYears = todayCal.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR)
//                if (todayCal.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR)){
//                    childYears--
//                }
//
//                if (childYears in 0..2){
//                    itemView.kids_class.text = "Nursery"
//                }else if (childYears in 3..5){
//                    itemView.kids_class.text = "Pre-School"
//                }else if (childYears in 6..10){
//                    itemView.kids_class.text = "Primary"
//                }else if (childYears in 11..12){
//                    itemView.kids_class.text = "Pre-Teens"
//                }
//
//                if (kid.attendance.checkedIn == 1){
//                    itemView.checkin_child_id.visibility = View.GONE
//                    itemView.checkout_child_id.visibility = View.VISIBLE
//                }else{
//                    itemView.checkout_child_id.visibility = View.GONE
//                    itemView.checkin_child_id.visibility = View.VISIBLE
//                }
//
//                itemView.checkin_child_id.setOnClickListener(object : View.OnClickListener{
//                    override fun onClick(p0: View?) {
//                        //CheckInClicked
////                        itemView.checkin_child_id.visibility = View.GONE
////                        itemView.checkout_child_id.visibility = View.VISIBLE
//                        val pop = CheckinDialogue.newInstance(kid)
//                        pop.show(activity.supportFragmentManager, "checkinChild")
//
//                    }
//                })
//
//                itemView.checkout_child_id.setOnClickListener(object : View.OnClickListener{
//                    override fun onClick(p0: View?) {
//
//                        MaterialDialog(activity).show {
//
//                            positiveButton(text = "Yes") { dialog ->
//                                //Checking Out Child
//                                itemView.checkin_child_id.visibility = View.VISIBLE
//                                itemView.checkout_child_id.visibility = View.GONE
//
//                                //Check child out in the child information node
//                                firebaseData?.child(kid.id)?.child("attendance")?.child("checkedIn")?.setValue(0)
//                                firebaseData?.child(kid.id)?.child("attendance")?.child("cardNumber")?.setValue("")
//
//                                //Check child out in the attendance Register node
//                                val calendar = Calendar.getInstance()
//                                val day = calendar.get(Calendar.DAY_OF_MONTH)
//                                val month = calendar.get(Calendar.MONTH)+1
//                                val year = calendar.get(Calendar.YEAR)
//                                val todaysDateKey = day.toString()+"-"+month+"-"+year
//                                val attendanceReference = FirebaseDatabase.getInstance().getReference("attendance")
//                                        .child(todaysDateKey)
//                                        .child(kid.attendance.service.toString())
//                                attendanceReference.child(kid.id).child("checkedOut").setValue(1)
//                            }
//                            negativeButton(text = "No") { dialog ->
//                                dismiss()
//                            }
//                        }
//
//                        val alertCheckout = AlertView("Checkout Child", "Are you sure you want to checkout "+kid.firstName+" "+kid.lastName, AlertStyle.DIALOG)
//                        alertCheckout.addAction(AlertAction("No", AlertActionStyle.NEGATIVE) { action ->
//
//                        })
//                        alertCheckout.addAction(AlertAction("Yes", AlertActionStyle.POSITIVE) { action ->
//
//                        })
//
//                    }
//                })
//
//                itemView.edit_child_info_button.setOnClickListener(object : View.OnClickListener{
//                    override fun onClick(p0: View?) {
//                        val editPopup = EditChildInfoFragment.newInstance(kid)
//                        Log.d("sizex", "Child ID is "+kid.id)
//                        editPopup.show(activity.supportFragmentManager, "edit_info")
//                    }
//                })
//            }
//        }
//    }
//
//    fun updateData(updatedList : List<Kid> ){
//        var emptyList : ArrayList<Kid> = ArrayList()
//        kids = emptyList
//        kids = updatedList
//        this.notifyDataSetChanged()
//    }
//
//}

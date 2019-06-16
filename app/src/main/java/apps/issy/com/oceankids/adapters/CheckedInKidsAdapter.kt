package apps.issy.com.oceankids.adapters

import android.content.Context.VIBRATOR_SERVICE
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.database.entities.Kid
import apps.issy.com.oceankids.fragments.CheckoutFragment
import apps.issy.com.oceankids.util.KidsDiffCallback
import apps.issy.com.oceankids.viewmodels.KidViewModel
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.checked_in_kids_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class CheckedInKidsAdapter internal constructor(val fragment: CheckoutFragment) : RecyclerView.Adapter<CheckedInKidsAdapter.ViewHolder>(){

    private var kids = emptyList<Kid>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checked_in_kids_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(kids[position], fragment)
    }

    override fun getItemCount(): Int {
        return kids.size
    }

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        fun bindForecast(kid: Kid, fragment: CheckoutFragment) {
            with(kid) {
                itemView.kids_names.text = kid.firstName+ " " + kid.lastName

                //Get the object of the current child
                val kidViewModel = ViewModelProviders.of(fragment).get(KidViewModel::class.java)

                //Calculate the age of the child from the date of birth
                var childYears : Int
                val todayCal : Calendar = Calendar.getInstance()
                val dobCal : Calendar = Calendar.getInstance()
                dobCal.timeInMillis = kid.dob
                childYears = todayCal.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR)
                if (todayCal.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR)){
                    childYears--
                }

                val vibration : Vibrator = fragment.activity!!.applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator

                //Init the views based on child details
                //itemView.profile_image.setColorFilter(fragment.context!!.resources.getColor(R.color.white))


                if (childYears in 0..2){
                    itemView.kids_class.text = "Nursery"
                    itemView.class_color_reflector.setBackgroundColor(fragment.resources.getColor(R.color.yellow_600))
                }else if (childYears in 3..5){
                    itemView.kids_class.text = "Pre-School"
                    itemView.class_color_reflector.setBackgroundColor(fragment.resources.getColor(R.color.purple_600))
                }else if (childYears in 6..10){
                    itemView.kids_class.text = "Primary"
                    itemView.class_color_reflector.setBackgroundColor(fragment.resources.getColor(R.color.light_blue_600))
                }else if (childYears in 11..12){
                    itemView.kids_class.text = "Pre-Teens"
                    itemView.class_color_reflector.setBackgroundColor(fragment.resources.getColor(R.color.blue_grey_900))
                }

                //Set the value of a child's card on the checkout button
                itemView.checkout_child_id.text = kid.attendance.cardNumber

                //Ask user to confirm checking out of the child
                itemView.checkout_child_id.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        vibration.vibrate(40)
                        MaterialDialog(p0!!.context).show {

                            title(text = "Checkout Child")
                            message(text = "Are you sure you want to checkout "+kid.firstName+" "+kid.lastName+"?")
                            positiveButton(text = "Yes") { dialog ->

                                GlobalScope.launch (Dispatchers.IO) {
                                    kidViewModel.checkingOutChild(kid)
                                }

                            }
                            negativeButton(text = "No") { dialog ->
                                dismiss()
                            }
                        }

                    }
                })
            }
        }
    }

    fun setKids(updatedList : List<Kid> ){

        val kidsDiffCallback = KidsDiffCallback(this.kids, updatedList)
        val diffResult : DiffUtil.DiffResult = DiffUtil.calculateDiff (kidsDiffCallback)

        this.kids = emptyList()
        this.kids = updatedList
        diffResult.dispatchUpdatesTo(this)

    }

}
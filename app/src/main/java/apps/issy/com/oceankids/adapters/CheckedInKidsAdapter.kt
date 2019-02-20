package apps.issy.com.oceankids.adapters

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

                val kidViewModel = ViewModelProviders.of(fragment).get(KidViewModel::class.java)

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
                }else if (childYears in 3..5){
                    itemView.kids_class.text = "Pre-School"
                }else if (childYears in 6..10){
                    itemView.kids_class.text = "Primary"
                }else if (childYears in 11..12){
                    itemView.kids_class.text = "Pre-Teens"
                }

                itemView.checkout_child_id.text = kid.attendance.cardNumber

                var selectedService : Int = 2
                var cardNumberGiven : Int = 0
                var checkInChild : Boolean = false

                itemView.checkout_child_id.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {

                        MaterialDialog(p0!!.context).show {

                            title(text = "Checkout Child")
                            message(text = "Are you sure you want to checkout "+kid.firstName+" "+kid.lastName+"?")
                            positiveButton(text = "Yes") { dialog ->

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
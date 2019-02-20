package apps.issy.com.oceankids.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.data.Child
import kotlinx.android.synthetic.main.birthday_child_view.view.*
import java.util.*

/**
 *  Created by issy on 25/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */
class ChildrenWithBirthdaysAdapter (private val children : ArrayList<Child>) : RecyclerView.Adapter<ChildrenWithBirthdaysAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.birthday_child_view, parent, false)
        return ViewHolder(rootView, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(children[position])
    }

    override fun getItemCount(): Int {
        return children.size
    }

    class ViewHolder(view : View, val context : Context) : RecyclerView.ViewHolder (view){

        fun bindView(child : Child){
            itemView.names.text = child.firstName+ " "+child.lastName
            //Calculate age and display in itemView.years

            val dateOfBirthCalendarInstance  = Calendar.getInstance()
            val todaysCalendarInstance = Calendar.getInstance()

            dateOfBirthCalendarInstance.timeInMillis = child.dob
            val birthdayMonth = dateOfBirthCalendarInstance.get(Calendar.MONTH)+1
            val birthdayDate = dateOfBirthCalendarInstance.get(Calendar.DAY_OF_MONTH)
            val birthdayYear = todaysCalendarInstance.get(Calendar.YEAR)

            var childAge = todaysCalendarInstance.get(Calendar.YEAR) - dateOfBirthCalendarInstance.get(Calendar.YEAR)

            itemView.years.text = childAge.toString()+" Years"
            itemView.birthday_date.text = birthdayDate.toString()+"/"+
                    birthdayMonth.toString()+"/"+
                    birthdayYear.toString()

            if (childAge in 0..3){
                itemView.names.setTextColor(context.resources.getColor(R.color.yellow_700))
                itemView.years.setTextColor(context.resources.getColor(R.color.yellow_700))
            }else if (childAge in 4..5){
                itemView.names.setTextColor(context.resources.getColor(R.color.purple_600))
                itemView.years.setTextColor(context.resources.getColor(R.color.purple_600))
            }else if (childAge in 6..10){
                itemView.names.setTextColor(context.resources.getColor(R.color.light_blue_600))
                itemView.years.setTextColor(context.resources.getColor(R.color.light_blue_600))
            }

        }

    }

}
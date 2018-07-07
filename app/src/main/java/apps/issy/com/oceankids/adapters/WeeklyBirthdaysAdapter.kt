package apps.issy.com.oceankids.adapters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import apps.issy.com.oceankids.BirthdaysActivity
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.data.BirthdayObject
import apps.issy.com.oceankids.data.Child
import kotlinx.android.synthetic.main.weekly_birthdays_item.view.*

/**
 *  Created by issy on 25/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class WeeklyBirthdaysAdapter (val birthdayObject: ArrayList<BirthdayObject>) : RecyclerView.Adapter<WeeklyBirthdaysAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.weekly_birthdays_item, parent, false)
        return ViewHolder(rootView, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //do stuff..
        holder.bindView(birthdayObject[position])
    }

    override fun getItemCount(): Int {
        return birthdayObject.size
    }

    class ViewHolder (view: View, val context : Context) : RecyclerView.ViewHolder(view) {
        fun bindView(birthdayObject: BirthdayObject){
            itemView.week_title_text.text = "Week "+birthdayObject.dateRange

            if (birthdayObject.listOfChildren.size > 0){
                itemView.single_birthdays_recycler.visibility = View.VISIBLE
                itemView.txt_message.visibility = View.GONE
            }else{
                itemView.single_birthdays_recycler.visibility = View.GONE
                itemView.txt_message.visibility = View.VISIBLE
            }

            val childrenList : ArrayList<Child> = birthdayObject.listOfChildren
            //initialize the kids recycler view, create the recycler adapter and add to the recyclerView
            val lLayoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
            itemView.single_birthdays_recycler.layoutManager = lLayoutManager
            itemView.single_birthdays_recycler.hasFixedSize()

            val adapter = ChildrenWithBirthdaysAdapter(childrenList)
            itemView.single_birthdays_recycler.adapter = adapter

        }
    }

}
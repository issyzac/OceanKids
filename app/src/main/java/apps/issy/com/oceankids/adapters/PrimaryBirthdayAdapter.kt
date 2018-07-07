package apps.issy.com.oceankids.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.issy.com.oceankids.PreSchoolBirthdayActivity
import apps.issy.com.oceankids.PrimaryBirthdayActivity
import apps.issy.com.oceankids.R
import kotlinx.android.synthetic.main.nursery_list_item.view.*
import java.util.ArrayList

/**
 *  Created by issy on 07/07/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */
class PrimaryBirthdayAdapter (val months : ArrayList<String>, val itemClick: (String) -> Unit) : RecyclerView.Adapter<PrimaryBirthdayAdapter.ViewHolder>(){

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nursery_list_item, parent, false)
        context = parent.context
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(months[position])
        if (position == PrimaryBirthdayActivity.selectedPosition){
            val colorPurple : Int = context!!.resources.getColor(R.color.light_blue_100)
            holder.itemView?.setBackgroundColor( colorPurple )
        }else {
            val colorLight : Int = context!!.resources.getColor(R.color.card_separator_third)
            holder.itemView?.setBackgroundColor( colorLight )
        }

    }

    override fun getItemCount(): Int {
        return months.size
    }

    class ViewHolder (view : View, val itemClick: (String) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindForecast(month: String) {
            with(month) {
                itemView.nursery_kids_names.text = month
            }
        }
    }

}
package apps.issy.com.oceankids.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import apps.issy.com.oceankids.NuseryKidsInfoActivity
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.data.Child
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.nursery_list_item.view.*
import java.util.*

/**
 *  Created by issy on 13/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class NurseryListAdapter (val kids : ArrayList<Child>, val firebaseData : DatabaseReference?, val infoActivity: NuseryKidsInfoActivity, val itemClick: (Child) -> Unit) : RecyclerView.Adapter<NurseryListAdapter.ViewHolder>(){

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nursery_list_item, parent, false)
        context = parent.context
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(kids[position], firebaseData, infoActivity)
        if (position == NuseryKidsInfoActivity.selectedPosition){
            val colorPurple : Int = context!!.resources.getColor(R.color.yellow_100)
            holder.itemView?.setBackgroundColor( colorPurple )
        }else {
            val colorLight : Int = context!!.resources.getColor(R.color.card_separator_third)
            holder.itemView?.setBackgroundColor( colorLight )
        }

    }

    override fun getItemCount(): Int {
        return kids.size
    }

    class ViewHolder (view : View, val itemClick: (Child) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindForecast(kid: Child, firebaseData : DatabaseReference?, infoActivity: NuseryKidsInfoActivity) {
            with(kid) {
                itemView.nursery_kids_names.text = kid.firstName+ " " + kid.lastName
            }
        }
    }

}

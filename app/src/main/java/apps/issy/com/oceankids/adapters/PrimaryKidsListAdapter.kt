package apps.issy.com.oceankids.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.issy.com.oceankids.PrimaryKidsRewardActivity
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.data.Child
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.primary_kids_list_item.view.*
import java.util.ArrayList

/**
 *  Created by issy on 05/07/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */
class PrimaryKidsListAdapter (val kids : ArrayList<Child>, val firebaseData : DatabaseReference?, val infoActivity: PrimaryKidsRewardActivity, val itemClick: (Child) -> Unit) : RecyclerView.Adapter<PrimaryKidsListAdapter.ViewHolder>(){

    private var selectedItem : Int = -1
    var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.primary_kids_list_item, parent, false)
        context = parent.context
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindChild(position, kids[position], firebaseData, infoActivity)

        if(holder.adapterPosition == PrimaryKidsRewardActivity.selectedPosition){
            val colorYellow : Int = context!!.resources.getColor(R.color.light_blue_100)
            holder.itemView?.setBackgroundColor( colorYellow )
        }else{
            val colorLight : Int = context!!.resources.getColor(R.color.card_separator_third)
            holder.itemView?.setBackgroundColor( colorLight )
        }

        holder.itemView?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                setClicked(holder.adapterPosition)
                notifyDataSetChanged()
            }
        })

    }

    override fun getItemCount(): Int {
        return kids.size
    }

    fun setClicked(position: Int){
        selectedItem = position
    }

    class ViewHolder (view : View, val itemClick: (Child) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindChild(pos : Int, kid: Child, firebaseData : DatabaseReference?, primaryKidsRewardActivity: PrimaryKidsRewardActivity) {
            with(kid) {
                itemView.primary_kids_names.text = kid.firstName+ " " + kid.lastName
            }
        }

    }

}
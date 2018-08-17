package apps.issy.com.oceankids.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.issy.com.oceankids.PreSchoolInfoActivity
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.data.Child
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.preschool_list_item.view.*
import java.util.*

/**
 *  Created by issy on 13/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class PreSchoolListAdapter (val kids : ArrayList<Child>, val firebaseData : DatabaseReference?, val infoActivity: PreSchoolInfoActivity, val itemClick: (Child) -> Unit) : RecyclerView.Adapter<PreSchoolListAdapter.ViewHolder>(){

    public var selectedItem : Int = -1
    var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.preschool_list_item, parent, false)
        context = parent.context
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindForecast(position, kids[position], firebaseData, infoActivity)

        if(position == PreSchoolInfoActivity.selectedPosition){
            var colorYellow : Int = context!!.resources.getColor(R.color.purple_100)
            holder?.itemView?.setBackgroundColor( colorYellow )
        }else{
            var colorLight : Int = context!!.resources.getColor(R.color.card_separator_third)
            holder?.itemView?.setBackgroundColor( colorLight )
        }

        holder?.itemView?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                setClicked(position)
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

        fun bindForecast(pos : Int, kid: Child, firebaseData : DatabaseReference?, infoActivity: PreSchoolInfoActivity) {
            with(kid) {
                itemView.pre_school_kids_names.text = kid.firstName+ " " + kid.lastName
            }
        }

    }

}
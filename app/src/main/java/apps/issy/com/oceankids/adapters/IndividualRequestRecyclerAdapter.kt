package apps.issy.com.oceankids.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.data.SingleRequestItem
import apps.issy.com.oceankids.data.WeeklyResponses
import kotlinx.android.synthetic.main.requests_contents_item.view.*

/**
 *  Created by issy on 02/04/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class IndividualRequestRecyclerAdapter (val items : ArrayList<SingleRequestItem>) : RecyclerView.Adapter<IndividualRequestRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.requests_contents_item, parent, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val emptySingleRequest : SingleRequestItem = SingleRequestItem()
        holder.bindItem(emptySingleRequest)
    }

    override fun getItemCount(): Int {
        //return items.size
        return 6
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

        fun bindItem(item : SingleRequestItem){
            itemView.kids_name.text = item.kidsName
            itemView.contents.text = item.kidsReruest
        }

    }
}
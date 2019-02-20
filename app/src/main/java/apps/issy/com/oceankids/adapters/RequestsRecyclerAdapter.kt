package apps.issy.com.oceankids.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.data.WeeklyResponses
import kotlinx.android.synthetic.main.prayer_and_thanksgiving_item.view.*

/**
 *  Created by issy on 02/04/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class RequestsRecyclerAdapter (val context: Context, val weeklyResponses : ArrayList<WeeklyResponses>,val fm: FragmentManager?) : RecyclerView.Adapter<RequestsRecyclerAdapter.ViewHolder>(){

    val TAG : String = "RequestsRecyclerAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.prayer_and_thanksgiving_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val emptyResponses : WeeklyResponses = WeeklyResponses()

        holder.bindKidRequest(emptyResponses, position, fm)
    }

    override fun getItemCount(): Int {
        //return weeklyResponses.size
        return 2
    }


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bindKidRequest(thisWeekResponses : WeeklyResponses, pos: Int, fragmentManager: FragmentManager?){
            /**
             * Assign the date to this week's title
             * Initialize the tablayout for this week's prayers and thanks giving
             * initialize the viewpager
             *
             * from @thisWeekResponses object get this weeks prayers list and this week's thanks giving list
             *      create an instance of the prayer list fragment passing this weeks prayers as well as an instance
             *      of thanks giving and pass this weeks thanks giving list
             *
             *      At the fragment [prayer|thanks giving] initialize the corresponding recyclerview create the corresponding adapter
             *      for the list of either prayers or thanks giving and add the list of data to it passed to that fragment from this
             *      adapter
             */

            itemView.this_week_date.text = "Sunday "+(pos+1)+"th April, 2018"

            val requestsAdapter = RequestsAdapter(thisWeekResponses.kro)

            val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(itemView.context)
            itemView.prayers_recycler.hasFixedSize()
            itemView.prayers_recycler.layoutManager = linearLayoutManager
            itemView.prayers_recycler.adapter = requestsAdapter

        }
    }
}
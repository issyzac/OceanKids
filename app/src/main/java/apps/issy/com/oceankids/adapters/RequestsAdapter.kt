package apps.issy.com.oceankids.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.data.KidsRequests
import kotlinx.android.synthetic.main.kid_list_item.view.*

/**
 *  Created by issy on 10/04/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class RequestsAdapter (private val prayers: ArrayList<KidsRequests>) :
        RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.requests_contents_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val emptyRequest : KidsRequests = KidsRequests()
        holder.bindForecast(emptyRequest)
    }

    override fun getItemCount(): Int {
        //return prayers.size
        return 6
    }

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        fun bindForecast(kidsRequests: KidsRequests) {
            with(kidsRequests) {

            }
        }
    }

}
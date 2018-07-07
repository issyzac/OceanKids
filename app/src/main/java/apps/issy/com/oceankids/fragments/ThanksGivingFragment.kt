package apps.issy.com.oceankids.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.adapters.IndividualRequestRecyclerAdapter
import apps.issy.com.oceankids.data.KidsRequests
import apps.issy.com.oceankids.data.SingleRequestItem
import kotlinx.android.synthetic.main.thanks_giving_fragment.*

/**
 *  Created by issy on 02/04/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class ThanksGivingFragment : Fragment(){

    companion object {

        var items : ArrayList<KidsRequests> = ArrayList()

        fun newInstance(_items : ArrayList<KidsRequests>) : Fragment{
            this.items = _items
            return ThanksGivingFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.thanks_giving_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //inititialize the recyclerview and add an adapter to it

        /**
         * Get the list of kids requests from this day and go through them to get only thanks giving
         * then convert the thanks giving to {@link SingleRequestItem } object
         */

        val linearLayoutManager = LinearLayoutManager(view.context)
        thanks_giving_recycler.layoutManager = linearLayoutManager
        thanks_giving_recycler.hasFixedSize()

        val emptyItems : ArrayList<SingleRequestItem> = ArrayList()
        thanks_giving_recycler.adapter = IndividualRequestRecyclerAdapter(emptyItems)

    }

}
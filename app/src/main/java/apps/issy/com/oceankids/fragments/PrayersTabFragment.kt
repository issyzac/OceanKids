package apps.issy.com.oceankids.fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.issy.com.oceankids.MainActivity
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.adapters.RequestsRecyclerAdapter
import apps.issy.com.oceankids.data.WeeklyResponses
import com.mcxiaoke.koi.ext.getActivity
import kotlinx.android.synthetic.main.fragment_prayer.*

/**
 *  Created by issy on 31/03/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class PrayersTabFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView : View = inflater!!.inflate(R.layout.fragment_prayer, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Handle app logics after the view was already created

        val linearLayoutManager : RecyclerView.LayoutManager = LinearLayoutManager(view.context)
        kids_requests_recycler_view.hasFixedSize()
        kids_requests_recycler_view.layoutManager = linearLayoutManager


        var data = ArrayList<WeeklyResponses>()
        kids_requests_recycler_view.adapter = RequestsRecyclerAdapter(view.context,  data, this.childFragmentManager)

    }

}
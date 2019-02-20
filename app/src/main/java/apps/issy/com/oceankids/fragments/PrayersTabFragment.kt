package apps.issy.com.oceankids.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.adapters.RequestsRecyclerAdapter
import apps.issy.com.oceankids.data.WeeklyResponses
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
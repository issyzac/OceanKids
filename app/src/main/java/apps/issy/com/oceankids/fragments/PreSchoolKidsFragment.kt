package apps.issy.com.oceankids.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.issy.com.oceankids.R

/**
 *  Created by issy on 28/05/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class PreSchoolKidsFragment : Fragment(){

    fun newInstance() : PreSchoolKidsFragment ? {
        val fragmentInstance : PreSchoolKidsFragment = PreSchoolKidsFragment()
        return fragmentInstance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView : View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //..
    }

}
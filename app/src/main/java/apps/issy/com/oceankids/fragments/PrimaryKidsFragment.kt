package apps.issy.com.oceankids.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import apps.issy.com.oceankids.R

/**
 *  Created by issy on 28/05/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class PrimaryKidsFragment : Fragment() {

    public fun newInstance() : PrimaryKidsFragment?{
        var fragmentInstance : PrimaryKidsFragment = apps.issy.com.oceankids.fragments.PrimaryKidsFragment()
        return fragmentInstance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView : View = inflater.inflate(R.layout.primary_kids_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
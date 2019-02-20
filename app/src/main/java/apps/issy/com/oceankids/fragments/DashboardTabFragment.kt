package apps.issy.com.oceankids.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import apps.issy.com.oceankids.KidsListActivity
import apps.issy.com.oceankids.R
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.jetbrains.anko.startActivity

/**
 *  Created by issy on 31/03/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class DashboardTabFragment : Fragment() {

    public fun newInstance() : DashboardTabFragment?{
        var fragmentInstance : DashboardTabFragment = DashboardTabFragment()
        return fragmentInstance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView : View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        primary_class_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                p0?.context?.startActivity<KidsListActivity>()
            }
        })

    }

}
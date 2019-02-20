package apps.issy.com.oceankids

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.adapters.AllKidsAdapter
import apps.issy.com.oceankids.database.entities.Kid
import apps.issy.com.oceankids.viewmodels.KidViewModel
import com.mcxiaoke.koi.ext.onTextChange

/**
 *  Created by issy on 23/02/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

import kotlinx.android.synthetic.main.class_list_activity.*


class KidsListActivity : BaseActivity() {

    private lateinit var kidViewModel : KidViewModel

    var searchedKids : List<Kid> = ArrayList()

    var allKidsList : List<Kid> = ArrayList()

    var searchAdapter : AllKidsAdapter = AllKidsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.class_list_activity)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //Wrap the viewModel in a viewModelProvider for configuration changes sake
        kidViewModel = ViewModelProviders.of(this).get(KidViewModel::class.java)

        val searchResultsLinearLayoutManager = LinearLayoutManager(this)


        search_results.layoutManager = searchResultsLinearLayoutManager

        search_results.setHasFixedSize(true)

        search_results.adapter = searchAdapter

        //After all has been loaded program will continue here
        kidViewModel.allKids.observe(this, Observer { kids ->
            kids?.let {
                allKidsList = it
            }
        })

        //loadKids()

        search_names.onTextChange { text, _, _, _ ->

            if (text.isEmpty()){
                search_results.visibility = View.INVISIBLE
            }else{
                search_results.visibility = View.VISIBLE
            }

            val res =  allKidsList.filter {
                it.firstName.startsWith(text, ignoreCase = true) || it.lastName.startsWith(text, ignoreCase = true)
            }
            val result : ArrayList<Kid> = ArrayList(res)
            searchAdapter.setKids(result)

        }

        all_checkin_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

//                nursery_checkin_button.setBackgroundResource(R.drawable.border_one)
//                pre_school_checkin_button.setBackgroundResource(R.drawable.border_one)
//                primary_checkin_button.setBackgroundResource(R.drawable.border_one)
                all_checkin_button.setBackgroundResource(R.drawable.border_filter_button)
                all_checkout_button.setBackgroundResource(R.drawable.border_one)

                checkin_container.visibility = View.VISIBLE
                checkout_fragment.view!!.visibility = View.INVISIBLE

            }
        })

        all_checkout_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

                all_checkout_button.setBackgroundResource(R.drawable.border_filter_button)
                all_checkin_button.setBackgroundResource(R.drawable.border_one)

                checkin_container.visibility = View.INVISIBLE
                checkout_fragment.view!!.visibility = View.VISIBLE

            }
        })

    }

}
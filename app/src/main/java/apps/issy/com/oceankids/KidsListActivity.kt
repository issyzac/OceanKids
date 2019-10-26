package apps.issy.com.oceankids

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.adapters.AllKidsAdapter
import apps.issy.com.oceankids.database.entities.Kid
import apps.issy.com.oceankids.util.Constants
import apps.issy.com.oceankids.util.SharedPreferencesHelper
import apps.issy.com.oceankids.viewmodels.KidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mcxiaoke.koi.Const
import com.mcxiaoke.koi.ext.onTextChange


/**
 *  Created by issy on 23/02/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

import kotlinx.android.synthetic.main.class_list_activity.*
import org.jetbrains.anko.share


class KidsListActivity : BaseActivity() {

    private lateinit var kidViewModel : KidViewModel

    var searchedKids : List<Kid> = ArrayList()

    var allKidsList : List<Kid> = ArrayList()

    var searchAdapter : AllKidsAdapter = AllKidsAdapter(this)

    private val auth : FirebaseAuth? = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.class_list_activity)

        setSupportActionBar(toolbar)

        if (auth?.currentUser == null){
            val intent  = Intent(this@KidsListActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val sharedPreferences : SharedPreferencesHelper = SharedPreferencesHelper(this)
        kids_list_message_text.setText(Constants.KIDS_LIST_DEFAULT_MESSAGE)

        sharedPreferences.save(Constants.CHECKING_IN, true)

        if (sharedPreferences.getValueBoolean(Constants.CHECKING_IN, true) == true){
            //Show the lading kids loader
            kids_loading_indicator.visibility = View.VISIBLE
            kids_list_message_text.setText("Loading Kids")
        }

        //Wrap the viewModel in a viewModelProvider for configuration changes sake
        kidViewModel = ViewModelProviders.of(this).get(KidViewModel::class.java)

        val searchResultsLinearLayoutManager = LinearLayoutManager(this)

        searchAdapter.setHasStableIds(true)

        search_results.layoutManager = searchResultsLinearLayoutManager
        search_results.setHasFixedSize(true)
        search_results.setItemViewCacheSize(20)
        search_results.adapter = searchAdapter

        //After all has been loaded program will continue here
        kidViewModel.allKids.observe(this, Observer { kids ->
            kids?.let {
                allKidsList = it
                kids_loading_indicator.visibility = View.INVISIBLE
                kids_list_message_text.setText(Constants.KIDS_LIST_DEFAULT_MESSAGE)
                searchAdapter.setKids(allKidsList)
            }
        })

        //loadKids()

        search_names.onTextChange { text, _, _, _ ->

            if (text.isEmpty()){
                search_results.visibility = View.VISIBLE
            }else{
                search_results.visibility = View.VISIBLE
            }

            val res =  allKidsList.filter {
                it.firstName.startsWith(text, ignoreCase = true) || it.lastName.startsWith(text, ignoreCase = true)
            }
            if (res.size <= 0){
                kids_list_message_text.setText("Child could not be found!")
            }else{
                kids_list_message_text.setText("")
            }

            val result : ArrayList<Kid> = ArrayList(res)
            searchAdapter.setKids(result)

        }

        all_checkin_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

                if (sharedPreferences.getValueBoolean(Constants.CHECKING_IN, true) == false){
                    sharedPreferences.save(Constants.CHECKING_IN, true)
                }

                all_checkin_button.setBackgroundResource(R.drawable.border_filter_button)
                all_checkout_button.setBackgroundResource(R.drawable.border_one)

                checkin_container.visibility = View.VISIBLE
                checkout_fragment.view!!.visibility = View.INVISIBLE

            }
        })

        all_checkout_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

                if (sharedPreferences.getValueBoolean(Constants.CHECKING_IN, true) == true){
                    sharedPreferences.save(Constants.CHECKING_IN, false)
                }

                all_checkout_button.setBackgroundResource(R.drawable.border_filter_button)
                all_checkin_button.setBackgroundResource(R.drawable.border_one)

                checkin_container.visibility = View.INVISIBLE
                checkout_fragment.view!!.visibility = View.VISIBLE

            }
        })

    }

}
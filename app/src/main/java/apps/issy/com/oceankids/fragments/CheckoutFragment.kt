package apps.issy.com.oceankids.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.R
import apps.issy.com.oceankids.adapters.CheckedInKidsAdapter
import apps.issy.com.oceankids.database.entities.Kid
import apps.issy.com.oceankids.viewmodels.KidViewModel
import com.mcxiaoke.koi.ext.onTextChange
import kotlinx.android.synthetic.main.fragment_checkout.*

class CheckoutFragment : Fragment() {

    private lateinit var kidViewModel : KidViewModel

    var currentAdapterType : Int = 1

    var nurseryKidsList : List<Kid> = ArrayList()
    var preschoolKidsList : List<Kid> = ArrayList()
    var primaryKidsList : List<Kid> = ArrayList()

    var preSchoolAdapter : CheckedInKidsAdapter = CheckedInKidsAdapter(this)
    var primaryAdapter : CheckedInKidsAdapter = CheckedInKidsAdapter(this)
    var nurseryAdapter : CheckedInKidsAdapter = CheckedInKidsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        kidViewModel = ViewModelProviders.of(this).get(KidViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nurseryListLinearLayoutManager = LinearLayoutManager(this.context)
        val preSchoolListLinearLayoutManager = LinearLayoutManager(this.context)
        val primaryListLinearLayoutManager = LinearLayoutManager(this.context)

        nursery_list.layoutManager = nurseryListLinearLayoutManager
        pre_school_list.layoutManager = preSchoolListLinearLayoutManager
        primary_list.layoutManager = primaryListLinearLayoutManager

        nursery_list.setHasFixedSize(true)
        pre_school_list.setHasFixedSize(true)
        primary_list.setHasFixedSize(true)

        nursery_list.adapter = nurseryAdapter
        pre_school_list.adapter = preSchoolAdapter
        primary_list.adapter = primaryAdapter


        kidViewModel.checkedInKids.observe(this, Observer { kids ->
            separateKidsByClass(kids)
            getSpecificClass(currentAdapterType)
        })

        nursery_checkout_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                nursery_checkout_button.setBackgroundResource(R.drawable.border_filter_button)
                pre_school_checkout_button.setBackgroundResource(R.drawable.border_one)
                primary_checkout_button.setBackgroundResource(R.drawable.border_one)

                currentAdapterType = 1
                getSpecificClass(currentAdapterType)
            }
        })

        pre_school_checkout_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                nursery_checkout_button.setBackgroundResource(R.drawable.border_one)
                pre_school_checkout_button.setBackgroundResource(R.drawable.border_filter_button)
                primary_checkout_button.setBackgroundResource(R.drawable.border_one)

                currentAdapterType = 2
                getSpecificClass(currentAdapterType)

            }
        })

        primary_checkout_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                nursery_checkout_button.setBackgroundResource(R.drawable.border_one)
                pre_school_checkout_button.setBackgroundResource(R.drawable.border_one)
                primary_checkout_button.setBackgroundResource(R.drawable.border_filter_button)

                currentAdapterType = 3
                getSpecificClass(currentAdapterType)

            }
        })

        card_number_filter.onTextChange { text, start, before, count ->

            when (currentAdapterType){
                1 -> {
                    val res = nurseryKidsList
                            .filter { it.attendance.cardNumber.startsWith(text, ignoreCase = true) }
                    val result : ArrayList<Kid> = ArrayList(res)
                    updateSearch(result)
                }
                2 -> {
                    val res = preschoolKidsList
                            .filter { it.attendance.cardNumber.startsWith(text, ignoreCase = true) }
                    val result : ArrayList<Kid> = ArrayList(res)
                    updateSearch(result)
                }
                3 -> {
                    val res = primaryKidsList
                            .filter { it.attendance.cardNumber.startsWith(text, ignoreCase = true) }
                    val result : ArrayList<Kid> = ArrayList(res)
                    updateSearch(result)
                }
            }

        }

        names_filter.onTextChange { text, start, before, count ->

            when (currentAdapterType) {
                1 -> {
                    val res = nurseryKidsList
                            .filter { it.firstName.startsWith(text, ignoreCase = true) || it.lastName.startsWith(text, ignoreCase = true)}
                    val result : ArrayList<Kid> = ArrayList(res)
                    updateSearch(result)
                }
                2 -> {
                    val res = preschoolKidsList
                            .filter { it.firstName.startsWith(text, ignoreCase = true) || it.lastName.startsWith(text, ignoreCase = true)}
                    val result : ArrayList<Kid> = ArrayList(res)
                    updateSearch(result)
                }
                3 -> {
                    val res = primaryKidsList
                            .filter { it.firstName.startsWith(text, ignoreCase = true) || it.lastName.startsWith(text, ignoreCase = true)}
                    val result : ArrayList<Kid> = ArrayList(res)
                    updateSearch(result)
                }
            }

        }

    }

    fun separateKidsByClass(kids : List<Kid>){

        val primList : ArrayList<Kid> = ArrayList()
        val preList : ArrayList<Kid> = ArrayList()
        val nursList : ArrayList<Kid> = ArrayList()

        for (currentChild in kids){

            if (currentChild.dob > BaseActivity.toPrimaryAge){
                if (currentChild.dob > BaseActivity.toPreSchoolAge){
                    if (currentChild.dob > BaseActivity.toNurseryAge){
                        nursList.add(currentChild)
                    }else{
                        preList.add(currentChild)
                    }
                }else{
                    primList.add(currentChild)
                }
            }
        }

        nurseryKidsList = nursList
        nurseryAdapter.setKids(nursList)

        preschoolKidsList = preList
        preSchoolAdapter.setKids(preList)

        primaryKidsList = primList
        primaryAdapter.setKids(primList)

    }

    private fun getSpecificClass(classNumber : Int){
        currentAdapterType = classNumber
        updateAdapter(currentAdapterType)
    }

    private fun updateSearch(data : List<Kid>){

        //Update the appropriate adapter depending on the current adapter in view
        when (currentAdapterType){
            1 -> { nurseryAdapter.setKids(data) }
            2 -> { preSchoolAdapter.setKids(data) }
            3 -> { primaryAdapter.setKids(data) }
        }

    }

    private fun updateAdapter(type : Int){

        nursery_list.visibility = View.INVISIBLE
        pre_school_list.visibility = View.INVISIBLE
        primary_list.visibility = View.INVISIBLE

        names_filter.setText("")
        card_number_filter.setText("")

        when (type){
            1 -> { nursery_list.visibility = View.VISIBLE }
            2 -> { pre_school_list.visibility = View.VISIBLE }
            3 -> { primary_list.visibility = View.VISIBLE }
        }

    }

}
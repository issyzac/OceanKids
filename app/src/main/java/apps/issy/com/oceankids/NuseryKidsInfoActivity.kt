package apps.issy.com.oceankids

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.adapters.NurseryListAdapter
import apps.issy.com.oceankids.data.Child
import apps.issy.com.oceankids.data.Parent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mcxiaoke.koi.ext.onTextChange
import kotlinx.android.synthetic.main.activity_nursery_info.*
import java.util.*
import kotlin.collections.ArrayList

/**
 *  Created by issy on 12/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class NuseryKidsInfoActivity : BaseActivity(){

    var kidsList : ArrayList<Child> = ArrayList()
    var adapter : NurseryListAdapter = NurseryListAdapter(kidsList, childReference, this){}

    companion object {
        var selectedPosition : Int = RecyclerView.NO_POSITION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nursery_info)

        val linearLayoutManager = LinearLayoutManager(this)
        nursery_kids_list_recycler.layoutManager = linearLayoutManager
        nursery_kids_list_recycler.hasFixedSize()

        parent_name_icon.setColorFilter(resources.getColor(R.color.yellow_900))
        parent_phone_icon.setColorFilter(resources.getColor(R.color.yellow_900))

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val kidsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val toReturn: ArrayList<Child> = ArrayList()
                for (data in dataSnapshot.children){
                    try {
                        val oneChild = data.getValue<Child>(Child::class.java)
                        val child = oneChild.let { it } ?: continue

                        //Calculate child year before listing to this class
                        var childYears : Int
                        var todayCal : Calendar = Calendar.getInstance()
                        var dobCal : Calendar = Calendar.getInstance()
                        dobCal.timeInMillis = child.dob
                        childYears = todayCal.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR)
                        if (todayCal.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR)){
                            childYears--
                        }

                        if (childYears in 0..3){
                            toReturn.add(child)
                        }

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

                kidsList  = toReturn
                accommodateItemSizeChanges(kidsList)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }
        childReference?.addValueEventListener(kidsListener)

        nursery_kids_list_recycler.addOnItemClickListener(object: NuseryKidsInfoActivity.OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (kidsList.size > 0){

                    parent_name_value.text = ""
                    phone_value.text = ""

                    selectedPosition = position
                    adapter.notifyDataSetChanged()

                    var child  = kidsList.get(position)

                    nusery_details_kid_names.text = child.firstName+" "+child.lastName
                    nursery_allergies_value.text = child.allergies

                    getParentDetails(child.parents)
                }
            }
        } )

        nursery_kids_search_et.onTextChange { text, start, before, count ->
            val res = kidsList
                    .filter { it.firstName!!.startsWith(text, ignoreCase = true) || it.lastName!!.startsWith(text, ignoreCase = true) }
            val result : ArrayList<Child> = ArrayList(res)
            accommodateItemSizeChanges(result)
        }

    }

    private fun accommodateItemSizeChanges(items : ArrayList<Child>){

        if (items.size > 0){

            selectedPosition = 0

            message_text.visibility = View.GONE
            nursery_kids_list_recycler.visibility = View.VISIBLE
            child_info_details_container.visibility = View.VISIBLE
            progress_view.visibility = View.GONE

            setupAdapter(items)
            val child : Child = items[0]

            nusery_details_kid_names.text = child.firstName+" "+child.lastName
            nursery_allergies_value.text = child.allergies

            getParentDetails(child.parents)

        }else{

            message_text.visibility = View.VISIBLE
            nursery_kids_list_recycler.visibility = View.GONE
            child_info_details_container.visibility = View.GONE
            progress_view.visibility = View.GONE

            message_text.text = "No Child Available"

        }
    }

    private fun getParentDetails(parents : ArrayList<Child.Parent>){
        var currentChildParentReference : DatabaseReference? = parentsReference
        for (parent in parents){

            val parentId = parent.id
            currentChildParentReference = currentChildParentReference!!.child(parentId)

            val parentValueListener = object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val currentParent = p0.getValue<Parent>(Parent::class.java)
                    parent_name_value.text = currentParent?.firstName + " " + currentParent?.lastName
                    phone_value.text = currentParent!!.phoneNumber
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            }
            currentChildParentReference.addListenerForSingleValueEvent(parentValueListener)

        }
    }

    private fun setupAdapter(data: ArrayList<Child>){

        adapter = NurseryListAdapter(data, childReference, this) {
            //..
        }
        nursery_kids_list_recycler.adapter = adapter
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View?) {
                view?.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View?) {
                view?.setOnClickListener({
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                })
            }
        })
    }

}
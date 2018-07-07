package apps.issy.com.oceankids

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.adapters.PreSchoolListAdapter
import apps.issy.com.oceankids.data.Child
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_preschool.*
import java.util.*
import java.util.Calendar.DAY_OF_YEAR
import java.util.Calendar.YEAR
import kotlin.collections.ArrayList

/**
 *  Created by issy on 12/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */
 class PreSchoolInfoActivity : BaseActivity(){

    var kidsList : ArrayList<Child> = ArrayList()
    var adapter : PreSchoolListAdapter = PreSchoolListAdapter(kidsList, childReference, this){}

    companion object {
        var selectedPosition : Int = RecyclerView.NO_POSITION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preschool)

        parent_name_icon.setColorFilter(resources.getColor(R.color.yellow_900))
        parent_phone_icon.setColorFilter(resources.getColor(R.color.yellow_900))

        val linearLayoutManager = LinearLayoutManager(this)
        preschool_kids_list_recycler.layoutManager = linearLayoutManager
        preschool_kids_list_recycler.hasFixedSize()

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
                        childYears = todayCal.get(YEAR) - dobCal.get(YEAR)
                        if (todayCal.get(DAY_OF_YEAR) < dobCal.get(DAY_OF_YEAR)){
                            childYears--
                        }

                        if (childYears in 4..5){
                            toReturn.add(child)
                        }

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

                kidsList  = toReturn
                if (kidsList.size > 0){
                    selectedPosition = 0

                    preschool_list_message.visibility = View.GONE
                    preschool_kids_list_recycler.visibility = View.VISIBLE
                    child_info_details_container.visibility = View.VISIBLE
                    progress_view.visibility = View.GONE

                    setupAdapter(kidsList)
                    val child  = kidsList.get(0)
                    preschool_details_kid_names.text = child.firstName+" "+child.lastName
                    preschool_allergies_value.text = child.allergies

                    /*
                    TODO : Call this child's parent and set the values accordingly
                     */

                }else{

                    preschool_list_message.visibility = View.VISIBLE
                    preschool_kids_list_recycler.visibility = View.GONE
                    child_info_details_container.visibility = View.GONE
                    progress_view.visibility = View.GONE

                    preschool_list_message.text = "No Child available"

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }
        childReference?.addValueEventListener(kidsListener)

        preschool_kids_list_recycler.addOnItemClickListener(object: OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {
                if (kidsList.size > 0){

                    selectedPosition = position
                    adapter.notifyDataSetChanged()

                    var child  = kidsList.get(position)
                    preschool_details_kid_names.text = child.firstName+" "+child.lastName
                    preschool_allergies_value.text = child.allergies
                }
            }
        } )

    }

    private fun setupAdapter(data : ArrayList<Child>){
        adapter = PreSchoolListAdapter(data, childReference, this){}
        preschool_kids_list_recycler.adapter = adapter
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
package apps.issy.com.oceankids

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.adapters.KidsListAdapter
import apps.issy.com.oceankids.data.Child
import com.google.firebase.database.*
import com.mcxiaoke.koi.ext.onTextChange

/**
 *  Created by issy on 23/02/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

import kotlinx.android.synthetic.main.class_list_activity.*

class KidsListActivity : BaseActivity() {

    var kidsList : ArrayList<Child> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.class_list_activity)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val linearLayoutManager = LinearLayoutManager(this)
        kids_list.layoutManager = linearLayoutManager
        kids_list.hasFixedSize()
        setupAdapter(kidsList)

        val kidsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val toReturn: ArrayList<Child> = ArrayList()
                for (data in dataSnapshot.children){
                    try {
                        val oneChild = data.getValue<Child>(Child::class.java)
                        val child = oneChild.let { it } ?: continue
                        toReturn.add(child)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

                kidsList  = toReturn
                setupAdapter(kidsList)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }
        childReference?.addValueEventListener(kidsListener)

        addchild_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                //TODO Add a pop up view to add a new child to the database
                val intent  = Intent(this@KidsListActivity, RegisterChildActivity::class.java)
                startActivity(intent)
            }
        })

        card_number_filter.onTextChange { text, start, before, count ->
//            TODO
            val res = kidsList
                    .filter { it.attendance.cardNumber.startsWith(text, ignoreCase = true) }

            val result : ArrayList<Child> = ArrayList(res)
            setupAdapter(result)

        }

        names_filter.onTextChange { text, start, before, count ->

            val res = kidsList
                    .filter { it.firstName!!.startsWith(text, ignoreCase = true) }

            val result : ArrayList<Child> = ArrayList(res)
            setupAdapter(result)

        }

    }

    private fun setupAdapter(data: ArrayList<Child>){

        kids_list.adapter = KidsListAdapter(data, childReference, this) {
            //
        }

        //scroll to bottom
        //mainActivityRecyclerView.scrollToPosition(data.size - 1)

    }

}
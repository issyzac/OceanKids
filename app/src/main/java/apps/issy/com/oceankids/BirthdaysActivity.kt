package apps.issy.com.oceankids

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.adapters.BirthdayMonthsAdapter
import apps.issy.com.oceankids.adapters.WeeklyBirthdaysAdapter
import apps.issy.com.oceankids.data.BirthdayObject
import apps.issy.com.oceankids.data.Child
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_birthdays.*
import java.util.*
import kotlin.collections.ArrayList

/**
 *  Created by issy on 22/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */
class BirthdaysActivity : BaseActivity() {

    var monthsList : ArrayList<String> =  ArrayList()
    var childrenList : ArrayList<Child> =  ArrayList()
    var adapter : BirthdayMonthsAdapter = BirthdayMonthsAdapter(monthsList){}
    var thisWeek  = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)

    companion object {
        var selectedPosition : Int = RecyclerView.NO_POSITION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthdays)

        val linearLayoutManager = LinearLayoutManager(this)
        months_recycler.layoutManager = linearLayoutManager
        months_recycler.hasFixedSize()

        val lLayoutManager = LinearLayoutManager(this)
        lLayoutManager.scrollToPositionWithOffset(thisWeek, 8)
        weekly_birthdays_recycler.layoutManager = lLayoutManager
        weekly_birthdays_recycler.hasFixedSize()


        setSupportActionBar(birthdays_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        monthsList.add("January")
        monthsList.add("February")
        monthsList.add("March")
        monthsList.add("April")
        monthsList.add("May")
        monthsList.add("June")
        monthsList.add("July")
        monthsList.add("August")
        monthsList.add("September")
        monthsList.add("October")
        monthsList.add("November")
        monthsList.add("December")

        setupAdapter(monthsList)
        selectedPosition = 0

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
                            Log.d("BirthdayActivity", "Birth Month : "+dobCal.get(Calendar.MONTH))
                        }

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

                childrenList = toReturn
                getAllBirthdaysOfMonth(selectedPosition)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }
        childReference?.addValueEventListener(kidsListener)

        months_recycler.addOnItemClickListener(object: BirthdaysActivity.OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (monthsList.size > 0){
                    selectedPosition = position
                    adapter.notifyDataSetChanged()

                    /*

                    */
                    getAllBirthdaysOfMonth(position)

                }
            }
        } )

    }

    private fun getAllBirthdaysOfMonth(position : Int){

        var monthValue = position

        var allChildrenViews : String = ""
        var wholeMonthBirthdays : ArrayList<BirthdayObject> = ArrayList()
        var weekOneChildren : ArrayList<Child> = ArrayList()
        var weekTwoChildren : ArrayList<Child> = ArrayList()
        var weekThreeChildren : ArrayList<Child> = ArrayList()
        var weekFourChildren : ArrayList<Child> = ArrayList()
        var weekFiveChildren : ArrayList<Child> = ArrayList()

        for (child in childrenList){

            var calendar : Calendar = Calendar.getInstance()
            calendar.timeInMillis = child.dob

            var month = calendar.get(Calendar.MONTH)

            if (month == position){
                when (calendar.get(Calendar.WEEK_OF_MONTH)){
                    1 -> {
                        weekOneChildren.add(child)
                    }
                    2 -> {
                        weekTwoChildren.add(child)
                    }
                    3 -> {
                        weekThreeChildren.add(child)
                    }
                    4 -> {
                        weekFourChildren.add(child)
                    }
                    5 -> {
                        weekFiveChildren.add(child)
                    }

                }

            }

        }

        var week1BirthdayObject = BirthdayObject()
        var week2BirthdayObject = BirthdayObject()
        var week3BirthdayObject = BirthdayObject()
        var week4BirthdayObject = BirthdayObject()
        var week5BirthdayObject = BirthdayObject()

        week1BirthdayObject.dateRange = "1"
        week1BirthdayObject.listOfChildren = weekOneChildren
        wholeMonthBirthdays.add(week1BirthdayObject)

        week2BirthdayObject.dateRange = "2"
        week2BirthdayObject.listOfChildren = weekTwoChildren
        wholeMonthBirthdays.add(week2BirthdayObject)

        week3BirthdayObject.dateRange = "3"
        week3BirthdayObject.listOfChildren = weekThreeChildren
        wholeMonthBirthdays.add(week3BirthdayObject)

        week4BirthdayObject.dateRange = "4"
        week4BirthdayObject.listOfChildren = weekFourChildren
        wholeMonthBirthdays.add(week4BirthdayObject)

        week5BirthdayObject.dateRange = "5"
        week5BirthdayObject.listOfChildren = weekFiveChildren
        wholeMonthBirthdays.add(week5BirthdayObject)

        var currentMonthBirthdaysAdapter = WeeklyBirthdaysAdapter(wholeMonthBirthdays)
        weekly_birthdays_recycler.adapter = currentMonthBirthdaysAdapter



        if (!allChildrenViews.isEmpty()){
            progress_view.visibility = View.GONE
            child_names.text = allChildrenViews
        }else{
            progress_view.visibility = View.GONE
            child_names.text = "No Birthdays For this Month"
        }



    }

    private fun setupAdapter(data: java.util.ArrayList<String>){

        adapter = BirthdayMonthsAdapter(data) {
            //..
        }
        months_recycler.adapter = adapter
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
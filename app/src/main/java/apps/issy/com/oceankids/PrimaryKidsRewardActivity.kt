package apps.issy.com.oceankids

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.adapters.PrimaryKidsListAdapter
import apps.issy.com.oceankids.data.Child
import apps.issy.com.oceankids.data.Parent
import com.google.firebase.database.*
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.mcxiaoke.koi.ext.asString
import com.mcxiaoke.koi.ext.dateNow
import com.mcxiaoke.koi.ext.dateParse
import com.mcxiaoke.koi.ext.onTextChange
import kotlinx.android.synthetic.main.activity_primary_kids_reward.*
import java.util.*
import kotlin.collections.ArrayList

/**
 *  Created by issy on 05/07/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class PrimaryKidsRewardActivity : BaseActivity() {

    private var totalPoints : Int = 0
    private var aggregatePoints : Int = 0
    private var tillNextCandy : Int = 0

    private var bible : Boolean? = false
    private var verse : Boolean? = false
    private var friend : Boolean? = false
    private var offering : Boolean? = false
    private var bonus : Boolean? = false
    private var attend : Boolean? = false

    private var currentChild : Child = Child()

    private val rewardReference = FirebaseDatabase.getInstance().reference.child("kids").child("reward_chart")

    private var kidsList : ArrayList<Child> = ArrayList()
    private var adapter : PrimaryKidsListAdapter = PrimaryKidsListAdapter(kidsList, childReference,this){}

    private val nowString = dateNow()
    private val date = dateParse(nowString)
    private val dateTitle = date.asString("EEE, MMM d, ''yy")
    private val dateKey = date.asString("yyyy-MM-dd")

    companion object {
        var selectedPosition : Int = RecyclerView.NO_POSITION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary_kids_reward)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        parent_name_icon.setColorFilter(resources.getColor(R.color.light_blue_400))
        parent_phone_icon.setColorFilter(resources.getColor(R.color.light_blue_400))
        attended_icon.setColorFilter(resources.getColor(R.color.white))
        bible_icon.setColorFilter(resources.getColor(R.color.white))
        verse_icon.setColorFilter(resources.getColor(R.color.white))
        offering_icon.setColorFilter(resources.getColor(R.color.white))
        brought_friend_icon.setColorFilter(resources.getColor(R.color.white))
        bonus_icon.setColorFilter(resources.getColor(R.color.white))

        val linearLayoutManager = LinearLayoutManager(this)
        primary_kids_list_recycler.layoutManager = linearLayoutManager
        primary_kids_list_recycler.hasFixedSize()

        rewardCardClicksAction()

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

                        if (childYears in 6..10){
                            toReturn.add(child)
                        }

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

                kidsList  = toReturn
                accomodateItemsListChanges(kidsList)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }
        childReference?.addListenerForSingleValueEvent(kidsListener)

        done_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                //this is supposed to save and finish the activity
                done_button.visibility = View.INVISIBLE
                save_rewards_progress_view.visibility = View.VISIBLE
                //Pass in the middleware first to check if the child's point have already been recorded
                RecordRewardMiddleware(attend, bible, verse, offering, friend, bonus, totalPoints)
                primary_kids_list_recycler.clearFocus()
                done_button.visibility = View.VISIBLE
                save_rewards_progress_view.visibility = View.INVISIBLE
            }
        })

        primary_kids_list_recycler.addOnItemClickListener(object: PrimaryKidsRewardActivity.OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (kidsList.size > 0){

                    if (currentChild != kidsList[position]){
                        //Reset View
                        resetDetailsView()
                    }

                    child_info_details_container.visibility = View.VISIBLE
                    PrimaryKidsRewardActivity.selectedPosition = position
                    adapter.notifyDataSetChanged()

                    val child  = kidsList[position]
                    currentChild = child

                    primary_details_kid_names.text = child.firstName+" "+child.lastName
                    primary_allergies_value.text = child.allergies

                    aggregatePoints = currentChild.aggregatePoints
                    tillNextCandy = 10 - (aggregatePoints % 10)
                    points_till_candy.text = tillNextCandy.toString()
                    aggregate_points.text = child.aggregatePoints.toString()

                    //Getting parent details
                    getParentDetails(child.parents)
                }
            }
        } )

        primary_kids_search_et.onTextChange { text, start, before, count ->
            val res = kidsList
                    .filter { it.firstName!!.startsWith(text, ignoreCase = true) || it.lastName!!.startsWith(text, ignoreCase = true) }
            val result : ArrayList<Child> = ArrayList(res)
            accomodateItemsListChanges(result)
        }
    }

    private fun accomodateItemsListChanges(items : ArrayList<Child>){

        if (items.size > 0){
            selectedPosition = 0

            primary_list_message.visibility = View.GONE
            primary_kids_list_recycler.visibility = View.VISIBLE
            child_info_details_container.visibility = View.VISIBLE
            progress_view.visibility = View.GONE

            setupAdapter(items)

            //Getting the first child in the list so as to display his/her information in details
            val child  = items[0]
            currentChild = child
            primary_details_kid_names.text = child.firstName+" "+child.lastName
            primary_allergies_value.text = child.allergies

            //Reward System points
            aggregatePoints = currentChild.aggregatePoints
            tillNextCandy = 10 - (aggregatePoints % 10)
            points_till_candy.text = tillNextCandy.toString()
            aggregate_points.text = child.aggregatePoints.toString()

            //Getting parent details for this child
            getParentDetails(child.parents)

        }else{

            primary_list_message.visibility = View.VISIBLE
            primary_kids_list_recycler.visibility = View.GONE
            child_info_details_container.visibility = View.GONE
            progress_view.visibility = View.GONE

            primary_list_message.text = "No Child available"

        }
    }

    /**
     * Get the details of the parent of a child from the parent Node
     *
     * receives a Child.parent object that contains
     *      relationship and Parent ID
     *
     */
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

    private fun resetDetailsView(){
        primary_details_scroll_view.fullScroll(View.FOCUS_UP)
        primary_details_scroll_view.pageScroll(View.FOCUS_UP)

        candy_wrap.visibility = View.GONE

        setViewVisibility(attended_check, false)
        setViewVisibility(bible_check, false)
        setViewVisibility(verse_check, false)
        setViewVisibility(offering_check, false)
        setViewVisibility(friend_check, false)
        setViewVisibility(bonus_check, false)
        total_points.text = ""
        points_till_candy.text = ""
        parent_name_value.text = ""
        phone_value.text = ""

        totalPoints = 0
        aggregatePoints = 0
        tillNextCandy = 0

    }

    private fun rewardCardClicksAction(){

        attendance_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                attend = true
                addPoints(1)
                setViewVisibility(attended_check, true)
            }
        })
        attended_check.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                attend = false
                minusPoints(1)
                setViewVisibility(attended_check, false)
            }
        })

        has_bible_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                bible = true
                addPoints(1)
                setViewVisibility(bible_check, true)
            }
        })
        bible_check.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                bible = false
                minusPoints(1)
                setViewVisibility(bible_check, false)
            }
        })

        knows_memory_verse_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                verse = true
                addPoints(2)
                setViewVisibility(verse_check, true)
            }
        })
        verse_check.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                verse = false
                minusPoints(2)
                setViewVisibility(verse_check, false)
            }
        })

        has_offering_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                offering = true
                addPoints(1)
                setViewVisibility(offering_check, true)
            }
        })
        offering_check.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                offering = false
                minusPoints(1)
                setViewVisibility(offering_check, false)
            }
        })

        brought_a_friend_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                friend = true
                addPoints(3)
                setViewVisibility(friend_check, true)
            }
        })
        friend_check.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                friend = false
                minusPoints(3)
                setViewVisibility(friend_check, false)
            }
        })

        bonus_card.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                bonus = true
                addPoints(1)
                setViewVisibility(bonus_check, true)
            }
        })
        bonus_check.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                bonus = false
                minusPoints(1)
                setViewVisibility(bonus_check, false)
            }
        })

    }

    fun addPoints(points : Int){
        totalPoints += points
        total_points.text = totalPoints.toString()

        if ((totalPoints - tillNextCandy) >= 0){ //TODO Remove 10 add limit depending on years
            candy_wrap.visibility = View.VISIBLE
        }
    }

    fun minusPoints(points: Int){
        totalPoints -= points
        total_points.text = totalPoints.toString()

        if ((totalPoints - tillNextCandy) < 0){ //TODO Remove 10 add limit depending on years
            candy_wrap.visibility = View.GONE
        }
    }

    private fun setViewVisibility(checkView : View, visible : Boolean){
        if (visible)
            checkView.visibility = View.VISIBLE
        else
            checkView.visibility = View.GONE
    }

    fun RecordRewardMiddleware (
            hasAttended : Boolean?,
            hasABible : Boolean?,
            knowsMemoryVerse : Boolean?,
            hasOffering : Boolean?,
            broughtAFriend : Boolean?,
            gotBonus : Boolean?,
            points: Int? ){
        val childRewardReference : DatabaseReference = rewardReference.child(dateKey).child(currentChild.id)
        val childRewardValueEventListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.getValue(true) != null){
                    //Data already exist
                    /*
                    TODO Change this to custom dialog
                    val alertSaved = AlertView("Not Saved, \nReward Already recorded for today", "", AlertStyle.DIALOG)
                    alertSaved.addAction(AlertAction("Cancel", AlertActionStyle.DEFAULT, { action ->  }))
                    alertSaved.show(this@PrimaryKidsRewardActivity)
                    */

                }else{
                    //Data does not exist
                    recordChart(hasAttended, hasABible, knowsMemoryVerse, hasOffering, broughtAFriend, gotBonus, points)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }

        }
        childRewardReference.addListenerForSingleValueEvent(childRewardValueEventListener)

    }

    fun recordChart(
            hasAttended : Boolean?,
            hasABible : Boolean?,
            knowsMemoryVerse : Boolean?,
            hasOffering : Boolean?,
            broughtAFriend : Boolean?,
            gotBonus : Boolean?,
            points: Int?){
        rewardReference.child(dateKey).child(currentChild.id).child("attended").setValue(hasAttended)
        rewardReference.child(dateKey).child(currentChild.id).child("bonus").setValue(gotBonus)
        rewardReference.child(dateKey).child(currentChild.id).child("brought_a_friend").setValue(broughtAFriend)
        rewardReference.child(dateKey).child(currentChild.id).child("has_bible").setValue(hasABible)
        rewardReference.child(dateKey).child(currentChild.id).child("has_offering").setValue(hasOffering)
        rewardReference.child(dateKey).child(currentChild.id).child("knows_memory_verse").setValue(knowsMemoryVerse)
        rewardReference.child(dateKey).child(currentChild.id).child("todays_points").setValue(points)

        aggregatePoints += totalPoints
        childReference!!.child(currentChild.id).child("aggregatePoints").setValue(aggregatePoints)

        //Display the aggregate points
        aggregate_points.text = aggregatePoints.toString()

        /*
        TODO Change this to custom dialog
        val alertSaved = AlertView("Rewards Saved", "", AlertStyle.DIALOG)
        alertSaved.addAction(AlertAction("OK", AlertActionStyle.DEFAULT, { action ->  }))
        alertSaved.show(this@PrimaryKidsRewardActivity)
        */

    }

    private fun setupAdapter(data : ArrayList<Child>){
        adapter = PrimaryKidsListAdapter(data, childReference, this){}
        primary_kids_list_recycler.adapter = adapter
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }

}
package apps.issy.com.oceankids

import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.data.Kid
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.mcxiaoke.koi.ext.asString
import com.mcxiaoke.koi.ext.dateNow
import com.mcxiaoke.koi.ext.dateParse

/**
 *  Created by issy on 03/03/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

import kotlinx.android.synthetic.main.child_particulars_capture.*

class ChildDetailsActivity : BaseActivity() {

    var totalPoints : Int = 0
    var aggregatePoints : Int = 0
    var tillNextCandy : Int = 0

    private var kidId : String = String()
    private val childRef = FirebaseDatabase.getInstance().reference.child("kids").child("kids_list")
    private val rewardReference = FirebaseDatabase.getInstance().reference.child("kids").child("reward_chart")

    //Reward Indicators
    var bible : Boolean? = false
    var verse : Boolean? = false
    var friend : Boolean? = false
    var offering : Boolean? = false
    var bonus : Boolean? = false
    var attend : Boolean? = false

    val nowString = dateNow()
    val date = dateParse(nowString)
    val dateTitle = date.asString("EEE, MMM d, ''yy")
    val dateKey = date.asString("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.child_particulars_capture)

        kidId = intent.extras.getString("kid_id")
        date_title.text = dateTitle

        attended_icon.setColorFilter(resources.getColor(R.color.white))
        bible_icon.setColorFilter(resources.getColor(R.color.white))
        verse_icon.setColorFilter(resources.getColor(R.color.white))
        offering_icon.setColorFilter(resources.getColor(R.color.white))
        brought_friend_icon.setColorFilter(resources.getColor(R.color.white))
        bonus_icon.setColorFilter(resources.getColor(R.color.white))

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

        val aKidListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val aKid = dataSnapshot.getValue<Kid>(Kid::class.java)
                aggregatePoints = aKid!!.aggregatePoints
                child_names.text = aKid.names
                aggregate_points.text = aKid.aggregatePoints.toString()

                tillNextCandy = 10 - (aggregatePoints % 10)
                points_till_candy.text = tillNextCandy.toString()

            }
            override fun onCancelled(p0: DatabaseError) {}
        }

        val referenceOfAKid = childRef.child(kidId+"")
        referenceOfAKid.addValueEventListener(aKidListener)

        done_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                //this is supposed to save and finish the activity
                recordChart(attend, bible, verse, offering, friend, bonus, totalPoints)
                finish()
            }
        })

        /*profile_arrow_right.setColorFilter(resources.getColor(R.color.teal_400))
        prayer_arrow_right.setColorFilter(resources.getColor(R.color.teal_400))*/

        profile_container.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                prayer_points_container.visibility = GONE
                profile_contents_container.visibility = VISIBLE
                prayer_arrow_right.visibility = GONE
                profile_arrow_right.visibility = VISIBLE
                profile_container.setBackgroundColor(resources.getColor(R.color.card_separator))
                prayer_container.setBackgroundColor(resources.getColor(android.R.color.transparent))
            }
        })

        prayer_container.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                prayer_points_container.visibility = VISIBLE
                profile_contents_container.visibility = GONE
                prayer_arrow_right.visibility = VISIBLE
                profile_arrow_right.visibility = GONE
                prayer_container.setBackgroundColor(resources.getColor(R.color.card_separator))
                profile_container.setBackgroundColor(resources.getColor(android.R.color.transparent))
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

    fun recordChart(
            hasAttended : Boolean?,
            hasABible : Boolean?,
            knowsMemoryVerse : Boolean?,
            hasOffering : Boolean?,
            broughtAFriend : Boolean?,
            gotBonus : Boolean?,
            points: Int?){
        rewardReference.child(dateKey).child(kidId).child("attended").setValue(hasAttended)
        rewardReference.child(dateKey).child(kidId).child("bonus").setValue(gotBonus)
        rewardReference.child(dateKey).child(kidId).child("brought_a_friend").setValue(broughtAFriend)
        rewardReference.child(dateKey).child(kidId).child("has_bible").setValue(hasABible)
        rewardReference.child(dateKey).child(kidId).child("has_offering").setValue(hasOffering)
        rewardReference.child(dateKey).child(kidId).child("knows_memory_verse").setValue(knowsMemoryVerse)
        rewardReference.child(dateKey).child(kidId).child("todays_points").setValue(points)

        aggregatePoints += totalPoints
        childRef.child(kidId).child("aggregatePoints").setValue(aggregatePoints)

    }

    private fun setViewVisibility(checkView : View, visible : Boolean){
        if (visible)
            checkView.visibility = VISIBLE
        else
            checkView.visibility = GONE
    }

}
package apps.issy.com.oceankids.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import apps.issy.com.oceankids.Base.BaseActivity
import apps.issy.com.oceankids.Base.Endpoints
import apps.issy.com.oceankids.data.Attendance
import apps.issy.com.oceankids.data.Parent
import apps.issy.com.oceankids.database.daos.KidDao
import apps.issy.com.oceankids.database.entities.Kid
import apps.issy.com.oceankids.util.Constants
import com.androidhuman.rxfirebase2.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class KidsRepository(private val kidDao: KidDao){

    val allKids : LiveData<List<Kid>> = kidDao.getAllKidsLiveData()
    val nurseryKids : LiveData<List<Kid>> = kidDao.getChildrenByClass(BaseActivity.fromNurseryAge, BaseActivity.toNurseryAge)
    val preSchoolKids : LiveData<List<Kid>> = kidDao.getChildrenByClass(BaseActivity.fromPreSchoolAge, BaseActivity.toPreSchoolAge)
    val primaryKids : LiveData<List<Kid>> = kidDao.getChildrenByClass(BaseActivity.fromPrimaryAge, BaseActivity.toPrimaryAge)

    val checkedInKids : LiveData<List<Kid>> = kidDao.getAllCheckedInKids(1)

    // @WorkerThread annotation marks that this method needs to be called from a non-UI thread
    // suspend modifier tells the compiler that this needs to be called from a coroutine or another suspending function.
    @WorkerThread
    suspend fun insert(kid : Kid){
        kidDao.InsertKid(kid)
    }
    @WorkerThread
    suspend fun insertNewKid(kid : Kid?){
        kidDao.InsertKid(kid)
    }
    @WorkerThread
    suspend fun updateKid(kid : Kid?){
        kidDao.updateChild(kid)
        Log.d("child_event_changes", "Child Updated!!")
    }

    fun insertWithoutSuspending(kid : Kid){
        kidDao.InsertKid(kid)
    }

    @SuppressLint("CheckResult")
    suspend fun  loadAllKids(userRole : Int){

        /**
         * If
         *  @userRole == 1 -> Nursery Class
         *  @userRole == 2 -> Preschool Class
         *  @userRole == 3 -> Primary Class
         */

        val kidsList : ArrayList<Kid> = ArrayList()

        val range = getRangeBasedOnRole(userRole)

        val reference = FirebaseDatabase.getInstance()
                .reference
                .child("kids")
                .child("kids_list")
                .orderByChild("dob")
                .startAt(range.first.toDouble())
                .endAt(range.second.toDouble())


        reference.data()
                .subscribe({
                    if (it.exists()) {
                        for (data in it.children) {

                            val id = data.key
                            val firstName = data.child("firstName").value
                            val lastName = data.child("lastName").value
                            val nationality = data.child("nationality").value
                            val dob = data.child("dob").value
                            val allergies = data.child("allergies").value
                            val gender = data.child("gender").value
                            val checkedIn = data.child("checkedIn").value

                            //parents= {0={relationship=Mother, id=-LPVy0XuL02yjX7M-1Pm}},
                            val parentId = data.child("parents").child("0").child("id").value
                            val parentRelationship = data.child("parents").child("0").child("relationship").value

                            //attendance={checkedIn=0, service=0, cardNumber=},
                            val attendanceCardNumber = data.child("attendance").child("cardNumber").value
                            val attendanceCheckedIn = data.child("attendance").child("checkedIn").value
                            val attendanceService = data.child("attendance").child("service").value

                            val child = Kid()

                            child.id = id.toString()
                            child.firstName = firstName.toString()
                            child.lastName = lastName.toString()
                            child.allergies = allergies.toString()
                            child.checkedIn = checkedIn.toString()
                            child.gender = gender.toString()
                            child.nationality = nationality.toString()

                            if (dob != null)
                                child.dob = dob.toString().toLong()
                            else
                                child.dob = 0

                            val attendance : Kid.Atendance = Kid.Atendance()
                            attendance.service = attendanceService.toString()
                            attendance.cardNumber = attendanceCardNumber.toString()
                            attendance.checkedIn = attendanceCheckedIn.toString()

                            val parent: Kid.Parent = Kid.Parent()
                            parent.id = parentId.toString()
                            parent.relationship = parentRelationship.toString()

                            val parents = ArrayList<Kid.Parent>()
                            parents.add(parent)

                            child.attendance = attendance
                            child.parents = parents

                            kidsList.add(child)

                            /*
                            val currentChild: Kid = data.getValue<Kid>(Kid::class.java).let {
                                it
                            } ?: continue //Continue with the looping if this child is null

                            currentChild.id = data.key.toString()
                            kidsList.add(currentChild)
                             */
                        }

                        GlobalScope.launch(IO){
                            for (kid in kidsList){
                                insertWithoutSuspending(kid)
                            }
                        }

                    }
                }){
                    Log.e("ASFORME", it.message)

                }

        reference.childEvents()
                .ofType(ChildAddEvent::class.java)
                .ofType(ChildChangeEvent::class.java)
                .subscribe({
                    if (it.dataSnapshot().exists()){
                        for (data in it.dataSnapshot().children){
                           val kid = data.getValue<Kid>(Kid::class.java).let {
                               it
                           } ?: continue

                            Log.d("spectacular", "Changed kid is : "+kid.firstName)
                        }
                    }
                }){
                    //Throw Errors
                    Log.d("spectacular", "Error : "+it.message)
                }
    }

    fun getRangeBasedOnRole(role: Int) : Pair<Long, Long> {

        val calendar = Calendar.getInstance()
        val todayInMilliseconds = calendar.timeInMillis

        var start : Long  = 0
        var end : Long = 0

        if (role == 3){
            //Role is primary class
            start = todayInMilliseconds - Constants.CALENDAR.TWELVE_YEARS_AGO
            end = todayInMilliseconds - Constants.CALENDAR.SIX_YEARS_AGO
        }else {
            //Role is preschool and Nursery checkin
            start = todayInMilliseconds - Constants.CALENDAR.SIX_YEARS_AGO
            end = todayInMilliseconds
        }

        return Pair(start, end)
    }

    fun insertAttendanceData(kid : Kid) {

        kidDao.updateChild(kid)

        //This is the attendance object that is attached with the date and child ID
        val attendance = Attendance()
        attendance.cardNumber = kid.attendance.cardNumber
        attendance.checkedOut = 0

        val kidAttendance = kid.attendance

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)+1
        val year = calendar.get(Calendar.YEAR)
        val todaysDateKey = day.toString()+"-"+month+"-"+year

        val thisChildRef = FirebaseDatabase.getInstance().getReference("kids")
                .child("kids_list")
                .child(kid.id)
                .child("attendance")

        val attendanceReference = FirebaseDatabase.getInstance().getReference("attendance")
                .child(todaysDateKey)
                .child(kid.attendance.service.toString())
                .child(kid.id)

        thisChildRef.rxSetValue(kidAttendance)
                .subscribe({
                    // Update successful
                }) {
                    //Error
                }

        attendanceReference.rxSetValue(attendance)
                .subscribe ({
                    //Successfully
                }){
                    //Error
                }

        val childReference = FirebaseDatabase.getInstance().getReference("kids")
                .child("kids_list")
                .child(kid.id)
        val childReferenceUpdateValue = mapOf("checkedIn" to 1)
        childReference.rxUpdateChildren(childReferenceUpdateValue)
                .subscribe({
                    //Updated
                }){
                    //Failed
                }

    }

    fun checkoutChildAttendance(kid: Kid){

        val kidService = kid.attendance.service

        kid.attendance.cardNumber = ""
        kid.attendance.service = "0"
        kid.attendance.checkedIn = "0"
        kid.checkedIn = "0"

        //Update local DB
        kidDao.updateChild(kid)

        //Comment this if you do not want the app to send text messages to parents after checkout
        //sendCheckoutText(kid)

        //Update Child node on Firebase to change child's attendance to checked out
        val thisChildRef = FirebaseDatabase.getInstance().getReference("kids")
                .child("kids_list")
                .child(kid.id)
                .child("attendance")

        val kidAttendanceUpdate = mapOf(
                "cardNumber" to "",
                "checkedIn" to 0,
                "service" to 0
        )

        thisChildRef.rxUpdateChildren(kidAttendanceUpdate)
                .subscribe({
                    //Child attendance updated successfully
                }){
                    //Error Updating child attendance
                }

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)+1
        val year = calendar.get(Calendar.YEAR)
        val todaysDateKey = day.toString()+"-"+month+"-"+year

        //Update firebase attendance node to record a child being checked out
        val attendanceReference = FirebaseDatabase.getInstance().getReference("attendance")
                .child(todaysDateKey)
                .child(kidService.toString())
                .child(kid.id)

        //Timestamp object to record time the child was checked out
        val time = Calendar.getInstance().timeInMillis

        val attendanceUpdate = mapOf(
                "checkedOut" to 1,
                "timeOut" to time
        )

        attendanceReference.rxUpdateChildren(attendanceUpdate)
                .subscribe({
                    //Attendance Updated
                }){
                    //Error updating attendance
                }

        val childReference = FirebaseDatabase.getInstance().getReference("kids")
                .child("kids_list")
                .child(kid.id)
        val childReferenceUpdateValue = mapOf("checkedIn" to 0)
        childReference.rxUpdateChildren(childReferenceUpdateValue)
                .subscribe({
                    //Updated
                }){
                    //Failed
                }

    }

    fun sendCheckoutText(kid : Kid){

        var gender = ""
        var phoneNo = ""

        when (kid.gender){
            Constants.genderMale -> gender = "him"
            Constants.genderFemale -> gender = "her"
        }

        val reference : DatabaseReference = FirebaseDatabase.getInstance()
                .reference
                .child("parents")
                .child(kid.parents.get(0).id)

        reference.data()
                .subscribe({
                    if (it.exists()){
                        val parent: Parent? = it.getValue<Parent>(Parent::class.java)
                        val phoneNumber = parent!!.phoneNumber

                        sendText(phoneNumber, gender, kid)

                    }
                }){

                }

    }

    private fun sendText(phone : String, gender : String, kid : Kid){

        val client = OkHttpClient().newBuilder()
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.baseURL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val smsService = retrofit.create(Endpoints::class.java)

        var body: RequestBody
        var datastream = ""
        val jsonObject = JSONObject()
        val jsonArray = JSONArray()

        try {
            jsonObject.put("from", "OCEAN KIDS")
            jsonObject.put("phone_number", phone)
            jsonObject.put("message", kid.firstName+" has been checked out. \nThank you for churching with us today, we wish you a great Sunday.\nSee you next week!" )
            jsonObject.put("device_id", "108776")

            jsonArray.put(jsonObject)

            datastream = jsonArray.toString()

            Log.d("PostOfficeService", datastream)

            body = RequestBody.create(MediaType.parse("application/json"), datastream)

        }catch (e : java.lang.Exception){
            e.printStackTrace()
            body = RequestBody.create(MediaType.parse("application/json"), datastream)
        }

        val call = smsService.sendSms(Constants.apiKey, body)

        GlobalScope.launch {

            call.enqueue(object : Callback<Any> {

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    Log.d("Coffee", "Response is  : "+response.body().toString())
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Log.d("Coffee", "Error : "+t.message)
                }

            })
        }

    }

    fun updateChildDetails(kid: Kid, parent: Parent?){

        //Update Child locally
        kidDao.updateChild(kid)

        //Update Child details on the server
        val thisChildRef = FirebaseDatabase.getInstance().getReference("kids")
                .child("kids_list")
                .child(kid.id)

        val kidUpdate = mapOf(
                "firstName" to kid.firstName,
                "lastName" to kid.lastName,
                "gender" to kid.gender,
                "allergies" to kid.allergies,
                "nationality" to kid.nationality,
                "dob" to kid.dob)

        thisChildRef.rxUpdateChildren(kidUpdate)
                .subscribe({
                    //Updated successfully
                }){
                    //Error updating
                }

        val currentParentReference = FirebaseDatabase.getInstance().getReference("parents").child(parent!!.id)

        val parentUpdate = mapOf(
                "firstName" to parent!!.firstName,
                "lastName" to parent.lastName,
                "phoneNumber" to parent.phoneNumber,
                "relationshipToChild" to parent.relationshipToChild,
                "email" to parent.email)

        currentParentReference.rxUpdateChildren(parentUpdate)
                .subscribe({
                    //Parent updated successfully
                }){
                    //Error updating
                }

    }

}
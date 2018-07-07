package apps.issy.com.oceankids.data

import com.google.firebase.database.IgnoreExtraProperties

/**
 *  Created by issy on 03/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

@IgnoreExtraProperties
class Child {

    var id : String  = ""
    var firstName : String = ""
    var lastName : String = ""
    var gender : String = ""
    var nationality : String = ""
    var dob : Long = 0

    /**
     *  1 -> Nursery
     *  2 -> Pre-School
     *  3 -> Primary
     *  4 -> Pre-Teens
     */
    var level : String = ""

    var attendance : Atendance = Atendance()

    var aggregatePoints : Int = 0

    var parents : ArrayList<Parent> = ArrayList()
    var allergies : String = ""

    constructor(){
        //Default constructor for Firebase
    }

    class Allergies{

        var description : String = ""

    }

    class Parent{

        //Parent ID in the parents Node
        var id : String = ""

        //Relationship of the parent with the child
        var relationship : String = ""

    }

    /**
     * Check-in/Check-out
     */
    class Atendance{
        var cardNumber : String = ""
        var service : Int = 0
        var checkedIn : Int = 0
    }

}
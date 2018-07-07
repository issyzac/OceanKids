package apps.issy.com.oceankids.data

import com.google.firebase.database.IgnoreExtraProperties

/**
 *  Created by issy on 23/02/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

@IgnoreExtraProperties
class Kid {
    var id : String? = ""
    var names : String? = ""
    var cardNumber : String? = ""
    var aggregatePoints : Int = 0
    var dob : Long? = 0

    /*
    0 - Nursery
    1 - Pre School
    2 - Primary
    3 - Pre teen
     */
    var gradeLevel : Int? = null
    var checkedIn : Int? = null
    /*
    M - Male
    F - Female
     */
    var gender : String? = ""

    var parentPhoneNumber : String? = ""

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Kid.class)
    }

    constructor(names: String?, cardNo : String?, dob : Long?, gLevel : Int?, gender : String?, phoneNumber : String?, isCheckedIn : Int?) {
        this.names = names
        this.cardNumber = cardNo
        this.dob = dob
        this.gradeLevel = gLevel
        this.gender = gender
        this.parentPhoneNumber = phoneNumber
        this.checkedIn = isCheckedIn
    }

}
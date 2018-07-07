package apps.issy.com.oceankids.data

import com.google.firebase.database.IgnoreExtraProperties

/**
 *  Created by issy on 02/04/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

@IgnoreExtraProperties
class KidsRequests{

    var kid_uuid : String? = ""

    var prayers : ArrayList<String>? = ArrayList()

    var thanks_giving : ArrayList<String>? = ArrayList()

    constructor(){
    }

    constructor(_kid_uuid : String?, _prayers : ArrayList<String>?, _thanks_giving : ArrayList<String>? ){
        this.kid_uuid = _kid_uuid
        this.prayers = _prayers
        this.thanks_giving = _thanks_giving
    }

}
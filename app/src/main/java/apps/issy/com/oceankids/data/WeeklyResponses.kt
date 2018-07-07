package apps.issy.com.oceankids.data

import com.google.firebase.database.IgnoreExtraProperties

/**
 *  Created by issy on 02/04/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

@IgnoreExtraProperties
class WeeklyResponses{

    var date : Long? = 0

    var kro : ArrayList<KidsRequests> = ArrayList()

    constructor(){}

    constructor(_date : Long?, _kro : ArrayList<KidsRequests>){
        this.date = _date
        this.kro = _kro
    }

}
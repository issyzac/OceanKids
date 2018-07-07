package apps.issy.com.oceankids.data

/**
 *  Created by issy on 02/04/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class SingleRequestItem{

    var kidsName : String? = ""

    var kidsReruest : String? = ""

    constructor(){}

    constructor(_kidsName : String?, _kidsRequest : String?){
        this.kidsName = _kidsName
        this.kidsReruest = _kidsRequest
    }
}
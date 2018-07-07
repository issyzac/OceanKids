package apps.issy.com.oceankids.data

import com.google.firebase.database.IgnoreExtraProperties

/**
 *  Created by issy on 02/04/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

@IgnoreExtraProperties
class Content{

    var id : String? = ""

    var content : String? = ""

    constructor(){}

    constructor(_content : String?){
        this.content = _content
    }

}
package apps.issy.com.oceankids

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import apps.issy.com.oceankids.Base.BaseActivity

/**
 *  Created by issy on 12/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */
 class PrimaryKidsActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.primary_kids_activity)

    }

    fun loadRewardChart(view : View) {
        Log.d("tag", "Loading reward chart")
        val intent = Intent(this@PrimaryKidsActivity, PrimaryKidsRewardActivity::class.java)
        startActivity(intent)
    }

}
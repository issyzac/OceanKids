package apps.issy.com.oceankids

import android.content.Intent
import android.os.Bundle
import android.view.View
import apps.issy.com.oceankids.Base.BaseActivity
import kotlinx.android.synthetic.main.activity_preschool_home.*

/**
 *  Created by issy on 26/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class PreSchoolActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preschool_home)

        setSupportActionBar(preschool_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        preschool_child_info_button_container.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(this@PreSchoolActivity, PreSchoolInfoActivity::class.java)
                //intent.putExtra(KEY, VALUE)
                startActivity(intent)
            }
        })

        preschool_birthdays_button_container.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(this@PreSchoolActivity, PreSchoolBirthdayActivity::class.java)
                //intent.putExtra(KEY, VALUE)
                startActivity(intent)
            }
        })

    }

}
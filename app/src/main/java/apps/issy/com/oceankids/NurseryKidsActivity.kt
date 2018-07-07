package apps.issy.com.oceankids

import android.os.Bundle
import android.view.View
import apps.issy.com.oceankids.Base.BaseActivity
import kotlinx.android.synthetic.main.activity_nursery_home.*
import org.jetbrains.anko.startActivity

/**
 *  Created by issy on 22/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */
class NurseryKidsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nursery_home)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        child_info_button_container.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                p0?.context?.startActivity<NuseryKidsInfoActivity>()
            }
        })

        birthdays_button_container.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                p0?.context?.startActivity<BirthdaysActivity>()
            }
        })

    }

}
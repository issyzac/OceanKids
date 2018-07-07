package apps.issy.com.oceankids.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import apps.issy.com.oceankids.fragments.BirthdaysTabFragment
import apps.issy.com.oceankids.fragments.DashboardTabFragment
import apps.issy.com.oceankids.fragments.PrayersTabFragment

/**
 *  Created by issy on 31/03/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class TabPagerAdapter(fm: FragmentManager, private var tabCount: Int) :
        FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return DashboardTabFragment()
            1 -> return PrayersTabFragment()
            2 -> return BirthdaysTabFragment()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}
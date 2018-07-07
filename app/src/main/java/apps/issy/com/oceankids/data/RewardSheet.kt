package apps.issy.com.oceankids.data

/**
 *  Created by issy on 23/02/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

data class RewardSheet (
        val rewardDate : Long,
        val kidId : String,
        val hasBible : Boolean,
        val hasAttended : Boolean,
        val knowsMemoryVerse : Boolean,
        val hasOffering : Boolean,
        val broughtAFriend : Boolean )
package apps.issy.com.oceankids.util

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import apps.issy.com.oceankids.database.entities.Kid

class KidsDiffCallback (private val mOldKidList: List<Kid>, private val mNewKidList: List<Kid>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldKidList.size
    }

    override fun getNewListSize(): Int {
        return mNewKidList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldKidList[oldItemPosition].id === mNewKidList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldKid = mOldKidList[oldItemPosition]
        val newKid = mNewKidList[newItemPosition]

        return ( oldKid == newKid)
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}
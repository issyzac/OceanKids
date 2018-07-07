package apps.issy.com.oceankids.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import apps.issy.com.oceankids.R

/**
 *  Created by issy on 10/03/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class SpinnerAdapter(internal var act: Context, resource: Int, internal var items: List<String>) : ArrayAdapter<String>(act, resource, items) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        val vi = act.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        rowView = vi.inflate(R.layout.single_text_spinner_item_drop_down, null)

        val tvTitle : TextView = rowView!!.findViewById(R.id.rowtext)
        tvTitle.text = items[position]

        return rowView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        val vi = act.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        rowView = vi.inflate(R.layout.single_text_spinner_view_item, null)

        val tvTitle : TextView = rowView!!.findViewById(R.id.rowtext)
        tvTitle.text = items[position]

        return rowView

    }
}
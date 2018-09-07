package com.example.happy.filter.Holders

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.happy.filter.R
import com.unnamed.b.atv.model.TreeNode
import kotlinx.android.synthetic.main.single_parent_item.view.*

class TitleViewHolder(context: Context) : TreeNode.BaseNodeViewHolder<String>(context) {
    var arrowIcon : ImageView? = null

    override fun createNodeView(node: TreeNode?, value: String?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.single_parent_item, null, false)
        arrowIcon = view.findViewById(R.id.icon)
        view.checkbox.visibility = View.GONE
        view.name_of_agent.text = value
        return  view
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun toggle(active: Boolean) {
        if(active)
            arrowIcon!!.setImageDrawable(context.getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp))
        else
            arrowIcon!!.setImageDrawable(context.getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp))
    }
}
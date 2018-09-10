package com.example.happy.filter.Treeview.Holder

import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.happy.filter.Treeview.Model.TreeNode

class SimpleViewHolder(context: Context) : TreeNode.BaseNodeViewHolder<Any>(context) {
    override fun createNodeView(node: TreeNode?, value: Any?): View? {
        val tv = TextView(context)
        tv.text = value.toString()
        return tv
    }


    override fun toggle(active: Boolean) {

    }
}
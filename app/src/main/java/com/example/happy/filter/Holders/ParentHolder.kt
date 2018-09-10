package com.example.happy.filter.Holders

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import com.example.happy.filter.MainActivity
import com.example.happy.filter.R
import com.example.happy.filter.TreeView.Model.TreeNode
import com.example.happy.filter.models.BA_Roles_Response
import com.example.happy.filter.models.DummyData
import com.example.happy.filter.models.RolesAgent
import com.google.gson.Gson
import kotlinx.android.synthetic.main.single_parent_item.view.*
import rx.Single
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ParentHolder(context: Context) : TreeNode.BaseNodeViewHolder<RolesAgent>(context) {
    var arrowIcon: ImageView? = null
    var mActivity = context as MainActivity
    override fun createNodeView(node: TreeNode?, value: RolesAgent?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.single_parent_item, null, false)
        arrowIcon = view.findViewById(R.id.icon)
        view.name_of_agent.text = value!!.name + " ( id = ${value.userId} )"
        view.checkbox.setOnClickListener(View.OnClickListener {
            onCheckBoxClick(node!!, view.checkbox.isChecked)
        })

        view.checkbox.setOnCheckedChangeListener { compoundButton, b ->
            onCheckBoxStateChange(node!!, b)
        }

        node!!.setClickListener { clickedNode, clickedNodeValue ->
            //adding agents as children if parent node having has Agents value true
            var nodeData = clickedNodeValue as RolesAgent
            if (!(clickedNode!!.children.size > 0)) {
                if (clickedNode.children.size == 0 && nodeData.hasAgents)
                    getAgents(clickedNode.level, value.mobile, clickedNode)
            }
        }
        return view
    }

    private fun onCheckBoxStateChange(clickedCheckBoxNode: TreeNode, isChecked: Boolean) {

        //Adding Each Checked Value to ArrayList
        var nodeValue = clickedCheckBoxNode.value as RolesAgent
        if(isChecked)
            mActivity.addIdToArrayList(nodeValue.userId.toInt())
        else
            mActivity.removeIdFromArrayList(nodeValue.userId.toInt())

        // Changing State of It's Children According to Parent
        if (clickedCheckBoxNode.children.size > 0)
            changeCheckBoxStateOfChildren(clickedCheckBoxNode, isChecked)

        // when parent is selected (Removing its siblings id's from ArrayList and Adding Its Parent Id to ArrayList)
        if (clickedCheckBoxNode.parent.viewHolder.view.findViewById<CheckBox>(R.id.checkbox).isChecked )
            removeSiblingsFromArrayList(clickedCheckBoxNode)

        if(isAllSiblingsSelected(clickedCheckBoxNode)){
            removeSiblingsFromArrayList(clickedCheckBoxNode)
        }

        if(clickedCheckBoxNode.parent.viewHolder.view.findViewById<CheckBox>(R.id.checkbox).isChecked){
            if(!isChecked){
                if(isAllChildSelected(clickedCheckBoxNode)){
                   clickedCheckBoxNode.viewHolder.view.findViewById<CheckBox>(R.id.checkbox).isChecked = true
                    mActivity.removeIdFromArrayList(nodeValue.userId.toInt())
                }
            }
        }

    }

    private fun onCheckBoxClick(clickedCheckBoxNode: TreeNode, isChecked: Boolean) {

        //when parent is selected and all children going to be checked
        if (clickedCheckBoxNode.parent.viewHolder.view.findViewById<CheckBox>(R.id.checkbox).isChecked) {
            changeCheckBoxStateOfChildren(clickedCheckBoxNode, true)
            changeCheckBoxStateOfSiblings(clickedCheckBoxNode,true)
        }


        // when all siblings are selected and parent is not selected
        if (isChecked && isAllSiblingsSelected(clickedCheckBoxNode))
            clickedCheckBoxNode.parent.viewHolder.view.findViewById<CheckBox>(R.id.checkbox).isChecked = true


        //when the hierarchy of  checkboxes needs to be checked or unchecked
        if (clickedCheckBoxNode.children.size > 0)
            changeCheckBoxStateOfChildren(clickedCheckBoxNode, isChecked)

    }

    fun changeCheckBoxStateOfChildren(node: TreeNode, isChecked: Boolean) {

       // Changing State of It's Children According to Parent
        for (i in 0 until node.children.size)
            node.children[i].viewHolder.view.findViewById<CheckBox>(R.id.checkbox).isChecked = isChecked

    }

    fun changeCheckBoxStateOfSiblings(node: TreeNode, isChecked: Boolean){
        // Changing State of It's Sibling According to Parent
        for (i in 0 until node.parent.children.size)
            node.parent.children[i].viewHolder.view.findViewById<CheckBox>(R.id.checkbox).isChecked = isChecked

    }

    fun removeSiblingsFromArrayList(node: TreeNode) {

        //removing children id from ArrayList
        for (i in 0 until node.parent.children.size) {
            var nodeValue = node.parent.children[i].value as RolesAgent
            mActivity.removeIdFromArrayList(nodeValue.userId.toInt())
        }

        //Adding parent id to ArrayList
        var nodeParentValue = node.parent.value as RolesAgent


       // mActivity.addIdToArrayList(nodeParentValue.userId.toInt())

    }

    fun isAllSiblingsSelected(node: TreeNode): Boolean {
        var totalSiblingCount = node.parent.children.size
        var totalCheckedSibiling = 0
        for (i in 0 until node.parent.children.size) {
            if (node.parent.children[i].viewHolder.view.findViewById<CheckBox>(R.id.checkbox).isChecked) {
                totalCheckedSibiling++
            }
        }
        return totalCheckedSibiling == totalSiblingCount
    }

    fun isAllChildSelected(node: TreeNode) : Boolean{
        var totalChildCount = node.children.size
        var totalCheckedChild = 0
        for (i in 0 until node.children.size) {
            if (node.children[i].viewHolder.view.findViewById<CheckBox>(R.id.checkbox).isChecked) {
                totalCheckedChild++
            }
        }
        return totalCheckedChild == totalChildCount
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun toggle(active: Boolean) {
        if (active)
            arrowIcon!!.setImageDrawable(context.getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp))
        else
            arrowIcon!!.setImageDrawable(context.getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp))
    }

    fun getAgents(level: Int, mobile: String, selectedNode: TreeNode): Subscription {
        return Single.fromCallable<BA_Roles_Response>({ this.loadData(level) })
                .subscribeOn(Schedulers.computation())
                .doOnError({ it.printStackTrace() })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    this.onGettingResponse(it, selectedNode)
                })
    }

    private fun onGettingResponse(response: BA_Roles_Response?, selectedNode: TreeNode) {

        for (i in 0..response!!.roles!!.children.size - 1) {
            treeView.addNode(selectedNode, TreeNode(response.roles!!.children[i]))
        }

        //if its parent node is  already checked then checking all the children checkboxes
        if (selectedNode.parent.viewHolder.view.findViewById<CheckBox>(R.id.checkbox).isChecked) {
            changeCheckBoxStateOfChildren(selectedNode,true)
        }

    }

    fun loadData(level: Int): BA_Roles_Response {
        return when (level) {

            1 -> Gson().fromJson(DummyData().agentFirstLevel, BA_Roles_Response::class.java)
            2 -> Gson().fromJson(DummyData().agentSecondLevel, BA_Roles_Response::class.java)
            3 -> Gson().fromJson(DummyData().agentThirdLevel, BA_Roles_Response::class.java)
            4 -> Gson().fromJson(DummyData().agentLevelFour, BA_Roles_Response::class.java)

            else -> {
                Gson().fromJson(DummyData().agentLevelFour, BA_Roles_Response::class.java)
            }
        }
    }


}
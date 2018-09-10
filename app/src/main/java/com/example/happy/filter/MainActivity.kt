package com.example.happy.filter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.example.happy.filter.Holders.ParentHolder
import com.example.happy.filter.Holders.TitleViewHolder
import com.example.happy.filter.Treeview.Model.TreeNode
import com.example.happy.filter.Treeview.view.AndroidTreeView
import com.example.happy.filter.models.BA_Roles_Response
import com.example.happy.filter.models.DummyData
import com.example.happy.filter.models.RolesAgent
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import rx.Single
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    var root = TreeNode.root()
    var containerView : RelativeLayout? = null
    var agentsList = ArrayList<RolesAgent>()
    var treeNodeList = ArrayList<TreeNode>()
    var currentLevel = 1
    var androidTreeView : AndroidTreeView? = null
    var rootLevelNode : TreeNode? = null
    val selectedIds = ArrayList<String>()
    var mapAgentDetails = ArrayList<HashMap<Int,String>>()
    var recyclerView : RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        containerView = findViewById<RelativeLayout>(R.id.container)
        recyclerView  = findViewById(R.id.recyclerview)
        recyclerView!!.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        get_ids.setOnClickListener(View.OnClickListener {
            setRecylerView()
        })
        getAgents(1)
        if(savedInstanceState!=null){
            val state = savedInstanceState.getString("tState")
            if (!TextUtils.isEmpty(state)) {
                androidTreeView!!.restoreState(state)
            }
        }
    }

     fun loadData(level :Int): BA_Roles_Response {
        return when(level){

            1 -> Gson().fromJson(DummyData().agentFirstLevel, BA_Roles_Response::class.java)
            2 -> Gson().fromJson(DummyData().agentSecondLevel, BA_Roles_Response::class.java)
            3 -> Gson().fromJson(DummyData().agentThirdLevel, BA_Roles_Response::class.java)
            4 -> Gson().fromJson(DummyData().agentLevelFour, BA_Roles_Response::class.java)

            else -> {
                Gson().fromJson(DummyData().agentLevelFour, BA_Roles_Response::class.java)
            }
        }
    }

     fun getAgents(level: Int): Subscription {
        return Single.fromCallable<BA_Roles_Response>({ this.loadData(level) })
                .subscribeOn(Schedulers.computation())
                .doOnError({ it.printStackTrace() })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    this.onGettingResponse(it) })
    }

    private fun onGettingResponse(response: BA_Roles_Response) {
        rootLevelNode = TreeNode("My Team").setViewHolder(TitleViewHolder(this))

        for (i in 0 until response.roles!!.children.size){
            treeNodeList.add(TreeNode(response.roles!!.children[i]).setViewHolder(ParentHolder(this)))
        }
        rootLevelNode!!.addChildren(treeNodeList)
        root.addChild(rootLevelNode!!)
        androidTreeView = AndroidTreeView(this, root)
        androidTreeView!!.setDefaultViewHolder(ParentHolder::class.java)
        androidTreeView!!.setUse2dScroll(true)
        androidTreeView!!.setDefaultContainerStyle(R.style.TreeNodeStyle)
        androidTreeView!!.saveState
        containerView!!.addView(androidTreeView!!.view)

    }


    fun addIdToArrayList(userId: Int){
        if(!checkAlreadyExist(userId))
            selectedIds.add(userId.toString())
    }

    fun removeIdFromArrayList(userId: Int){
        selectedIds.remove(userId.toString())
    }

    fun checkAlreadyExist(userId: Int) : Boolean{
       return selectedIds.contains(userId.toString())
    }

    fun setRecylerView(){
        recyclerView!!.adapter =  CustomAdapter(this,selectedIds)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("tState", androidTreeView!!.saveState)
    }



}

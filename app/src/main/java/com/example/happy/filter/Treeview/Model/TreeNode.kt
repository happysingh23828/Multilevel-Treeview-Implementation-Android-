package com.example.happy.filter.Treeview.Model

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.example.happy.filter.R
import com.example.happy.filter.Treeview.view.AndroidTreeView
import com.example.happy.filter.Treeview.view.TreeNodeWrapperView

import java.util.ArrayList
import java.util.Collections


class TreeNode(val value: Any?) {

    var id: Int = 0
        private set
    private var mLastId: Int = 0
    var parent: TreeNode? = null
        private set
    var isSelected: Boolean = false
        get() = isSelectable && field
    var isSelectable = true
    internal val children: MutableList<TreeNode>
    private var mViewHolder: BaseNodeViewHolder<*>? = null
    private var mClickListener: TreeNodeClickListener? = null
    private var mLongClickListener: TreeNodeLongClickListener? = null
    private var mExpanded: Boolean = false

    val isLeaf: Boolean
        get() = size() == 0

    val path: String
        get() {
            val path = StringBuilder()
            var node: TreeNode? = this
            while (node!!.parent != null) {
                path.append(node.id)
                node = node.parent
                if (node!!.parent != null) {
                    path.append(NODES_ID_SEPARATOR)
                }
            }
            return path.toString()
        }


    val level: Int
        get() {
            var level = 0
            var root: TreeNode? = this
            while (root!!.parent != null) {
                root = root.parent
                level++
            }
            return level
        }

    val isLastChild: Boolean
        get() {
            if (!isRoot) {
                val parentSize = parent!!.children.size
                if (parentSize > 0) {
                    val parentChildren = parent!!.children
                    return parentChildren[parentSize - 1].id == id
                }
            }
            return false
        }

    val isFirstChild: Boolean
        get() {
            if (!isRoot) {
                val parentChildren = parent!!.children
                return parentChildren[0].id == id
            }
            return false
        }

    val isRoot: Boolean
        get() = parent == null

    val root: TreeNode
        get() {
            var root: TreeNode? = this
            while (root!!.parent != null) {
                root = root.parent
            }
            return root
        }

    private fun generateId(): Int {
        return ++mLastId
    }

    init {
        children = ArrayList()
    }

    fun addChild(childNode: TreeNode): TreeNode {
        childNode.parent = this
        childNode.id = generateId()
        children.add(childNode)
        return this
    }

    fun addChildren(vararg nodes: TreeNode): TreeNode {
        for (n in nodes) {
            addChild(n)
        }
        return this
    }

    fun addChildren(nodes: Collection<TreeNode>): TreeNode {
        for (n in nodes) {
            addChild(n)
        }
        return this
    }

    fun deleteChild(child: TreeNode): Int {
        for (i in children.indices) {
            if (child.id == children[i].id) {
                children.removeAt(i)
                return i
            }
        }
        return -1
    }

    fun getChildren(): List<TreeNode> {
        return Collections.unmodifiableList(children)
    }

    fun size(): Int {
        return children.size
    }

    fun isExpanded(): Boolean {
        return mExpanded
    }

    fun setExpanded(expanded: Boolean): TreeNode {
        mExpanded = expanded
        return this
    }

    fun setViewHolder(viewHolder: BaseNodeViewHolder<*>?): TreeNode {
        mViewHolder = viewHolder
        if (viewHolder != null) {
            viewHolder.mNode = this
        }
        return this
    }

    fun setClickListener(listener: TreeNodeClickListener): TreeNode {
        mClickListener = listener
        return this
    }

    fun getClickListener(): TreeNodeClickListener? {
        return this.mClickListener
    }

    fun setLongClickListener(listener: TreeNodeLongClickListener): TreeNode {
        mLongClickListener = listener
        return this
    }

    fun getLongClickListener(): TreeNodeLongClickListener? {
        return mLongClickListener
    }

    fun getViewHolder(): BaseNodeViewHolder<*>? {
        return mViewHolder
    }

    interface TreeNodeClickListener {
        fun onClick(node: TreeNode, value: Any)
    }

    interface TreeNodeLongClickListener {
        fun onLongClick(node: TreeNode, value: Any): Boolean
    }


    abstract class BaseNodeViewHolder<E>(protected var context: Context) {
        var treeView: AndroidTreeView? = null
            protected set
        var mNode: TreeNode? = null
        private var mView: View? = null
        var containerStyle: Int = 0

        val view: View
            get() {
                if (mView != null) {
                    return mView as View
                }
                val nodeView = nodeView
                val nodeWrapperView = TreeNodeWrapperView(nodeView?.context, containerStyle)
                nodeWrapperView.insertNodeView(nodeView!!)
                mView = nodeWrapperView

                return mView as TreeNodeWrapperView
            }

        val nodeView: View?
            get() = createNodeView(mNode, mNode!!.value as E)

        val nodeItemsView: ViewGroup
            get() = view.findViewById<View>(R.id.node_items) as ViewGroup

        val isInitialized: Boolean
            get() = mView != null

        fun setTreeViev(treeViev: AndroidTreeView) {
            this.treeView = treeViev
        }


        abstract fun createNodeView(node: TreeNode?, value: E?): View?

        open fun toggle(active: Boolean) {
            // empty
        }

        fun toggleSelectionMode(editModeEnabled: Boolean) {
            // empty
        }
    }

    companion object {
        val NODES_ID_SEPARATOR = ":"

        fun root(): TreeNode {
            val root = TreeNode(null)
            root.isSelectable = false
            return root
        }
    }
}

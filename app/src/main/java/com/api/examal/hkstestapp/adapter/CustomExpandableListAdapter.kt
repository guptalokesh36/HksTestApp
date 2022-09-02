package com.api.examal.hkstestapp.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.api.examal.hkstestapp.PostModel
import com.api.examal.hkstestapp.R
import com.api.examal.hkstestapp.custom.NonScrollListView

class CustomExpandableListAdapter(
    private val context: Context,
    private val titleList: List<PostModel>,
    private val dataList: HashMap<String, MutableList<PostModel>>
) : BaseExpandableListAdapter() {

    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition].mID
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {

        var convertView = convertView
        val listTitle = getGroup(listPosition).toString()
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listView)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = "Mid: $listTitle"
        return convertView
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): List<PostModel> {
        val list =
            this.dataList[this.titleList[listPosition].mID]     // Get the list from map using mID
        val map = list!!.groupBy { it.tID }     // Map of tID as key list as value
        return map[list.toSet().toList()[expandedListPosition].tID]!!
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val child = getChild(listPosition, expandedListPosition)
        if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.child_list_item, null)
        }
        val tID = convertView!!.findViewById<TextView>(R.id.tid)
        val list: NonScrollListView = convertView.findViewById(R.id.child_list)

        tID.text = "Tid: ${child[expandedListPosition].tID}"
        val newList = child.map { item -> item.narration + "   -> " + item.amount }
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, newList)
        list.adapter = adapter

        return convertView
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.titleList[listPosition].mID]!!.groupBy { it.tID }.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}
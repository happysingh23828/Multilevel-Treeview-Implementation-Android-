package com.example.happy.filter.models

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class RolesParent protected constructor(`in`: Parcel) : Parcelable {


    @SerializedName("group_cumulative")
    var groupCumulative: List<AccumulativeStats>

    @SerializedName("identity")
    var identity: String

    @SerializedName("name")
    var name: String

    @SerializedName("node_status")
    var nodeStatus: String

    @SerializedName("parent_id")
    lateinit var parentId: Any

    @SerializedName("title")
    var designation: String

    @SerializedName("user_id")
    var userId: Int = 0

    @SerializedName("city")
    var city: String? = null


    init {
        groupCumulative = `in`.createTypedArrayList(AccumulativeStats.CREATOR)
        identity = `in`.readString()
        name = `in`.readString()
        nodeStatus = `in`.readString()
        designation = `in`.readString()
        userId = `in`.readInt()
        city = `in`.readString()
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(groupCumulative)
        dest.writeString(identity)
        dest.writeString(name)
        dest.writeString(nodeStatus)
        dest.writeString(designation)
        dest.writeInt(userId)
        dest.writeString(city)
    }

    companion object {

        val CREATOR: Parcelable.Creator<RolesParent> = object : Parcelable.Creator<RolesParent> {
            override fun createFromParcel(`in`: Parcel): RolesParent {
                return RolesParent(`in`)
            }

            override fun newArray(size: Int): Array<RolesParent?> {
                return arrayOfNulls(size)
            }
        }
    }
}

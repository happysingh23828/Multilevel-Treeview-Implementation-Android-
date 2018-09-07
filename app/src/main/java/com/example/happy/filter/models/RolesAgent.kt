package com.example.happy.filter.models

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class RolesAgent protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName("credit_approved")
    var creditApproved: Int = 0

    @SerializedName("group_cumulative")
    var groupCumulative: List<AccumulativeStats>

    @SerializedName("has_agent")
    var hasAgents: Boolean = false

    @SerializedName("identity")
    var mobile: String
        private set

    @SerializedName("name")
    var name: String

    @SerializedName("node_status")
    var agentStatus: String

    @SerializedName("parent_id")
    var parentId: String

    @SerializedName("payment_completed")
    var paymentCompleted: String

    @SerializedName("title")
    var designation: String

    @SerializedName("total_users")
    var totalUsers: String

    @SerializedName("user_id")
    var userId: String

    @SerializedName("city")
    var city: String


    init {
        creditApproved = `in`.readInt()
        groupCumulative = `in`.createTypedArrayList(AccumulativeStats.CREATOR)
        hasAgents = `in`.readByte().toInt() != 0
        mobile = `in`.readString()
        name = `in`.readString()
        agentStatus = `in`.readString()
        parentId = `in`.readString()
        paymentCompleted = `in`.readString()
        designation = `in`.readString()
        totalUsers = `in`.readString()
        userId = `in`.readString()
        city = `in`.readString()
    }

    fun setIdentity(identity: String) {
        this.mobile = identity
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(creditApproved)
        dest.writeTypedList(groupCumulative)
        dest.writeByte((if (hasAgents) 1 else 0).toByte())
        dest.writeString(mobile)
        dest.writeString(name)
        dest.writeString(agentStatus)
        dest.writeString(parentId)
        dest.writeString(paymentCompleted)
        dest.writeString(designation)
        dest.writeString(totalUsers)
        dest.writeString(userId)
        dest.writeString(city)
    }

    companion object {

        val CREATOR: Parcelable.Creator<RolesAgent> = object : Parcelable.Creator<RolesAgent> {
            override fun createFromParcel(`in`: Parcel): RolesAgent {
                return RolesAgent(`in`)
            }

            override fun newArray(size: Int): Array<RolesAgent?> {
                return arrayOfNulls(size)
            }
        }
    }
}


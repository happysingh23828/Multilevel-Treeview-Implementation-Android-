package com.example.happy.filter.models

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class RolesData protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName("children")
    var children: List<RolesAgent>

    @SerializedName("parent")
    var parent: RolesParent

    init {
        children = `in`.createTypedArrayList(RolesAgent.CREATOR)
        parent = `in`.readParcelable(RolesParent::class.java.classLoader)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(children)
        dest.writeParcelable(parent, flags)
    }

    companion object {

        val CREATOR: Parcelable.Creator<RolesData> = object : Parcelable.Creator<RolesData> {
            override fun createFromParcel(`in`: Parcel): RolesData {
                return RolesData(`in`)
            }

            override fun newArray(size: Int): Array<RolesData?> {
                return arrayOfNulls(size)
            }
        }
    }
}

package com.example.happy.filter.models

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class BA_Roles_Response protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName("data")
    var roles: RolesData? = null

    @SerializedName("result")
    var result: String? = null

    @SerializedName("message")
    var message: String? = null


    init {
        roles = `in`.readParcelable(RolesData::class.java.classLoader)
        result = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(roles, flags)
        dest.writeString(result)
    }

    companion object {

        val CREATOR: Parcelable.Creator<BA_Roles_Response> = object : Parcelable.Creator<BA_Roles_Response> {
            override fun createFromParcel(`in`: Parcel): BA_Roles_Response {
                return BA_Roles_Response(`in`)
            }

            override fun newArray(size: Int): Array<BA_Roles_Response?> {
                return arrayOfNulls(size)
            }
        }
    }
}

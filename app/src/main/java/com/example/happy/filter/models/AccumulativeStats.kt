package com.example.happy.filter.models

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class AccumulativeStats protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName("title")
    var title: String? = null

    @SerializedName("value")
    var value: Int = 0

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeInt(value)
    }

    init {
        title = `in`.readString()
        value = `in`.readInt()
    }

    companion object {

        val CREATOR: Parcelable.Creator<AccumulativeStats> = object : Parcelable.Creator<AccumulativeStats> {
            override fun createFromParcel(`in`: Parcel): AccumulativeStats {
                return AccumulativeStats(`in`)
            }

            override fun newArray(size: Int): Array<AccumulativeStats?> {
                return arrayOfNulls(size)
            }
        }
    }

}

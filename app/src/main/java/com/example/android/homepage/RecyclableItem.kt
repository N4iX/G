package com.example.android.homepage

import android.os.Parcel
import android.os.Parcelable

class RecyclableItem (
    val dataKey:String,
    var itemName: String,
    var itemImage: String,
    var itemInfoLink: String
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {}

    constructor():this("", "", "", ""){}

    fun setDetails(itemName: String, itemImage: String, itemInfoLink: String){
        this.itemName = itemName
        this.itemImage = itemImage
        this.itemInfoLink = itemInfoLink
    }


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(dataKey)
        dest.writeString(itemName)
        dest.writeString(itemImage)
        dest.writeString(itemInfoLink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecyclableItem> {
        override fun createFromParcel(parcel: Parcel): RecyclableItem {
            return RecyclableItem(parcel)
        }

        override fun newArray(size: Int): Array<RecyclableItem?> {
            return arrayOfNulls(size)
        }
    }

}
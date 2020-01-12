package com.example.android.homepage.ui.information_centre

import com.google.firebase.database.Exclude

class RecyclableItem {
    var itemName: String? = null
    var itemImage: String? = null
    var itemInfoLink: String? = null

    constructor(){
        //Default constructor
    }

    constructor(itemName: String?, itemImage: String?, itemInfoLink: String?){
        this.itemName = itemName
        this.itemImage = itemImage
        this.itemInfoLink = itemInfoLink
    }

    @Exclude
    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("itemName", itemName!!)
        result.put("itemImage", itemImage!!)
        result.put("itemInfoLink", itemInfoLink!!)

        return result
    }
}
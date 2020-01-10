package com.example.android.homepage.ui.information_centre

import com.google.firebase.database.Exclude

class RecyclableItem {
    var itemName: String? = null
    var itemImage: String ? = null

    constructor(){
        //Default constructor
    }

    constructor(itemName: String?, itemImage: String?){
        this.itemName = itemName
        this.itemImage = itemImage
    }

    @Exclude
    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("itemName", itemName!!)
        result.put("itemImage", itemImage!!)

        return result
    }
}
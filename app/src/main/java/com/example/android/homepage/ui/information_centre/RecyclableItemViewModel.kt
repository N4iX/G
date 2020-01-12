package com.example.android.homepage.ui.information_centre

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.example.android.homepage.RecyclableItem
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecyclableItemViewModel(application: Application) : AndroidViewModel(application) {

    val recyclableItemList: MutableLiveData<List<RecyclableItem>> = MutableLiveData()
    val database = FirebaseDatabase.getInstance()
    val recyclableItemRef = database.getReference("recyclableItem")

    init{
        val list = mutableListOf<RecyclableItem>()

        recyclableItemRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(i: DataSnapshot in p0.children){
                        val recyclableItem = i.getValue((RecyclableItem::class.java))
                        list.add(recyclableItem!!)
                    }
                }
                recyclableItemList.value = list
            }
        })
    }
}
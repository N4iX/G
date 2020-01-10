package com.example.android.homepage.ui.information_centre
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.android.homepage.R
//
//class RecyclerAdapter (var recyclableItems: MutableList<RecyclableItem>): RecyclerView.Adapter<RecyclableItemViewHolder>(){
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclableItemViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclable_item_view, parent, false)
//        return RecyclableItemViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return recyclableItems.size
//    }
//
////    override fun onBindViewHolder(holder: RecyclableItemViewHolder, position: Int) {
////        val recyclableItem = recyclableItems.get(position)
////        holder.bindRecyclableItem(recyclableItem.itemName, recyclableItem.itemImage, itemCl)
////    }
//}
package com.example.android.homepage.ui.information_centre

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.homepage.R
import com.example.android.homepage.RecyclableItem
import com.squareup.picasso.Picasso

class RecyclableItemAdapter internal constructor(context: Context):
RecyclerView.Adapter<RecyclableItemAdapter.RecyclableItemViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var recyclableItemList = emptyList<RecyclableItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclableItemViewHolder {
        val itemView = inflater.inflate(R.layout.recyclable_item_view, parent, false)
        return RecyclableItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return recyclableItemList.size
    }

    override fun onBindViewHolder(holder: RecyclableItemViewHolder, position: Int) {
        val recyclableItem = recyclableItemList[position]

        holder.textViewRecyclableItemName.text = recyclableItem.itemName
        Picasso.get().load(recyclableItem.itemImage).into(holder.imageViewRecyclableItem)
        holder.textViewItemInfoLink.text = recyclableItem.itemInfoLink

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(InformationCentreFragmentDirections.
                actionNavigationInformationCentreToDisplayItemInfoFragment(recyclableItem.itemName, recyclableItem.itemInfoLink))
        }
    }

    fun setRecyclableItemList(recyclableItemList: List<RecyclableItem>){
        this.recyclableItemList = recyclableItemList
        notifyDataSetChanged()
    }

    inner class RecyclableItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var textViewRecyclableItemName: TextView = itemView.findViewById(R.id.textViewRecyclableItemName)
        var imageViewRecyclableItem: ImageView = itemView.findViewById(R.id.imageViewRecyclableItem)
        var textViewItemInfoLink: TextView = itemView.findViewById(R.id.textViewItemInfoLink)
    }
}
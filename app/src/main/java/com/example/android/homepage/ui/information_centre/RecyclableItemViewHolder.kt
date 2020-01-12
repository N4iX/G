package com.example.android.homepage.ui.information_centre

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclable_item_view.view.*

class RecyclableItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    fun bindRecyclableItem(recyclableItem: RecyclableItem?){
        with(recyclableItem!!){
            itemView.textViewRecyclableItemName.text = itemName
            itemView.textViewItemInfoLink.text = itemInfoLink
            Picasso.get().load(itemImage).into(itemView.imageViewRecyclableItem)
        }
    }

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val itemName = v?.textViewRecyclableItemName?.text.toString()
        val itemInfoLink = v?.textViewItemInfoLink?.text.toString()
        v?.findNavController()?.navigate(InformationCentreFragmentDirections.
            actionNavigationInformationCentreToDisplayItemInfoFragment(itemName, itemInfoLink))
    }
}
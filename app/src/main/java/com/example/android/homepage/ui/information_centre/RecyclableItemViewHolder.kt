package com.example.android.homepage.ui.information_centre

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.homepage.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclable_item_view.view.*

class RecyclableItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    fun bindRecyclableItem(recyclableItem: RecyclableItem?){
        with(recyclableItem!!){
            itemView.textViewRecyclableItemName.text = itemName
            Picasso.get().load(itemImage).into(itemView.imageViewRecyclableItem)
        }
    }

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //Log.d("RecyclerView", v?.textViewRecyclableItemName?.text.toString())
        //val intent = Intent(itemView.context, DisplayItemInfoActivity::class.java)
        //startActivityForResult(intent, REQUEST_CODE)
        //v?.findNavController()?.navigate(R.id.action_navigation_information_centre_to_displayItemInfoActivity)
//        val myIntent = Intent(itemView.context, DisplayItemInfoActivity::class.java)
//        itemView.context!!.startActivity(myIntent)
        val intent = Intent(itemView.context, DisplayItemInfoActivity::class.java)
        intent.putExtra("itemName", v?.textViewRecyclableItemName?.text.toString())
        itemView.context.startActivity(intent)
    }

    companion object{
        const val REQUEST_CODE = 1
    }


}
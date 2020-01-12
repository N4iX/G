package com.example.android.homepage.ui.news_and_event.ManageEvent

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.homepage.R
import com.example.android.homepage.ui.news_and_event.Event
import kotlinx.android.synthetic.main.event_layout.view.*
import java.text.SimpleDateFormat

class EditEventViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

    fun bindEventEdit(event: Event?){
        with(event!!){
            itemView.textViewTitleContent.text = title
            itemView.textViewDescriptionContent.text = description
            itemView.textViewDateContent.text = date
            itemView.textViewDataKey.text = dataKey
        }
    }

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        val itemTitle = v?.textViewTitleContent?.text.toString()
        val itemDescription = v?.textViewDescriptionContent?.text.toString()
        val itemDate = v?.textViewDateContent?.text.toString()
        val itemDataKey = v?.textViewDataKey?.text.toString()

        v!!.findNavController().navigate(EditEventRVFragmentDirections.actionEditEventRVFragmentToEditEventFragment(itemTitle,itemDate,itemDescription,itemDataKey))
    }

    companion object{
        const val REQUEST_CODE = 1
    }
}
package com.example.android.homepage.ui.news_and_event.ManageNews

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.homepage.R
import com.example.android.homepage.ui.news_and_event.News
import kotlinx.android.synthetic.main.fragment_edit_news.view.*
import kotlinx.android.synthetic.main.news_layout.view.*
import java.text.SimpleDateFormat

class EditNewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

    private var dateTaken: Long? = null
    fun bindNewsEdit(news: News?){
        with(news!!){
            itemView.textViewLink.text = link
            itemView.textViewTitle.text = title
            itemView.textViewDate.text = SimpleDateFormat("dd.MM.yy").format(date)
            itemView.textViewDataKey.text = dataKey
            dateTaken = date
        }
    }

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        val itemTitle = v?.textViewTitle?.text.toString()
        val itemLink = v?.textViewLink?.text.toString()
        val itemDataKey = v?.textViewDataKey?.text.toString()
        val itemDate = dateTaken

        v!!.findNavController().navigate(EditNewsRVFragmentDirections.actionEditNewsRVFragmentToEditNewsFragment(itemTitle,itemLink,itemDataKey,itemDate!!))
    }

    companion object{
        const val REQUEST_CODE = 1
    }
}
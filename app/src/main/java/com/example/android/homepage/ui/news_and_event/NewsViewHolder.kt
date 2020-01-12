package com.example.android.homepage.ui.news_and_event

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.android.homepage.ui.news_and_event.News
import kotlinx.android.synthetic.main.news_layout.view.*
import java.text.SimpleDateFormat

class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

    fun bindNews(news: News?){
        with(news!!){
            itemView.textViewLink.text = link
            itemView.textViewTitle.text = title
            itemView.textViewDate.text = SimpleDateFormat("dd.MM.yy").format(date)
            itemView.textViewDataKey.text = dataKey
        }
    }

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        val uriUrl = Uri.parse(itemView.textViewLink.text.toString())
        val browserIntent = Intent(Intent.ACTION_VIEW,uriUrl)
        itemView.context.startActivity(browserIntent)
    }

    companion object{
        const val REQUEST_CODE = 1
    }
}
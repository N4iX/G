package com.example.android.homepage.ui.news_and_event

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_layout.view.*


class EventViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

    fun bindEvent(event: Event?){
        with(event!!){
            //Picasso.get().load(eventImage).into(itemView.imageViewEvent)
            itemView.textViewDescriptionContent.text = description
            itemView.textViewTitleContent.text = title
            itemView.textViewDateContent.text = date
        }
    }

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        /*val uriUrl = Uri.parse(itemView.textViewDescription.text.toString())
        val browserIntent = Intent(Intent.ACTION_VIEW,uriUrl)
        itemView.context.startActivity(browserIntent)*/
        val title = v?.textViewTitle?.text.toString()
        val date = v?.textViewDate?.text.toString()
        val description = v?.textViewDescription?.text.toString()

        //v?.findNavController()?.navigate(FragmentEventDirections.actionFragmentEventToEventDetails())
    }


    companion object{
        const val REQUEST_CODE = 1
    }
}
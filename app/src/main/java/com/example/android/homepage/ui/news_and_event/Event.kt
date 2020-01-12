package com.example.android.homepage.ui.news_and_event

import com.google.firebase.database.Exclude
import kotlinx.android.synthetic.main.fragment_add_news.*
import java.util.*

class Event{
    var title:String? = null
    var description:String? = null
    var date:String? = null
    var dataKey:String? = null
    constructor(){
        //Default constructor
    }

    constructor(title:String?, description:String?, date:String?){
        this.title=title
        this.description = description
        this.date=date
    }

    @Exclude
    fun toMap(): Map<String,Any>{
        val result = HashMap<String, Any>()
        //result.put("eventImage",eventImage!!)
        result.put("title",title!!)
        result.put("description",description!!)
        result.put("date",date!!)

        return result
    }
}
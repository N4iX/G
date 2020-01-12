package com.example.android.homepage.ui.news_and_event

import com.google.firebase.database.Exclude
import kotlinx.android.synthetic.main.fragment_add_news.*
import java.util.*

class News{
    var dataKey: String? = null
    var title:String? = null
    var link:String? = null
    var date:Long = System.currentTimeMillis()
    constructor(){
        //Default constructor
    }

    constructor(title:String?, link:String?, date:Long){
        this.title=title
        this.link=link
        this.date=date
    }

    @Exclude
    fun toMap(): Map<String,Any>{
        val result = HashMap<String, Any>()
        result.put("title",title!!)
        result.put("link",link!!)
        result.put("date",date)
        return result
    }
}
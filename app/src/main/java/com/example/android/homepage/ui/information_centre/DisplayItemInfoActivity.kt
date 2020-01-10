package com.example.android.homepage.ui.information_centre

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.homepage.R
import kotlinx.android.synthetic.main.recyclable_item_view.*

class DisplayItemInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_item_info)

        val intent = intent
        val itemName = intent.getStringExtra("itemName")
        setTitle(itemName)
    }
}

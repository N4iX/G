package com.example.android.homepage

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.homepage.R.id.navigation_location
import com.example.android.homepage.ui.information_centre.RecyclableItem
import com.example.android.homepage.ui.information_centre.RecyclableItemViewHolder
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.firebase.ui.database.FirebaseRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val REQUIRED = "Required"
    //access database table
    private var recyclableItemDatabase: DatabaseReference? = null
    //to get the current database pointer
    private var recyclableItemReference: DatabaseReference? = null
    private var recyclableItemListener: ChildEventListener? = null

    //no need
    private var recyclableItemAdapter: FirebaseRecyclerAdapter<RecyclableItem, RecyclableItemViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_location, R.id.navigation_information_centre,R.id.navigation_news_and_event
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        fab.setOnClickListener {
//            addNewRecyclableItem()
//        }
    }

//    private fun addNewRecyclableItem(){
//        val itemName = editTextItemName.text.toString()
//        val imageLink = editTextItemImageString.text.toString()
//
//        val recyclableItem = RecyclableItem(itemName
//            ,imageLink)
//
//        val recyclableItemValues = recyclableItem.toMap()
//        val childUpdates = HashMap<String, Any>()
//
//        val key = recyclableItemDatabase!!.child("recyclableItem").push().key
//
//        childUpdates.put("/recyclableItem/" + key, recyclableItemValues)
//
//        recyclableItemDatabase!!.updateChildren(childUpdates)
//    }
}

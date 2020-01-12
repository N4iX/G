package com.example.android.homepage

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.homepage.ui.information_centre.InformationCentreFragment
import com.example.android.homepage.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),
    InformationCentreFragment.OnFragmentInteractionListener,
    ProfileFragment.OnFragmentInteractionListener{

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

    //for fragment argument passing
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_profile,
                R.id.navigation_location,
                R.id.navigation_information_centre,
                R.id.navigation_news_and_event
            )
        )
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}

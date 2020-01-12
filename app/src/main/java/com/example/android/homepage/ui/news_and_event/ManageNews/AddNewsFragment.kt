package com.example.android.homepage.ui.news_and_event.ManageNews


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.android.homepage.MainActivity
import com.example.android.homepage.R
import com.example.android.homepage.ui.news_and_event.News
import com.example.android.homepage.ui.news_and_event.NewsViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_news.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class AddNewsFragment : Fragment() {

    private val TAG = "AddNewsFragment"
    private val REQUIRED = "Required"
    //access database table
    private var newsDatabase: DatabaseReference? = null//change
    //to get the current database pointer
    private var newsReference: DatabaseReference? = null//change
    private var newsListener: ChildEventListener? = null//change
    private var newsAdapter: FirebaseRecyclerAdapter<News, NewsViewHolder>? = null

    lateinit var notificationManager : NotificationManager //manage notification
    lateinit var notificationChannel : NotificationChannel //to allow user to enable/disable notification type
    lateinit var builder : Notification.Builder
    private val channelId = "com.example.android.homepage"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //must have to add data into database
        //to get the root folder
        newsDatabase = FirebaseDatabase.getInstance().reference//change
        //to access to the table
        newsReference = FirebaseDatabase.getInstance().getReference("news")

        val view = inflater.inflate(R.layout.fragment_add_news, container, false)
        val btnSubmit: Button = view.findViewById(R.id.buttonSubmit)
        notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        btnSubmit.setOnClickListener {
            //validation on input
            if(TextUtils.isEmpty(editTextTitle.text.toString())){
                editTextTitle.error = getString(R.string.required)
                Toast.makeText(context, "Title is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(editTextLink.text.toString())){
                editTextLink.error = getString(R.string.required)
                Toast.makeText(context, "Link is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            addNews()

            val intent = Intent(context,
                MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT)

            if(Build.VERSION.SDK_INT >= 0/*Build.VERSION_CODES.0*/){
            notificationChannel = NotificationChannel(channelId,"Green Urth",NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context,channelId)
                //.setContentTitle("Green Urth")
                .setContentText("Latest news have been updated. Check it out!")
                .setSmallIcon(R.drawable.ic_gu_logo)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                    R.drawable.ic_gu_logo
                ))
                .setContentIntent(pendingIntent)
            }else{
                builder = Notification.Builder(context,channelId)
                   // .setContentTitle("Green Urth")
                    .setContentText("Latest news have been updated. Check it out!")
                    .setSmallIcon(R.drawable.ic_gu_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                        R.drawable.ic_gu_logo
                    ))
                    .setContentIntent(pendingIntent)
            }
            //unique id
            notificationManager.notify(1,builder.build())
        }

        return view
      //  return inflater.inflate(R.layout.fragment_add_news, container, false)
    }

    private fun addNews(){
        val title = editTextTitle.text.toString()
        val link= editTextLink.text.toString()
        val date = System.currentTimeMillis()

        val news = News(
            title,
            link,
            date
        )

        val newsValues = news.toMap()
        val childUpdates = HashMap<String,Any>()
        val key = newsDatabase!!.child("news").push().key
        childUpdates.put("/news/" + key, newsValues)
        newsDatabase!!.updateChildren(childUpdates)
        Toast.makeText(context, "News updated", Toast.LENGTH_SHORT).show()
        editTextTitle.setText("")
        editTextLink.setText("")
        view?.findNavController()?.navigate(R.id.fragmentNews)
    }

    /*private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }*/
}

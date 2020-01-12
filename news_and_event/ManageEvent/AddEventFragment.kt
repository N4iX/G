package com.example.android.homepage.ui.news_and_event.ManageEvent


import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.homepage.MainActivity
import com.example.android.homepage.R
import com.example.android.homepage.ui.news_and_event.Event
import com.example.android.homepage.ui.news_and_event.EventViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_add_event.*
import kotlinx.android.synthetic.main.fragment_add_news.editTextTitle
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class AddEventFragment : Fragment() {

    private val TAG = "AddEventFragment"
    private val REQUIRED = "Required"
    //access database table
    private var eventDatabase: DatabaseReference? = null//change
    //to get the current database pointer
    private var eventReference: DatabaseReference? = null//change
    private var eventListener: ChildEventListener? = null//change
    private var eventAdapter: FirebaseRecyclerAdapter<Event, EventViewHolder>? = null

    //for image
    private var filePath: Uri? = null
    private var uploadFileName: String? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    lateinit var notificationManager : NotificationManager //manage notification
    lateinit var notificationChannel : NotificationChannel //to allow user to enable/disable notification type
    lateinit var builder : Notification.Builder
    private val channelId = "com.example.android.homepage"

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.size > 0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
            pickImageFromGallery()
        }
        else{
            Toast.makeText(context,"Permissions denied",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //must have to add data into database
        //to get the root folder
        eventDatabase = FirebaseDatabase.getInstance().reference//change
        //to access to the table
        eventReference = FirebaseDatabase.getInstance().getReference("event")

        val view = inflater.inflate(R.layout.fragment_add_event, container, false)
        val btnSubmit: Button = view.findViewById(R.id.buttonSubmit)
        //val imgUpload:ImageButton = view.findViewById(R.id.imageButtonEvent)
        notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        /*imgUpload.setOnClickListener {
           if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
               //what is require context
               if(checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                       PackageManager.PERMISSION_DENIED) {
                   val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                   pickImageFromGallery()
               }else{
                   pickImageFromGallery()
               }
           }
        }*/

        btnSubmit.setOnClickListener {
            //validation on input
            if(TextUtils.isEmpty(editTextTitle.text.toString())){
                editTextTitle.error = getString(R.string.required)
                Toast.makeText(context, "Title is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(editTextDescription.text.toString())){
                editTextDescription.error = getString(R.string.required)
                Toast.makeText(context, "Description is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /*if(TextUtils.isEmpty(imageButtonEvent.toString())){
                editTextDescription.error = getString(R.string.required)
                Toast.makeText(context, "Description is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }*/
            //uploadFile()//FirebaseNoSignedInUserException
            addEvent()

            //notification
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
                    .setContentText("An upcoming event is here. Check it out!")
                    .setSmallIcon(R.drawable.ic_gu_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                        R.drawable.ic_gu_logo
                    ))
                    .setContentIntent(pendingIntent)
            }else{
                builder = Notification.Builder(context,channelId)
                   // .setContentTitle("Green Urth")
                    .setContentText("An upcoming event is here. Check it out!")
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

    private fun pickImageFromGallery(){
        val imgIntent = Intent(Intent.ACTION_PICK)
        imgIntent.type = "image/*"
        startActivityForResult(imgIntent,
            IMAGE_PICK_CODE
        )
    }

    private fun uploadFile(){
        if(filePath != null){
            Toast.makeText(context,"Uploading", Toast.LENGTH_SHORT).show()
        }
        uploadFileName = UUID.randomUUID().toString()
        val imageRef = storageReference!!.child("eventImage/" + uploadFileName)
        //imageRef.putFile(filePath!!)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            //imageButtonEvent.setImageURI(data?.data)
            //Picasso.get().load(data?.data).into(imageButtonEvent)
            //filePath = data?.getData()

            /*var file = Uri.fromFile(File(imageURL))
            val imgRef = storageReference?.child("images/${file.lastPathSegment}")
            var upload = imgRef?.putFile(file)

            upload?.addOnFailureListener{
                Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
            }?.addOnSuccessListener {
                Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
            }*/
        }
    }

    private fun addEvent(){
        //val storageRef = storage?.reference
        //val imgRef = storageRef?.child(imageURL!!)
        //val eventImage = imageURL
        val title = editTextTitle.text.toString()
        val description= editTextDescription.text.toString()
        val date = editTextDate.text.toString()

        //val event = Event(eventImage,title,description,date)
        val event = Event(
            title,
            description,
            date
        )

        val eventValues = event.toMap()
        val childUpdates = HashMap<String,Any>()
        val key = eventDatabase!!.child("event").push().key
        childUpdates.put("/event/" + key, eventValues)
        eventDatabase!!.updateChildren(childUpdates)
        Toast.makeText(context, "Event added", Toast.LENGTH_SHORT).show()
        //imageViewEvent.setImageResource(android.R.color.transparent)
        editTextTitle.setText("")
        editTextDate.setText("")
        editTextDescription.setText("")
        view?.findNavController()?.navigate(R.id.fragmentEvent)
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
    companion object{
        const val IMAGE_PICK_CODE = 1
        const val RESULT_LOAD_IMAGE = 2
        const val RESULT_OK = 3
    }
}

package com.example.android.homepage.ui.news_and_event.ManageEvent


import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.android.homepage.MainActivity
import com.example.android.homepage.R
import com.example.android.homepage.R.id.fragmentEvent
import com.example.android.homepage.ui.news_and_event.Event
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.event_layout.*
import kotlinx.android.synthetic.main.event_layout.view.*
import kotlinx.android.synthetic.main.fragment_edit_event.*
import kotlinx.android.synthetic.main.fragment_edit_event.textViewDataKeyEdit
import kotlinx.android.synthetic.main.fragment_edit_event.view.*
import kotlinx.android.synthetic.main.fragment_edit_news.*
import java.lang.IllegalArgumentException
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class EditEventFragment : Fragment() {

    private val TAG = "EditEventFragment"
    private val REQUIRED = "Required"
    //access database table
    private var eventDatabase: DatabaseReference? = null//change
    //to get the current database pointer
    private var eventReference: DatabaseReference? = null//change
    private var eventListener: ChildEventListener? = null//change
    private var eventAdapter: FirebaseRecyclerAdapter<Event, EditEventViewHolder>? = null

    lateinit var notificationManager : NotificationManager //manage notification
    lateinit var notificationChannel : NotificationChannel //to allow user to enable/disable notification type
    lateinit var builder : Notification.Builder
    private val channelId = "com.example.android.homepage"

    private var listener: EditEventRVFragment.OnFragmentInteractionListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = EditEventFragmentArgs.fromBundle(arguments!!)
        //must have to add data into database
        //to get the root folder
        eventDatabase = FirebaseDatabase.getInstance().reference//change
        //to access to the table
        eventReference = FirebaseDatabase.getInstance().getReference("event")

        val view = inflater.inflate(R.layout.fragment_edit_event, container, false)
        val btnSubmit: Button = view.findViewById(R.id.buttonUpdate)
        val btnDelete: Button = view.findViewById(R.id.buttonDelete)
        notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        view.textViewDataKeyEdit.setText(args.dataKey)
        view.editTextTitle.setText(args.title)
        view.editTextDescription.setText(args.description)
        view.editTextDate.setText(args.date)

        btnDelete.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)

            dialogBuilder.setMessage(getString(R.string.delete_confirmation)).setCancelable(false)
                .setPositiveButton("Yes", DialogInterface.OnClickListener{
                        dialog, id -> deleteEvent()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener {
                        dialog, id ->  dialog.cancel()
                })

            val alert = dialogBuilder.create()
            alert.setTitle(getString(R.string.delete))
            alert.show()
        }

        btnSubmit.setOnClickListener {

            //validation on input
            if(TextUtils.isEmpty(editTextTitle.text.toString())){

                editTextTitle.error = getString(R.string.required)
                Toast.makeText(context, "Title is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if(TextUtils.isEmpty(editTextDate.text.toString())){
                editTextDate.error = getString(R.string.required)
                Toast.makeText(context, "Date is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(editTextDescription.text.toString())){
                editTextDescription.error = getString(R.string.required)
                Toast.makeText(context, "Description is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dialogBuilder = AlertDialog.Builder(context)

            dialogBuilder.setMessage(getString(R.string.update_confirmation)).setCancelable(false)
                .setPositiveButton("Yes",DialogInterface.OnClickListener{
                        dialog, id -> editEvent()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener {
                        dialog, id ->  dialog.cancel()
                })

            val alert = dialogBuilder.create()
            alert.setTitle(getString(R.string.update))
            alert.show()
        }

        return view
      //  return inflater.inflate(R.layout.fragment_add_event, container, false)
    }

    private fun deleteEvent(){
        eventDatabase = FirebaseDatabase.getInstance().reference.child("event").child(textViewDataKeyEdit.text.toString())//change
        eventDatabase!!.removeValue()

        Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show()
        view?.findNavController()?.navigate(R.id.fragmentEvent)
    }

    private fun editEvent(){
        val title = editTextTitle.text.toString()
        val description= editTextDescription.text.toString()
        val date = editTextDate.text.toString()

        val event = Event(
            title,
            description,
            date
        )

        val eventValues = event.toMap()
        val childUpdates = HashMap<String,Any>()

        val key = textViewDataKeyEdit.text.toString()
        childUpdates.put("/event/" + key, eventValues)
        eventDatabase!!.updateChildren(childUpdates)
        Toast.makeText(context, "Event updated", Toast.LENGTH_SHORT).show()
        editTextDescription.setText("")
        editTextTitle.setText("")
        editTextDate.setText("")

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
                .setContentText("Latest event have been updated. Check it out!")
                .setSmallIcon(R.drawable.ic_gu_logo)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                    R.drawable.ic_gu_logo
                ))
                .setContentIntent(pendingIntent)
        }else{
            builder = Notification.Builder(context,channelId)
                // .setContentTitle("Green Urth")
                .setContentText("Latest event have been updated. Check it out!")
                .setSmallIcon(R.drawable.ic_gu_logo)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                    R.drawable.ic_gu_logo
                ))
                .setContentIntent(pendingIntent)
        }
        //unique id
        notificationManager.notify(1,builder.build())

        view?.findNavController()?.navigate(R.id.fragmentEvent)

    }

    fun onButtonPressed(uri: Uri){
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is EditEventRVFragment.OnFragmentInteractionListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + "must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}

package com.example.android.homepage.ui.news_and_event.ManageNews


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
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.example.android.homepage.MainActivity
import com.example.android.homepage.R
import com.example.android.homepage.ui.news_and_event.News
import com.example.android.homepage.ui.news_and_event.ManageNews.EditNewsViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_news.*
import kotlinx.android.synthetic.main.fragment_edit_news.*
import kotlinx.android.synthetic.main.fragment_edit_news.view.*
import kotlinx.android.synthetic.main.news_layout.*
import java.lang.IllegalArgumentException
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class EditNewsFragment : Fragment() {

    private val TAG = "EditNewsFragment"
    private val REQUIRED = "Required"
    //access database table
    private var newsDatabase: DatabaseReference? = null//change
    //to get the current database pointer
    private var newsReference: DatabaseReference? = null//change
    private var newsListener: ChildEventListener? = null//change
    private var newsAdapter: FirebaseRecyclerAdapter<News, EditNewsViewHolder>? = null

    lateinit var notificationManager : NotificationManager //manage notification
    lateinit var notificationChannel : NotificationChannel //to allow user to enable/disable notification type
    lateinit var builder : Notification.Builder
    private val channelId = "com.example.android.homepage"

    private var dateRetrieved: Long? = null
    private var listener: EditNewsRVFragment.OnFragmentInteractionListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = EditNewsFragmentArgs.fromBundle(arguments!!)
        //must have to add data into database
        //to get the root folder
        newsDatabase = FirebaseDatabase.getInstance().reference//change
        //to access to the table
        newsReference = FirebaseDatabase.getInstance().getReference("news")

        val view = inflater.inflate(R.layout.fragment_edit_news, container, false)
        val btnSubmit: Button = view.findViewById(R.id.buttonSubmitEdit)
        val btnDelete: Button = view.findViewById(R.id.buttonDelete)
        notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        dateRetrieved = args.date
        view.textViewDataKeyEdit.setText(args.dataKey)
        view.editTextTitleEdit.setText(args.title)
        view.editTextLinkEdit.setText(args.link)

        btnDelete.setOnClickListener {

            val dialogBuilder = AlertDialog.Builder(context)

            dialogBuilder.setMessage(getString(R.string.delete_confirmation)).setCancelable(false)
                .setPositiveButton("Yes",DialogInterface.OnClickListener{
                    dialog, id -> deleteNews()
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
            if(TextUtils.isEmpty(editTextTitleEdit.text.toString())){

                editTextTitleEdit.error = getString(R.string.required)
                Toast.makeText(context, "Title is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(editTextLinkEdit.text.toString())){
                editTextLinkEdit.error = getString(R.string.required)
                Toast.makeText(context, "Link is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dialogBuilder = AlertDialog.Builder(context)

            dialogBuilder.setMessage(getString(R.string.update_confirmation)).setCancelable(false)
                .setPositiveButton("Yes",DialogInterface.OnClickListener{
                        dialog, id -> editNews()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener {
                        dialog, id ->  dialog.cancel()
                })

            val alert = dialogBuilder.create()
            alert.setTitle(getString(R.string.update))
            alert.show()
        }

        return view
      //  return inflater.inflate(R.layout.fragment_add_news, container, false)
    }


     private fun deleteNews(){
        newsDatabase = FirebaseDatabase.getInstance().reference.child("news").child(textViewDataKeyEdit.text.toString())//change
        newsDatabase!!.removeValue()

        Toast.makeText(context, "News deleted", Toast.LENGTH_SHORT).show()
        view?.findNavController()?.navigate(R.id.fragmentNews)
    }

    private fun editNews(){
        val title = editTextTitleEdit.text.toString()
        val link= editTextLinkEdit.text.toString()
        val date = dateRetrieved!!

        val news = News(
            title,
            link,
            date
        )

        val newsValues = news.toMap()
        val childUpdates = HashMap<String,Any>()

        //val key = newsDatabase!!.child("news").push().key
        val key = textViewDataKeyEdit.text.toString()
        childUpdates.put("/news/" + key, newsValues)
        newsDatabase!!.updateChildren(childUpdates)
        Toast.makeText(context, "News updated", Toast.LENGTH_SHORT).show()
        editTextTitleEdit.setText("")
        editTextLinkEdit.setText("")

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

        view?.findNavController()?.navigate(R.id.fragmentNews)
    }

    fun onButtonPressed(uri: Uri){
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is EditNewsRVFragment.OnFragmentInteractionListener){
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

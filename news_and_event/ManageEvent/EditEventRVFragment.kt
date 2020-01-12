package com.example.android.homepage.ui.news_and_event.ManageEvent


import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.homepage.R
import com.example.android.homepage.ui.news_and_event.Event
import com.firebase.ui.database.ChangeEventListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_fragment_event.*

/**
 * A simple [Fragment] subclass.
 */
class EditEventRVFragment : Fragment() {

    private var eventDatabase: DatabaseReference? = null//change
    //to get the current database pointer
    private var eventReference: DatabaseReference? = null//change
    private var eventListener: ChildEventListener? = null//change
    private var eventAdapter: FirebaseRecyclerAdapter<Event, EditEventViewHolder>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_event_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun firebaseListenerInit() {
        val childEventListener = object: ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "postMessages:onCancelled", databaseError!!.toException())
                //Toast.makeText(this, "Failed to load Message.", Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, location: String?) {
                Log.e(TAG, "onChildMoved:" + dataSnapshot!!.key)

                // A location has changed position
                val event = dataSnapshot.getValue(Event::class.java)
                //toast here
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, location: String?) {
                Log.e(TAG, "onChildChanged: " + dataSnapshot!!.key)

                val event = dataSnapshot.getValue(Event::class.java)
                //toast here
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, location: String?) {
                val event = dataSnapshot!!.getValue(Event::class.java)

                Log.e(TAG, "onChildAdded:" + event!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.e(TAG, "onChildRemoved:" + dataSnapshot!!.key)

                // A message has been removed
                val event = dataSnapshot.getValue(Event::class.java)
            }
        }
    }

    interface OnFragmentInteractionListener{
        fun onFragmentInteraction(uri: Uri)
    }

    private fun initView(){
        //to get the root folder
        eventDatabase = FirebaseDatabase.getInstance().reference
        eventReference = FirebaseDatabase.getInstance().getReference("event")

        firebaseListenerInit()
        eventRecyclerView.layoutManager = LinearLayoutManager(context)
        val query = eventReference!!.limitToLast(30)
        eventAdapter = object: FirebaseRecyclerAdapter<Event, EditEventViewHolder>(
            Event::class.java, R.layout.event_layout, EditEventViewHolder::class.java,query
        ){
            override fun populateViewHolder(viewHolder: EditEventViewHolder?, model: Event?, position: Int) {
                eventReference = getRef(position)
                model?.dataKey = eventReference?.key.toString()
                viewHolder!!.bindEventEdit(model)
            }

            override fun onChildChanged(
                type: ChangeEventListener.EventType?,
                snapshot: DataSnapshot?,
                index: Int,
                oldIndex: Int
            ) {
                super.onChildChanged(type, snapshot, index, oldIndex)
            }
        }
        eventRecyclerView.adapter = eventAdapter
    }

    override fun onStop() {
        super.onStop()

        if (eventListener != null) {
            eventReference!!.removeEventListener(eventListener!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        eventAdapter!!.cleanup()
    }
}

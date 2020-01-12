package com.example.android.homepage.ui.news_and_event


import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.homepage.R
import com.firebase.ui.database.ChangeEventListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_fragment_event.*

/**
 * A simple [Fragment] subclass.
 */
class FragmentEvent : Fragment() {

    private var eventDatabase: DatabaseReference? = null//change
    //to get the current database pointer
    private var eventReference: DatabaseReference? = null//change
    private var eventListener: ChildEventListener? = null//change
    private var eventAdapter: FirebaseRecyclerAdapter<Event, EventViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_event, container, false)
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

    private fun initView(){
        //to get the root folder
        eventDatabase = FirebaseDatabase.getInstance().reference
        eventReference = FirebaseDatabase.getInstance().getReference("event")

        firebaseListenerInit()
        eventRecyclerView.layoutManager = LinearLayoutManager(context)
        val query = eventReference!!.limitToLast(15)
        eventAdapter = object: FirebaseRecyclerAdapter<Event, EventViewHolder>(
            Event::class.java, R.layout.event_layout, EventViewHolder::class.java,query
        ){
            override fun populateViewHolder(viewHolder: EventViewHolder?, model: Event?, position: Int) {
                viewHolder!!.bindEvent(model)
            }

            override fun onChildChanged(
                type: ChangeEventListener.EventType?,
                snapshot: DataSnapshot?,
                index: Int,
                oldIndex: Int
            ) {
                super.onChildChanged(type, snapshot, index, oldIndex)
                //newsRecyclerView.scrollToPosition(index)
            }
        }
        eventRecyclerView.adapter = eventAdapter
    }

    interface OnFragmentInteractionListener{
        fun onFragmentInteraction(uri: Uri)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            setHasOptionsMenu(true)
        } else {
            setHasOptionsMenu(false)
        }
    }

    //inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.event_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //handle item clicks of menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //get item id to handle item clicks
        val id = item!!.itemId
        //handle item clicks
        if (id == R.id.add_event) {
            view!!.findNavController().navigate(R.id.addEventFragment)
            /* val myIntent = Intent(activity, TestAddNews::class.java)
             activity!!.startActivity(myIntent)*/
        }
        else if (id == R.id.edit_event) {
            view!!.findNavController().navigate(R.id.editEventRVFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}

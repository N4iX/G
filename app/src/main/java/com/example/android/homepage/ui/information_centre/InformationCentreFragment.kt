package com.example.android.homepage.ui.information_centre

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.homepage.R
import com.firebase.ui.database.ChangeEventListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_information_centre.*

class InformationCentreFragment : Fragment() {

    private lateinit var informationCentreViewModel: InformationCentreViewModel
    private val TAG = "ICFragment"
    private val REQUIRED = "Required"
    //access database table
    private var recyclableItemDatabase: DatabaseReference? = null//change
    //to get the current database pointer
    private var recyclableItemReference: DatabaseReference? = null//change
    private var recyclableItemListener: ChildEventListener? = null//change

    private var recyclableItemAdapter: FirebaseRecyclerAdapter<RecyclableItem, RecyclableItemViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        informationCentreViewModel =
            ViewModelProviders.of(this).get(InformationCentreViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_information_centre, container, false)
//        val textView: TextView = root.findViewById(R.id.text_information_centre)
//        informationCentreViewModel.text.observe(this, Observer {
//            textView.text = it
//        })
        //to get the root folder

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView(){
        recyclableItemDatabase = FirebaseDatabase.getInstance().reference//change
        //to access to the table
        recyclableItemReference = FirebaseDatabase.getInstance().getReference("recyclableItem")//change

        firebaseListenerInit()

        recyclerViewRecyclableItem.layoutManager = LinearLayoutManager(activity)
        val query = recyclableItemReference!!.limitToLast(8)
        recyclableItemAdapter = object: FirebaseRecyclerAdapter<RecyclableItem, RecyclableItemViewHolder>(
            RecyclableItem::class.java, R.layout.recyclable_item_view, RecyclableItemViewHolder::class.java,query
        ){
            override fun populateViewHolder(viewHolder: RecyclableItemViewHolder?, model: RecyclableItem?, position: Int) {
                viewHolder!!.bindRecyclableItem(model)
            }

            override fun onChildChanged(
                type: ChangeEventListener.EventType?,
                snapshot: DataSnapshot?,
                index: Int,
                oldIndex: Int
            ) {
                super.onChildChanged(type, snapshot, index, oldIndex)
                Log.d("key value", snapshot?.key.toString())
                //recyclerViewRecyclableItem.scrollToPosition(index)
            }
        }
        recyclerViewRecyclableItem.adapter = recyclableItemAdapter
    }

    private fun firebaseListenerInit() {
        val childEventListener = object: ChildEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "postMessages:onCancelled", databaseError!!.toException())
                //Toast.makeText(this, "Failed to load Message.", Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, recyclableItem: String?) {
                Log.e(TAG, "onChildMoved:" + dataSnapshot!!.key)

                // A recyclable item has changed position
                val recyclableItem = dataSnapshot.getValue(RecyclableItem::class.java)
                //toast here
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, recyclableItem: String?) {
                Log.e(TAG, "onChildChanged: " + dataSnapshot!!.key)

                val recyclableItem = dataSnapshot.getValue(RecyclableItem::class.java)
                //toast here
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, recyclableItem: String?) {
                val recyclableItem = dataSnapshot!!.getValue(RecyclableItem::class.java)

                Log.e(TAG, "onChildAdded:" + recyclableItem!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.e(TAG, "onChildRemoved:" + dataSnapshot!!.key)

                // A message has been removed
                val recyclableItem = dataSnapshot.getValue(RecyclableItem::class.java)
            }

        }
    }

    interface OnFragmentInteractionListener{
        fun onFragmentInteraction(uri: Uri)
    }
}
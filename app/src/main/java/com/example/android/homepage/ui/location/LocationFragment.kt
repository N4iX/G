package com.example.android.homepage.ui.location


import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.homepage.R
import com.firebase.ui.database.ChangeEventListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_location.*
import kotlinx.android.synthetic.main.location_layout.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LocationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private val TAG = "locationFragment"
    private val REQUIRED = "Required"

    //access database table
    private var locationDatabase: DatabaseReference? = null
    //to get the current database pointer
    private var locationReference: DatabaseReference? = null
    private var locationListener: ChildEventListener? = null

    //no need
    private var locationAdapter: FirebaseRecyclerAdapter<Location, LocationViewHolder>? = null

    //can throw
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //to get the root folder
        locationDatabase = FirebaseDatabase.getInstance().reference
        //to access to the table
        locationReference = FirebaseDatabase.getInstance().getReference("location")

        firebaseListenerInit()

        locationRecyclerView.layoutManager = LinearLayoutManager(activity)


        locationAdapter = object: FirebaseRecyclerAdapter<Location, LocationViewHolder>(
            Location::class.java,
            R.layout.location_layout,
            LocationViewHolder::class.java,
            this.locationReference//show all of the data in the firebase
        )
        {
            override fun populateViewHolder(viewHolder: LocationViewHolder?, model: Location?, position: Int) {
                viewHolder!!.bindLocation(model)
            }

            override fun onChildChanged(
                type: ChangeEventListener.EventType?,
                snapshot: DataSnapshot?,
                index: Int,
                oldIndex: Int
            ) {
                super.onChildChanged(type, snapshot, index, oldIndex)
                locationRecyclerView.scrollToPosition(index)
            }

            override fun onBindViewHolder(
                holder: LocationViewHolder,
                position: Int,
                payloads: MutableList<Any>
            ) {
                super.onBindViewHolder(holder, position, payloads)
                val address: String = holder.itemView.textViewLocationAddress.text.toString()

                holder.itemView.textViewLocationAddress.setOnClickListener{

                    val intent = Intent(ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
                    startActivity(intent)
                }
            }
        }
        locationRecyclerView.adapter = locationAdapter

        fabAddLocation.setOnClickListener{
            //set to another fragment

            val intent = Intent(activity, AddLocation::class.java)

            startActivity(intent)
        }
    }
    private fun firebaseListenerInit() {
        val childEventListener = object: ChildEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "postMessages:onCancelled", databaseError!!.toException())
                //Toast.makeText(this, "Failed to load Message.", Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, location: String?) {
                Log.e(TAG, "onChildMoved:" + dataSnapshot!!.key)

                // A location has changed position
                val location = dataSnapshot.getValue(Location::class.java)
                //toast here
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, location: String?) {
                Log.e(TAG, "onChildChanged: " + dataSnapshot!!.key)

                val location = dataSnapshot.getValue(Location::class.java)
                //toast here
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, location: String?) {
                val location = dataSnapshot!!.getValue(Location::class.java)

                Log.e(TAG, "onChildAdded:" + location!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.e(TAG, "onChildRemoved:" + dataSnapshot!!.key)

                // A message has been removed
                val location = dataSnapshot.getValue(Location::class.java)
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LocationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LocationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

package com.example.android.homepage.ui.news_and_event.ManageNews


import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.homepage.R
import com.example.android.homepage.ui.news_and_event.News
import com.firebase.ui.database.ChangeEventListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_fragment_news.*

/**
 * A simple [Fragment] subclass.
 */
class EditNewsRVFragment : Fragment() {

    private var newsDatabase: DatabaseReference? = null//change
    //to get the current database pointer
    private var newsReference: DatabaseReference? = null//change
    private var newsListener: ChildEventListener? = null//change
    private var newsAdapter: FirebaseRecyclerAdapter<News, EditNewsViewHolder>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_news_rv, container, false)
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
                val news = dataSnapshot.getValue(News::class.java)
                //toast here
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, location: String?) {
                Log.e(TAG, "onChildChanged: " + dataSnapshot!!.key)

                val news = dataSnapshot.getValue(News::class.java)
                //toast here
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, location: String?) {
                val news = dataSnapshot!!.getValue(News::class.java)

                Log.e(TAG, "onChildAdded:" + news!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.e(TAG, "onChildRemoved:" + dataSnapshot!!.key)

                // A message has been removed
                val news = dataSnapshot.getValue(News::class.java)
            }
        }
    }

    interface OnFragmentInteractionListener{
        fun onFragmentInteraction(uri: Uri)
    }

    private fun initView(){
        //to get the root folder
        newsDatabase = FirebaseDatabase.getInstance().reference
        newsReference = FirebaseDatabase.getInstance().getReference("news")

        firebaseListenerInit()
        newsRecyclerView.layoutManager = LinearLayoutManager(context)
        val query = newsReference!!.limitToLast(30)
        newsAdapter = object: FirebaseRecyclerAdapter<News, EditNewsViewHolder>(
            News::class.java, R.layout.news_layout, EditNewsViewHolder::class.java,query
        ){
            override fun populateViewHolder(viewHolder: EditNewsViewHolder?, model: News?, position: Int) {
                newsReference = getRef(position)
                model?.dataKey = newsReference?.key.toString()
                viewHolder!!.bindNewsEdit(model)
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
        newsRecyclerView.adapter = newsAdapter
    }

    override fun onStop() {
        super.onStop()

        if (newsListener != null) {
            newsReference!!.removeEventListener(newsListener!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        newsAdapter!!.cleanup()
    }
}

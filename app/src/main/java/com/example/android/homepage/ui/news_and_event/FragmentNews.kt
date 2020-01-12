package com.example.android.homepage.ui.news_and_event


import android.content.ContentValues.TAG
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
import kotlinx.android.synthetic.main.fragment_fragment_news.*

/**
 * A simple [Fragment] subclass.
 */
class FragmentNews : Fragment() {

    private var newsDatabase: DatabaseReference? = null//change
    //to get the current database pointer
    private var newsReference: DatabaseReference? = null//change
    private var newsListener: ChildEventListener? = null//change
    private var newsAdapter: FirebaseRecyclerAdapter<News, NewsViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_news, container, false)
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

    private fun initView(){
        //to get the root folder
        newsDatabase = FirebaseDatabase.getInstance().reference
        newsReference = FirebaseDatabase.getInstance().getReference("news")

        firebaseListenerInit()
        newsRecyclerView.layoutManager = LinearLayoutManager(context)
        val query = newsReference!!.limitToLast(30)
        newsAdapter = object: FirebaseRecyclerAdapter<News, NewsViewHolder>(
            News::class.java, R.layout.news_layout, NewsViewHolder::class.java,query
        ){
            override fun populateViewHolder(viewHolder: NewsViewHolder?, model: News?, position: Int) {
                newsReference = getRef(position)
                model?.dataKey = newsReference?.key.toString()
                viewHolder!!.bindNews(model)
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
        inflater!!.inflate(R.menu.news_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //handle item clicks of menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //get item id to handle item clicks
        val id = item!!.itemId
        //handle item clicks
        if (id == R.id.add_news) {
            view!!.findNavController().navigate(R.id.addNewsFragment)
            /* val myIntent = Intent(activity, TestAddNews::class.java)
             activity!!.startActivity(myIntent)*/
        }
        else if (id == R.id.edit_news) {
            view!!.findNavController().navigate(R.id.editNewsRVFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}

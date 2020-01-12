package com.example.android.homepage.ui.news_and_event

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.android.homepage.R
import com.google.android.material.tabs.TabLayout

class NewsAndEventFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_news_and_event, container, false)
        viewPager = root.findViewById(R.id.viewPager)
        tabLayout = root.findViewById(R.id.tabs)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        setHasOptionsMenu(true)
    }

    private fun setUpViewPager(viewPager: ViewPager) {
        val pagerAdapter =
            NEPagerAdapter(
                childFragmentManager
            )
        pagerAdapter.addFragment(FragmentNews(), "News")
        pagerAdapter.addFragment(FragmentEvent(), "Event")

        viewPager.adapter = pagerAdapter
    }

    //enable options menu in this fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


}
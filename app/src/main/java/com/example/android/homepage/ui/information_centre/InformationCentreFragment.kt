package com.example.android.homepage.ui.information_centre

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.homepage.R
import kotlinx.android.synthetic.main.fragment_information_centre.*

class InformationCentreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_information_centre, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var recyclableItemAdapter = RecyclableItemAdapter(requireContext())
        var recyclableItemViewModel = ViewModelProviders.of(requireActivity()).get(RecyclableItemViewModel::class.java)

        recyclableItemViewModel.recyclableItemList.observe(viewLifecycleOwner,
            Observer {
                if(it.isNotEmpty()){
                    recyclableItemAdapter.setRecyclableItemList(it)
                }
            })
        recyclerViewRecyclableItem.adapter = recyclableItemAdapter
        recyclerViewRecyclableItem.layoutManager = LinearLayoutManager(requireContext())
    }

    interface OnFragmentInteractionListener{
        fun onFragmentInteraction(uri: Uri)
    }
}
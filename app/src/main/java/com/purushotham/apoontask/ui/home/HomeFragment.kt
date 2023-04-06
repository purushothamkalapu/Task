package com.purushotham.apoontask.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.purushotham.apoontask.adapters.AddressAdapter
import com.purushotham.apoontask.adapters.ItemSelected
import com.purushotham.apoontask.databinding.FragmentHomeBinding
import com.purushotham.apoontask.ui.GetLocation

class HomeFragment : Fragment(), AddressAdapter.ItemClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: AddressAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.fabAddLocation?.setOnClickListener {
            Intent(requireContext(), GetLocation::class.java).also {
                resultLauncher.launch(it)
            }
        }

        binding.rcvLocations.setHasFixedSize(true)
        binding.rcvLocations.layoutManager = LinearLayoutManager(context)

        recycleData()
    }

    private fun recycleData() {
        homeViewModel.getAddress(requireContext())?.observe(this.viewLifecycleOwner) {
            if(it.size>0){
                binding.tvDefaultText.visibility = View.GONE
                adapter = AddressAdapter(it, requireContext(), homeViewModel, object: ItemSelected{
                    override fun onItemRemoveClick(position: Int) {
                        adapter.notifyDataSetChanged()
                    }

                })
                binding.rcvLocations.adapter = adapter

            }else{
                recycleData()
                binding.tvDefaultText.visibility = View.VISIBLE
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val lat = data!!.getStringExtra("lat")
            val lng = data!!.getStringExtra("lng")
            val address = data!!.getStringExtra("address")
            if (address != null) {
                if (lat != null) {
                    if (lng != null) {
                        homeViewModel.insertData(requireContext(), lat, lng, address, isFav = false)
                        Toast.makeText(requireContext(), "Successfully saved data...!", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }
    }

    override fun onItemClick(position: Int) {
        recycleData()
    }




}
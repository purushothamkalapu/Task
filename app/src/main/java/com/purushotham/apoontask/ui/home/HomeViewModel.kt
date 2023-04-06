package com.purushotham.apoontask.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.purushotham.apoontask.room.AddressEntity
import com.purushotham.apoontask.room.AddressRepository

class HomeViewModel : ViewModel() {



    fun insertData(context: Context, lat: String, lng: String, address: String, isFav: Boolean) {
        AddressRepository.insertData(context, lat, lng, address, isFav)
    }
    fun deleteItem(context: Context, id: Int) {
        AddressRepository.deleteItem(context, id)
    }

    fun update(context: Context, id: Int, boolValue: Boolean) {
        AddressRepository.update(context, id, boolValue)
    }

    var liveDataAddress: LiveData<MutableList<AddressEntity>>? = null
    fun getAddress(context: Context) : LiveData<MutableList<AddressEntity>>? {
        liveDataAddress = AddressRepository.getAddress(context)
        return liveDataAddress
    }
}
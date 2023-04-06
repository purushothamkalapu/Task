package com.purushotham.apoontask.ui.city

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.purushotham.apoontask.room.AddressEntity
import com.purushotham.apoontask.room.AddressRepository

class CityViewModel : ViewModel() {

    var liveDataAddress: LiveData<AddressEntity>? = null
    fun getFavAddress(context: Context) : LiveData<AddressEntity>? {
        liveDataAddress = AddressRepository.getFavAddress(context)
        return liveDataAddress
    }
}
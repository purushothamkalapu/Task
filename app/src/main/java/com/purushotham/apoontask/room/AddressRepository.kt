package com.purushotham.apoontask.room

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressRepository {

    companion object {

        var addressDatabase: AddressDatabase? = null

        var addressDataRepo: LiveData<MutableList<AddressEntity>>? = null

        fun initializeDB(context: Context) : AddressDatabase {
            return AddressDatabase.getDatabaseClient(context)
        }

        fun insertData(context: Context, lat: String, lng: String, address: String, isFav: Boolean) {

            addressDatabase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                val loginDetails = AddressEntity(lat, lng, address, isFav)
                addressDatabase!!.userDao().insertData(loginDetails)
            }

        }

        fun getAddress(context: Context) : LiveData<MutableList<AddressEntity>>? {

            addressDatabase = initializeDB(context)

            addressDataRepo = addressDatabase!!.userDao().getUserDetails()

            return addressDataRepo
        }

        fun deleteItem(context: Context, id: Int): Int {
            addressDatabase = initializeDB(context)

            var result = addressDatabase!!.userDao().deleteItem(id)

            return result
        }

        fun update(context: Context, id: Int, boolValue: Boolean): Int {
            addressDatabase = initializeDB(context)

            var result = addressDatabase!!.userDao().update(id, boolValue)

            return result
        }

        fun getFavAddress(context: Context): LiveData<AddressEntity> {
            addressDatabase = initializeDB(context)

            var result = addressDatabase!!.userDao().getFavAddress()

            return result
        }

    }
}
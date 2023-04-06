package com.purushotham.apoontask.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_address")
data class AddressEntity (@ColumnInfo(name = "lat")
                          var lat: String,
                          @ColumnInfo(name = "lng")
                          var lng: String,
                          @ColumnInfo(name = "address")
                          var address: String,
                          @ColumnInfo(name = "isFav")
                          var isFav: Boolean,
                          ){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int? = null
}
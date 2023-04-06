package com.purushotham.apoontask.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(addressEntity: AddressEntity)

    @Query("SELECT * FROM tbl_address")
    fun getUserDetails() : LiveData<MutableList<AddressEntity>>
    @Query("DELETE FROM tbl_address WHERE id = :id")
    fun deleteItem(id: Int): Int

    @Query("UPDATE tbl_address SET isFav = :boolValue WHERE id = :id")
    fun update(id: Int, boolValue: Boolean): Int

    @Query("SELECT * FROM tbl_address WHERE isFav = true")
    fun getFavAddress() : LiveData<AddressEntity>
}
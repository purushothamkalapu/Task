package com.purushotham.apoontask.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(AddressEntity::class), version = 1, exportSchema = false)
abstract class AddressDatabase(): RoomDatabase() {

    abstract fun userDao() : AddressDao

    companion object {

        @Volatile
        private var INSTANCE: AddressDatabase? = null

        fun getDatabaseClient(context: Context) : AddressDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, AddressDatabase::class.java, "USER_DATABASE")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()

                return INSTANCE!!

            }
        }

    }

}
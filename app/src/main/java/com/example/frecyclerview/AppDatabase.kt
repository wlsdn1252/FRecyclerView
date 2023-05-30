package com.example.frecyclerview

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database()
abstract class AppDatabase : RoomDatabase(){

    companion object{
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context:Context): AppDatabase?{
            if(INSTANCE == null){
                synchronized(AppDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app-database.db"
                    ).build()
                }

            }
            return INSTANCE
        }

    }
}
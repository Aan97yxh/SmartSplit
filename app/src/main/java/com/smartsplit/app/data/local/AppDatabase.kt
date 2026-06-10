package com.smartsplit.app.data.local

import android.content.Context
import androidx.room.*
import com.smartsplit.app.data.local.converter.Converters
import com.smartsplit.app.data.local.dao.BillDao
import com.smartsplit.app.domain.model.Bill

@Database(entities = [Bill::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun billDao(): BillDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "smartsplit_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
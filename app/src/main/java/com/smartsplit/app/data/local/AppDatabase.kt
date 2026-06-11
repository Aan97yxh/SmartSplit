package com.smartsplit.app.data.local

import android.content.Context
import androidx.room.*
import com.smartsplit.app.data.local.converter.Converters
import com.smartsplit.app.data.local.dao.BillDao
import com.smartsplit.app.data.local.dao.UserDao
import com.smartsplit.app.domain.model.Bill
import com.smartsplit.app.domain.model.User

@Database(entities = [Bill::class, User::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun billDao(): BillDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "smartsplit_db")
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
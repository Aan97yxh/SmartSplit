package com.smartsplit.app.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smartsplit.app.domain.model.BillItem

class Converters {
    private val gson = Gson()

    @TypeConverter fun fromStringList(value: List<String>): String = gson.toJson(value)
    @TypeConverter fun toStringList(value: String): List<String> =
        gson.fromJson(value, object : TypeToken<List<String>>() {}.type)

    @TypeConverter fun fromStringSet(value: Set<String>): String = gson.toJson(value)
    @TypeConverter fun toStringSet(value: String): Set<String> =
        gson.fromJson(value, object : TypeToken<Set<String>>() {}.type)

    @TypeConverter fun fromBillItemList(value: List<BillItem>): String = gson.toJson(value)
    @TypeConverter fun toBillItemList(value: String): List<BillItem> =
        gson.fromJson(value, object : TypeToken<List<BillItem>>() {}.type)
}
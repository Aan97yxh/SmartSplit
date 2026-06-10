package com.smartsplit.app.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

object ShareUtils {
    fun saveBitmapAndShare(context: Context, bitmap: Bitmap) {
        try {
            val cachePath = File(context.cacheDir, "images").also { it.mkdirs() }
            val file      = File(cachePath, "smartsplit_bill.png")
            FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type      = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(intent, "Share Bill"))
            Timber.d("Bill shared successfully")
        } catch (e: Exception) {
            Timber.e(e, "Failed to share bill")
        }
    }
}
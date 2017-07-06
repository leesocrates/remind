package com.lee.library.imageLoader


import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object Imageutil {

    fun getCompressImageFromFile(srcPath: String): Bitmap {
        val newOpts = BitmapFactory.Options()

        newOpts.inJustDecodeBounds = false
        newOpts.inSampleSize = 2//设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565

        return BitmapFactory.decodeFile(srcPath, newOpts)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options,
                                      reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = if (heightRatio < widthRatio) widthRatio else heightRatio
        }

        return inSampleSize
    }

    fun decodeSampledBitmapFromResource(res: Resources, resId: Int,
                                        reqWidth: Int, reqHeight: Int): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    fun decodeSampledBitmapFromFile(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        if (!File(filePath).exists()) {
            return null
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    fun decodeSampleBitmapFromByteArray(data: ByteArray, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(data, 0, data.size, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        options.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(data, 0, data.size, options)
    }

    fun getRealImageFilePath(context: Context, uri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        if (cursor!!.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val yourRealPath = cursor.getString(columnIndex)

            return yourRealPath
        } else {
            //boooo, cursor doesn't have rows ...
        }
        cursor.close()
        return ""
    }

    fun saveBitmapToFile(context: Context, bmp: Bitmap, fileName: String): Boolean {
        val file = File(context.filesDir, fileName)
        var bos: BufferedOutputStream? = null
        try {
            bos = BufferedOutputStream(FileOutputStream(file))
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        } finally {
            try {
                if (bos != null) {
                    bos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return true
    }
}

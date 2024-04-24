package com.example.learntogether_mobile.API

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Для конвертации изображений в строки для json и наоборот
 */
object ImageConverter {
    /**
     * Обязательно инициализировать в первой активности!
     */
    @JvmField
    var DefaultIcon: Bitmap? = null

    /**
     * Метод для кодирования изображения в строку Base64
     */
    @JvmStatic
    fun encodeImage(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, 40, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.URL_SAFE)
    }

    /**
     * Метод для декодирования строки Base64 обратно в изображение
     */
    @JvmStatic
    fun decodeImage(encodedString: String?): Bitmap? {
        if (encodedString.isNullOrEmpty()) {
            return DefaultIcon
        }
        val decodedBytes = Base64.decode(encodedString, Base64.URL_SAFE)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    /**
     * Метод для кодирования массива изображений в строку Base64
     */
    @JvmStatic
    fun encodeImages(imageList: List<Bitmap>): String {
        val encodedString = StringBuilder()
        for (bitmap in imageList) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.WEBP, 70, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val base64Image = Base64.encodeToString(byteArray, Base64.URL_SAFE)
            encodedString.append(base64Image).append(" ")
        }
        return encodedString.toString()
    }

    /**
     * Метод для декодирования строки Base64 в список изображений Bitmap
     */
    @JvmStatic
    fun decodeImages(encodedImages: String): List<Bitmap> {
        val imageList: MutableList<Bitmap> = ArrayList()
        val encodedImagesArray =
            encodedImages.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (encodedImage in encodedImagesArray) {
            val decodedBytes = Base64.decode(encodedImage, Base64.URL_SAFE)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            if (bitmap != null) {
                imageList.add(bitmap)
            }
        }
        return imageList
    }
}
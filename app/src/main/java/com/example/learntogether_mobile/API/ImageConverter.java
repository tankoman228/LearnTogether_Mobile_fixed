package com.example.learntogether_mobile.API;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.example.learntogether_mobile.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Для конвертации изображений в строки для json и наоборот
 */
public class ImageConverter {

    /**
     * Обязательно инициализировать в первой активности!
     */
    public static Bitmap DefaultIcon;

    /**
     * Метод для кодирования изображения в строку Base64
     */
    public static String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.URL_SAFE);
    }


    /**
     * Метод для декодирования строки Base64 обратно в изображение
     */
    public static Bitmap decodeImage(String encodedString) {
        if (encodedString == null || encodedString.length() == 0) {
            return DefaultIcon;
        }

        byte[] decodedBytes = Base64.decode(encodedString, Base64.URL_SAFE);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    /**
     * Метод для кодирования массива изображений в строку Base64
     */
    public static String encodeImages(List<Bitmap> imageList) {
        StringBuilder encodedString = new StringBuilder();
        for (Bitmap bitmap : imageList) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, 70, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(byteArray, Base64.URL_SAFE);
            encodedString.append(base64Image).append(" ");
        }
        return encodedString.toString();
    }


    /**
     * Метод для декодирования строки Base64 в список изображений Bitmap
     */
    public static List<Bitmap> decodeImages(String encodedImages) {
        List<Bitmap> imageList = new ArrayList<>();
        String[] encodedImagesArray = encodedImages.split(" ");
        for (String encodedImage : encodedImagesArray) {
            byte[] decodedBytes = Base64.decode(encodedImage, Base64.URL_SAFE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            if (bitmap != null) {
                imageList.add(bitmap);
            }
        }
        return imageList;
    }
}
package com.example.learntogether_mobile.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ImageConverter {


    // Метод для кодирования изображения в строку Base64
    public static String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.URL_SAFE);
    }


    // Метод для декодирования строки Base64 обратно в изображение
    public static Bitmap decodeImage(String encodedString) {
        byte[] decodedBytes = Base64.decode(encodedString, Base64.URL_SAFE);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    // Метод для кодирования массива изображений в строку Base64
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

    // Метод для декодирования строки Base64 в список изображений Bitmap
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
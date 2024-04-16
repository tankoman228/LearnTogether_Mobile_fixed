package com.example.learntogether_mobile.API;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Кодировка файлов в строки для json и наоборот, преобразование по строке из json
 * в файлы в папке загрузок
 */
public class FileEncoderDecoder {

    /**
     * Files to string
     */
    public static String encodeFilesToBase64(Uri[] uris, Context context) {
        JSONArray jsonArray = new JSONArray();
        for (Uri uri : uris) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                byte[] fileBytes = new byte[inputStream.available()];
                inputStream.read(fileBytes);
                inputStream.close();
                String base64String = Base64.encodeToString(fileBytes, Base64.URL_SAFE);
                JSONObject jsonObject = new JSONObject();

                String filename = uri.getLastPathSegment();
                String extension = MimeTypeMap.getFileExtensionFromUrl(filename);
                if (extension == null || extension.isEmpty()) {
                    // Если расширение не найдено, попробуйте получить его из ContentResolver
                    String mimeType = context.getContentResolver().getType(uri);
                    extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
                }
                jsonObject.put("filename",  filename.replace(":", "") + "." + extension);
                jsonObject.put("data", base64String);
                jsonArray.put(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return jsonArray.toString();
    }

    /**
     * String to file
     */
    public static void decodeBase64ToFile(String base64String, Context context, String id) {
        try {
            JSONArray jsonArray = new JSONArray(base64String);
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String filename = "lt" + id + "_" + jsonObject.getString("filename");
                Log.d("API", filename);
                String data = jsonObject.getString("data");
                byte[] fileBytes = Base64.decode(data, Base64.URL_SAFE);
                File file = new File(downloadsDir, filename);
                FileOutputStream os = new FileOutputStream(file);
                os.write(fileBytes);
                os.close();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Проверка, скачан ли файл заранее
     */
    public static boolean checkFilesDownloaded(String id) {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        for (File file : downloadsDir.listFiles()) {
            if (file.getName().contains(id)) {
                return true;
            }
        }

        return false;
    }
}
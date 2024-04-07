package com.example.learntogether_mobile.API;

import android.content.Context;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileEncoderDecoder {

    public static String encodeFilesToBase64(File[] files) {
        JSONArray jsonArray = new JSONArray();
        for (File file : files) {
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] fileBytes = new byte[(int) file.length()];
                fis.read(fileBytes);
                fis.close();
                String base64String = Base64.encodeToString(fileBytes, Base64.DEFAULT);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("filename", file.getName());
                jsonObject.put("data", base64String);
                jsonArray.put(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return jsonArray.toString();
    }

    public static void decodeBase64ToFile(String base64String, Context context, int id) {
        try {
            JSONArray jsonArray = new JSONArray(base64String);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String filename = "[" + id + "]_" + jsonObject.getString("filename");
                String data = jsonObject.getString("data");
                byte[] fileBytes = Base64.decode(data, Base64.DEFAULT);
                File file = new File(context.getFilesDir(), filename);
                FileOutputStream os = new FileOutputStream(file);
                os.write(fileBytes);
                os.close();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkFilesDownloaded(int id, Context context) {
        try {
            File directory = context.getFilesDir();
            if (directory.exists() && directory.isDirectory()) {
                for (File file : directory.listFiles()) {
                    if (file.getName().startsWith("[" + id + "]_")) {
                        return true;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
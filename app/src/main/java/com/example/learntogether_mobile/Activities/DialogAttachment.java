package com.example.learntogether_mobile.Activities;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;

import com.example.learntogether_mobile.API.Cache.CallbackAfterLoaded;
import com.example.learntogether_mobile.API.ImageConverter;
import com.example.learntogether_mobile.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

//Класс диалогового окна
public class DialogAttachment extends DialogFragment {

    private AppCompatActivity activity;
    private Bitmap image;
    public static String AttachmentJson;
    public static boolean WatchOnly;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;


    //Связь с текущим экраном
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Сборка диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Установка макета
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_attachments, null);
        builder.setView(view);

        EditText etLink = view.findViewById(R.id.editTextLink);
        TextView tvLink = view.findViewById(R.id.textViewLink);
        PhotoView iv = view.findViewById(R.id.imageView);

        if (!WatchOnly) {
            view.findViewById(R.id.btnClear).setOnClickListener(l -> {
                tvLink.setText("");
                AttachmentJson = "";
                iv.setVisibility(View.GONE);
                image = null;
            });
            view.findViewById(R.id.btnAddImage).setOnClickListener(l -> {
                if (ContextCompat.checkSelfPermission(this.activity, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickImageLauncher.launch(galleryIntent);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                    }
                    else {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }
            });
            view.findViewById(R.id.btnAddLink).setOnClickListener(l -> {
                if (!etLink.getText().toString().equals("")) {
                    tvLink.setText(new StringBuilder().append(tvLink.getText()).append("\n").append(etLink.getText()).toString());
                    etLink.setText("");
                }
            });
            AttachmentJson = "";
            image = null;
        }
        else {
            view.findViewById(R.id.btnClear).setVisibility(View.GONE);
            view.findViewById(R.id.btnAddImage).setVisibility(View.GONE);
            view.findViewById(R.id.btnAddLink).setVisibility(View.GONE);
            view.findViewById(R.id.editTextLink).setVisibility(View.GONE);
            try {
                JSONObject attachmentJsonObj = new JSONObject(AttachmentJson);

                String img = attachmentJsonObj.getString("image");
                if (!img.equals("")) {
                    iv.setImageBitmap(ImageConverter.decodeImage(img));
                    iv.setVisibility(View.VISIBLE);
                }
                String decodedLinks = attachmentJsonObj.getString("links");

                tvLink.setText(decodedLinks);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(galleryIntent);
            } else {
                Toast.makeText(activity, "Access to your gallery denied", Toast.LENGTH_SHORT).show();
            }
        });

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                try {
                    image = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
                    iv.setImageBitmap(image);
                    iv.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    iv.setVisibility(View.GONE);
                }
            }
            else {
                iv.setVisibility(View.GONE);
            }
        });

        return builder
                .setTitle("Attachment")
                .setIcon(R.drawable.folder)
                .setPositiveButton("OK", (dialog, which) -> {

                    String linksList = tvLink.getText().toString();

                    if (image == null && linksList.equals("")) {
                        return;
                    }

                    String imageBitmapConverted;
                    if (image != null)
                        imageBitmapConverted = ImageConverter.encodeImage(image);
                    else
                        imageBitmapConverted = "";

                    JSONObject json = new JSONObject();
                    try {
                        json.put ("image", imageBitmapConverted);
                        json.put("links", linksList);
                        AttachmentJson = json.toString();
                    } catch (JSONException e) {e.printStackTrace();
                    }
                })
                .create();
    }
}
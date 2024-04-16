package com.example.learntogether_mobile.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.learntogether_mobile.API.FileEncoderDecoder;
import com.example.learntogether_mobile.API.ListU;
import com.example.learntogether_mobile.API.RequestU;
import com.example.learntogether_mobile.API.ResponseU;
import com.example.learntogether_mobile.API.RetrofitRequest;
import com.example.learntogether_mobile.API.Variables;
import com.example.learntogether_mobile.Activities.WatchMoreActivity.ActComments;
import com.example.learntogether_mobile.R;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Адаптер для ленты с материалами
 */
public class AdapterInfo  extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;

    private ArrayList<ListU> objects;


    public AdapterInfo(Context context, ArrayList<ListU> objList) {
        ctx = context;
        objects = objList;

        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return objects.size();
    }
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = lInflater.inflate(R.layout.item_info, parent, false);

        ListU thisInfo = getForumAsk(position);
        //Инициализация интерфейса
        ((TextView) view.findViewById(R.id.tvUsername)).setText(thisInfo.getAuthorTitle());
        ((TextView) view.findViewById(R.id.tvTitle)).setText(thisInfo.getTitle());
        ((TextView) view.findViewById(R.id.tvTextDescription)).setText(thisInfo.getText());
        ((TextView) view.findViewById(R.id.tvWhen)).setText(thisInfo.getWhenAdd());
        ((TextView) view.findViewById(R.id.tvCommentsNum)).setText(String.valueOf(thisInfo.getCommentsFound()));

        //Удаление из базы
        view.findViewById(R.id.ibDelete).setOnClickListener(l -> {
            RequestU req = new RequestU();
            req.setID_InfoBase(thisInfo.getID_InfoBase());
            req.setSession_token(Variables.SessionToken);

            RetrofitRequest r = new RetrofitRequest();
            r.apiService.delete_ib(req).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {

                    if (response.body().Error != null) {
                        Toast.makeText(ctx, ctx.getString(R.string.error) + response.body().Error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ctx, ctx.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    ((AppCompatActivity)ctx).runOnUiThread(() -> view.setVisibility(View.GONE));
                }
                @Override
                public void onFailure(Call<ResponseU> call, Throwable t) {
                }
            });
        });

        //Переход в комментарии
        view.findViewById(R.id.ibComments).setOnClickListener(l -> {
            ((AppCompatActivity)ctx).runOnUiThread(() -> {
                ActComments.ID_InfoBase = thisInfo.getID_InfoBase();
                ctx.startActivity(new Intent(ctx, ActComments.class));
            });
        });

        //Скачивание в загрузки и открытие фала
        if (FileEncoderDecoder.checkFilesDownloaded(FilenameAllowed(thisInfo))) {
            view.findViewById(R.id.layoutRate).setVisibility(View.VISIBLE);

            Button openFileButton = view.findViewById(R.id.btnDownload);
            openFileButton.setText(R.string.open_file);
            openFileButton.setOnClickListener(v -> {
                openFile(thisInfo);
            });
        }
        else {
            view.findViewById(R.id.btnDownload).setOnClickListener(l -> {
                thisInfo.getID_InfoBase();

                RequestU requestU = new RequestU();
                requestU.setSession_token(Variables.SessionToken);
                requestU.setId_object(thisInfo.getID_Information());
                new RetrofitRequest().apiService.download(requestU).enqueue(new Callback<ResponseU>() {
                    @Override
                    public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                        FileEncoderDecoder.decodeBase64ToFile(response.body().getContents(), ctx, FilenameAllowed(thisInfo));

                        ((AppCompatActivity) ctx).runOnUiThread(() -> {
                            Button openFileButton = view.findViewById(R.id.btnDownload);
                            openFileButton.setText(R.string.open_file);
                            openFileButton.setOnClickListener(v -> {
                                openFile(thisInfo);
                            });
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseU> call, Throwable t) {

                    }
                });
            });
        }

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        Button btnRates[] = {
                view.findViewById(R.id.btn1),
                view.findViewById(R.id.btn2),
                view.findViewById(R.id.btn3),
                view.findViewById(R.id.btn4),
                view.findViewById(R.id.btn5)
        };
        for (int i = 1; i < 6; i++) {
            int finalI = i;
            btnRates[i-1].setOnClickListener(l -> {
                RequestU req = new RequestU();
                req.setID_InfoBase(thisInfo.getID_InfoBase());
                req.setSession_token(Variables.SessionToken);
                req.Rank = finalI;
                RetrofitRequest r = new RetrofitRequest();
                r.apiService.rate(req).enqueue(new Callback<ResponseU>() {
                    @Override
                    public void onResponse(Call<ResponseU> call, Response<ResponseU> response) {
                        Toast.makeText(ctx, "Thanks for your rate", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<ResponseU> call, Throwable t) {
                        Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
        progressBar.setProgress((int)(thisInfo.Rate * 20f));


        return view;
    }

    ListU getForumAsk(int position) {
        return ((ListU) getItem(position));
    }

    // Метод для получения MIME-типа файла
    private String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * Перевод в нормальный вид для имени файла
     */
    public static String FilenameAllowed(ListU ask) {
        return FilenameAllowed(ask.getID_InfoBase() + ask.getTitle() + ask.getID_Group() + ask.getAuthorTitle());
    }
    /**
     * Перевод в нормальный вид для имени файла
     */
    public static String FilenameAllowed(String ask) {
        return ask.replaceAll("[^a-zA-Z0-9.-]", "").replace(":", "");
    }


    /**
     * Открытие файлов
     */
    private void openFile(ListU thisInfo) {
        String targetFileName = FilenameAllowed(thisInfo); // Получаем имя файла, которое нужно найти

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] files = downloadsDir.listFiles();

        Log.d("API", targetFileName);
        for (File file : files) {
            if (file.getName().contains(targetFileName)) {
                // Найден файл, открываем его
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(ctx, ctx.getApplicationContext().getPackageName() + ".provider", file);
                intent.setDataAndType(uri, getMimeType(file.getAbsolutePath()));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                //if (intent.resolveActivity(ctx.getPackageManager()) != null) {
                ctx.startActivity(Intent.createChooser(intent, "How to open this file?"));
                // } else {
                //    Toast.makeText(ctx, "Cannot open file in downloads: " + file.getName(), Toast.LENGTH_SHORT).show();
                //}

            }
        }

    }
}
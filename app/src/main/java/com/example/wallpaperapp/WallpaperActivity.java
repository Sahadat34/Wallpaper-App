package com.example.wallpaperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wallpaperapp.models.Photo;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class WallpaperActivity extends AppCompatActivity {
    ImageView wallpaper_image;
    Button download_btn,setWallpaper_btn;
    Photo photo;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wallpaper);
        progressBar = findViewById(R.id.pb);
        //Toast.makeText(WallpaperActivity.this, "Loading Image", Toast.LENGTH_SHORT).show();

        wallpaper_image = findViewById(R.id.imageView_Wallpaper);
        download_btn = findViewById(R.id.download_btn);
        setWallpaper_btn = findViewById(R.id.setWallpaper_btn);


        photo = (Photo) getIntent().getSerializableExtra("photo");
        Picasso.get().load(photo.getSrc().getOriginal()).placeholder(R.drawable.not_available).into(wallpaper_image, new Callback() {
            @Override
            public void onSuccess() {
              progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadManager downloadManager = null;
                downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(photo.getSrc().getOriginal());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                        .setAllowedOverRoaming(false)
                        .setTitle("Wallpaper"+photo.getPhotographer())
                        .setMimeType("image/jpeg")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,"Wallpaper_"+photo.getPhotographer()+".jpg");
                downloadManager.enqueue(request);
                Toast.makeText(WallpaperActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();

            }
        });
        setWallpaper_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setWallpaper();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                },2000);



            }
        });


    }
    public void setWallpaper(){
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(WallpaperActivity.this);
        Bitmap bitmap = ((BitmapDrawable) wallpaper_image.getDrawable()).getBitmap();
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(WallpaperActivity.this, "Wallpaper applied", Toast.LENGTH_SHORT).show();


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(WallpaperActivity.this, "Something error", Toast.LENGTH_SHORT).show();

        }
    }
}
package com.example.wallpaperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wallpaperapp.listeners.OnRecyclerClickListner;
import com.example.wallpaperapp.listeners.WallpapersListeners;
import com.example.wallpaperapp.listeners.WallpapersListenersSearch;
import com.example.wallpaperapp.models.CuratedApiResponse;
import com.example.wallpaperapp.models.Photo;
import com.example.wallpaperapp.models.SearchApiResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRecyclerClickListner {
    RecyclerView recyclerView_home;
    ProgressDialog pd;
    SearchView searchView;
    CustomAdapter adapter;
    RequestManager manager;
    Button next_btn,previous_btn;
    int page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        next_btn = findViewById(R.id.next_btn);
        previous_btn = findViewById(R.id.previous_btn);
        searchView = findViewById(R.id.serach);

        pd = new ProgressDialog(this);
        pd.setTitle("Loading...");
        pd.show();


        manager = new RequestManager(this);
        manager.getCuratedWallpapers(listeners,"1");

        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page>1) {
                    String prev_page = String.valueOf(page - 1);
                    manager.getCuratedWallpapers(listeners, prev_page);
                    pd.show();
                }
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String next_page = String.valueOf(page+1);
                manager.getCuratedWallpapers(listeners,next_page);
                pd.show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pd.setTitle("Searching..");
                pd.show();
                manager.searchWallpapers(wallpapersListenersSearch,"1",query);
                next_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String next_page = String.valueOf(page+1);
                        manager.searchWallpapers(wallpapersListenersSearch,next_page,query);
                        pd.show();
                    }
                });
                previous_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (page>1) {
                            String prev_page = String.valueOf(page - 1);
                            manager.searchWallpapers(wallpapersListenersSearch, prev_page,query);
                            pd.show();
                        }
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private final WallpapersListeners listeners = new WallpapersListeners() {
        @Override
        public void onFetch(CuratedApiResponse response, String message) {
            if (response.getPhotos().isEmpty()){
                Toast.makeText(MainActivity.this, "No image found", Toast.LENGTH_SHORT).show();
                return;
            }
            page = response.getPage();
            showImage(response.getPhotos());
            pd.dismiss();
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
            return;
        }
    };

    private void showImage(List<Photo> photos) {
        recyclerView_home = findViewById(R.id.recycler_main);
        recyclerView_home.setHasFixedSize(true);
        recyclerView_home.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new CustomAdapter(MainActivity.this,photos,this);
        recyclerView_home.setAdapter(adapter);
    }

    @Override
    public void onClick(Photo photo) {
        startActivity(new Intent(MainActivity.this,WallpaperActivity.class)
                .putExtra("photo",photo));
    }
    private final WallpapersListenersSearch wallpapersListenersSearch = new WallpapersListenersSearch() {
        @Override
        public void onFetch(SearchApiResponse response, String message) {
            pd.dismiss();
           if (response.getPhotos().isEmpty()){
               Toast.makeText(MainActivity.this, "No result found", Toast.LENGTH_SHORT).show();
               return;
           }
           showImage(response.getPhotos());
           pd.dismiss();
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            return;
        }
    };
}
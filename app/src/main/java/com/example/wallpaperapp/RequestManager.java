package com.example.wallpaperapp;

import android.content.Context;
import android.widget.Toast;

import com.example.wallpaperapp.listeners.WallpapersListeners;
import com.example.wallpaperapp.listeners.WallpapersListenersSearch;
import com.example.wallpaperapp.models.CuratedApiResponse;
import com.example.wallpaperapp.models.SearchApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class RequestManager {
    Context context;

    public RequestManager(Context context) {
        this.context = context;
    }
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private interface CallWallpapersList {
        @Headers({
                "Accept: Application/json",
                "Authorization: 563492ad6f9170000100000132c3a46cf0344449a86c5c828895eaf3"
        })
        @GET("curated/")
        Call<CuratedApiResponse> getWallpapers(
                @Query("page") String page,
                @Query("per_page") String per_page
        );

    }
    // This method call Wallpapers
    public void getCuratedWallpapers(WallpapersListeners listeners,String page){
        CallWallpapersList callWallpapersList = retrofit.create(CallWallpapersList.class);
        Call<CuratedApiResponse> call = callWallpapersList.getWallpapers(page,"50");
        call.enqueue(new Callback<CuratedApiResponse>() {
            @Override
            public void onResponse(Call<CuratedApiResponse> call, Response<CuratedApiResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(context, "An error Occured", Toast.LENGTH_SHORT).show();
                }
                listeners.onFetch(response.body(),response.message());
            }

            @Override
            public void onFailure(Call<CuratedApiResponse> call, Throwable t) {
                listeners.onError(t.getMessage());
            }
        });
    }
    //interface for saerch
    private interface CallWallpaperSearchList{
        @Headers({
                "Accept: Application/json",
                "Authorization: 563492ad6f9170000100000132c3a46cf0344449a86c5c828895eaf3"
        })
        @GET("search")
        Call<SearchApiResponse> getWallpaperSearch(
                @Query("query") String query,
                @Query("page") String page,
                @Query("per_page") String per_page
        );
    }
    public void searchWallpapers(WallpapersListenersSearch searchListener, String page, String query){
        CallWallpaperSearchList callWallpaperSearchList = retrofit.create(CallWallpaperSearchList.class);
        Call<SearchApiResponse> call = callWallpaperSearchList.getWallpaperSearch(query,page,"50");
        call.enqueue(new Callback<SearchApiResponse>() {
            @Override
            public void onResponse(Call<SearchApiResponse> call, Response<SearchApiResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT).show();
                    return;
                }
                searchListener.onFetch(response.body(),response.message());
            }

            @Override
            public void onFailure(Call<SearchApiResponse> call, Throwable t) {
               searchListener.onError(t.getMessage());
            }
        });
                
    }

}

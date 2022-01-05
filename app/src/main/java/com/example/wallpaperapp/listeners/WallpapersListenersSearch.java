package com.example.wallpaperapp.listeners;

import com.example.wallpaperapp.models.CuratedApiResponse;
import com.example.wallpaperapp.models.SearchApiResponse;

public interface WallpapersListenersSearch {
    void onFetch(SearchApiResponse response,String message);
    void onError(String message);
}

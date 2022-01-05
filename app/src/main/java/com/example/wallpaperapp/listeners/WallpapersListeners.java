package com.example.wallpaperapp.listeners;

import com.example.wallpaperapp.models.CuratedApiResponse;

public interface WallpapersListeners {
    void onFetch(CuratedApiResponse response, String message);
    void onError(String message);
}

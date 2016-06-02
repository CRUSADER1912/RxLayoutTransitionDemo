package com.crusader.rxlayouttransitiondemo.data;

import com.crusader.rxlayouttransitiondemo.data.model.Photo;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

/**
 * Modeling the unsplash.it API.
 */
public interface UnsplashService {

    String ENDPOINT = "https://unsplash.it/";

    @GET("list")
    Observable<List<Photo>> getFeed();

}

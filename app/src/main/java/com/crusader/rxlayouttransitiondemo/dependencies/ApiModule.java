package com.crusader.rxlayouttransitiondemo.dependencies;

import com.crusader.rxlayouttransitiondemo.data.UnsplashService;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 * Created by CRUSADER on 6/19/2016.
 */

@Module
public class ApiModule {

    @Provides
    @CustomScope
    UnsplashService provideUnsplashService(Retrofit retrofit){
        return retrofit.create(UnsplashService.class);
    }
}

package com.crusader.rxlayouttransitiondemo.dependencies;

import javax.inject.Singleton;

import dagger.Component;
import retrofit.Retrofit;

/**
 * Created by CRUSADER on 6/19/2016.
 */
@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {
    Retrofit retrofit();
}

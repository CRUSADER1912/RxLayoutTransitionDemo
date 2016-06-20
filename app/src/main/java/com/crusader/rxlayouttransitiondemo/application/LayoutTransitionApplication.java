package com.crusader.rxlayouttransitiondemo.application;

import android.app.Application;

import com.crusader.rxlayouttransitiondemo.data.UnsplashService;
import com.crusader.rxlayouttransitiondemo.dependencies.ApiComponent;
import com.crusader.rxlayouttransitiondemo.dependencies.DaggerApiComponent;
import com.crusader.rxlayouttransitiondemo.dependencies.DaggerNetworkComponent;
import com.crusader.rxlayouttransitiondemo.dependencies.NetworkComponent;
import com.crusader.rxlayouttransitiondemo.dependencies.NetworkModule;

/**
 * Created by CRUSADER on 6/19/2016.
 */

public class LayoutTransitionApplication extends Application {

    private ApiComponent mApiComponent;

    @Override
    public void onCreate() {
        resolveDependency();
        super.onCreate();
    }

    public ApiComponent getmApiComponent() {
        return mApiComponent;
    }

    private void resolveDependency() {
        mApiComponent = DaggerApiComponent.builder()
                .networkComponent(getNetworkComponent())
                .build();
    }

    public NetworkComponent getNetworkComponent() {
        return DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule(UnsplashService.ENDPOINT))
                .build();
    }
}

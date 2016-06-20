package com.crusader.rxlayouttransitiondemo.dependencies;

import com.crusader.rxlayouttransitiondemo.activities.MainActivity;

import dagger.Component;

/**
 * Created by CRUSADER on 6/19/2016.
 */
@CustomScope
@Component(modules = ApiModule.class, dependencies = NetworkComponent.class)
public interface ApiComponent {

    void inject(MainActivity activity);
}

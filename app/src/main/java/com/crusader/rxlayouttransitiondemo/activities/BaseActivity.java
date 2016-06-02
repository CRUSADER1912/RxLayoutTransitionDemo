package com.crusader.rxlayouttransitiondemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by CRUSADER on 6/2/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetContentView();
        initComponents();
        initListeners();
    }

    public abstract void initSetContentView();

    public abstract void initComponents();

    public abstract void initListeners();

}

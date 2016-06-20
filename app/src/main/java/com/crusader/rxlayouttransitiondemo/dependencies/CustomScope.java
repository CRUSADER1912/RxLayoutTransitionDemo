package com.crusader.rxlayouttransitiondemo.dependencies;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by CRUSADER on 6/19/2016.
 */

@Scope
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CustomScope {
}

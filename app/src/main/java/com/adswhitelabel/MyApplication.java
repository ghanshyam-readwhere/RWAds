package com.adswhitelabel;

import android.app.Application;

import com.rwadswhitelabel.RwAdsIntialize;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RwAdsIntialize.init(this,"");
    }
}

package com.yuxi.yxscada;

import android.app.Application;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "ae7c4396b9d2a37c3d0b8a6b8d72b223");

    }
}

package com.fan.componentization.common;

import android.content.Context;

public interface IAppLike {

    int MAX_PRIORITY = 10;
    int MIN_PRIORITY = 1;
    int NORM_PRIORITY = 5;

    int getPriority();
    void onCreate(Context context);
    void onTerminate();
}
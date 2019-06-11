package com.example.android.camera2video;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by johnson on 6/10/19.
 */

public class CameraApplication extends Application {

    private static Application sInstance;
    private static Handler sHandler;
    @Override
    public void onCreate() {
        Log.i(TAG, "Camera App onCreate");
        sInstance = this;
        super.onCreate();
    }

    public static Application getsInstance() {
        return sInstance;
    }

    public static Handler getsHandler() {
        if (sHandler == null) {
            sHandler = new Handler(Looper.getMainLooper());
        }
        return sHandler;
    }
}

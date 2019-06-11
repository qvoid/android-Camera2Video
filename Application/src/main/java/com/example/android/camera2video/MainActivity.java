package com.example.android.camera2video;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private final static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (null == savedInstanceState) {
            FragmentManagerUtil.getInstance().setActivity(this);
            FragmentManagerUtil.getInstance().switchFragment(FragmentManagerUtil.FRAGMENT_TAG_HOME, savedInstanceState);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        if (FragmentManagerUtil.getInstance().getCurrentFragmentTag() != FragmentManagerUtil.FRAGMENT_TAG_HOME)
            FragmentManagerUtil.getInstance().switchFragment(FragmentManagerUtil.FRAGMENT_TAG_HOME, new Bundle());
        else {
            super.onBackPressed();
        }
    }
}

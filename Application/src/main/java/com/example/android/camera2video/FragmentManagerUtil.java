package com.example.android.camera2video;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by johnson on 6/11/19.
 */

public class FragmentManagerUtil {
    private final static String TAG = FragmentManagerUtil.class.getName();
    private static FragmentManagerUtil sInstance;

    public static final int FRAGMENT_TAG_HOME = 0;
    public static final int FRAGMENT_TAG_PROBE_CAMERA = 1;
    public static final int FRAGMENT_TAG_CAMERA_2_RAW = 2;
    public static final int FRAGMENT_TAG_CAMERA_2_VIDEO = 3;
    public static final int FRAGMENT_TAG_DECODER = 4;
    public static final int FRAGMENT_TAG_ENCODER = 5;

    private HomeFragment mHomeFragment = null;
    private Camera2RawFragment mCamera2RawFragment = null;
    private Camera2VideoFragment mCamera2VideoFragment = null;
    private CameraProbeFragment mCameraProbeFragment = null;

    private Activity mActivity = null;

    private int mCurrentFragmentTag = FRAGMENT_TAG_HOME;

    public FragmentManagerUtil() {

    }

    public synchronized static FragmentManagerUtil getInstance() {
        if (sInstance == null) {
            sInstance = new FragmentManagerUtil();
        }
        return sInstance;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    private void setFragment(Fragment fragment, Bundle bundle) {
        if (mActivity == null || fragment == null) {
            Log.e(TAG, "fragment or bundle is null");
            return;
        }

        fragment.setArguments(bundle);

        FragmentManager fm = mActivity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void switchFragment(int fragmentTag, Bundle bundle) {
        if (mActivity == null) {
            return;
        }

        Log.d(TAG, "switchFragment: " + fragmentTag);
        switch(fragmentTag) {
            case FRAGMENT_TAG_HOME:
                mHomeFragment = new HomeFragment();
                setFragment(mHomeFragment, bundle);
                break;
            case FRAGMENT_TAG_CAMERA_2_RAW:
                mCamera2RawFragment = new Camera2RawFragment();
                setFragment(mCamera2RawFragment, bundle);
                break;
            case FRAGMENT_TAG_CAMERA_2_VIDEO:
                mCamera2VideoFragment = new Camera2VideoFragment();
                setFragment(mCamera2VideoFragment, bundle);
                break;
            case FRAGMENT_TAG_PROBE_CAMERA:
                mCameraProbeFragment = new CameraProbeFragment();
                setFragment(mCameraProbeFragment, bundle);
                break;
        }
        mCurrentFragmentTag = fragmentTag;
    }

    public int getCurrentFragmentTag() {
        return mCurrentFragmentTag;
    }
}

package com.example.android.camera2video;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by johnson on 6/11/19.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = HomeFragment.class.getName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.camera_probe_btn).setOnClickListener(this);
        view.findViewById(R.id.camera_raw_btn).setOnClickListener(this);
        view.findViewById(R.id.camera_video_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_probe_btn:
                Log.d(TAG, "onClick: probe_btn");
                FragmentManagerUtil.getInstance().switchFragment(FragmentManagerUtil.FRAGMENT_TAG_PROBE_CAMERA, new Bundle());
                break;
            case R.id.camera_raw_btn:
                Log.d(TAG, "onClick: raw_btn");
                FragmentManagerUtil.getInstance().switchFragment(FragmentManagerUtil.FRAGMENT_TAG_CAMERA_2_RAW, new Bundle());
                break;
            case R.id.camera_video_btn:
                Log.d(TAG, "onClick: video_btn");
                FragmentManagerUtil.getInstance().switchFragment(FragmentManagerUtil.FRAGMENT_TAG_CAMERA_2_VIDEO, new Bundle());
                break;
        }
    }
}

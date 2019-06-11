package com.example.android.camera2video;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

import static android.hardware.camera2.CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES;
import static android.hardware.camera2.CameraCharacteristics.CONTROL_AE_LOCK_AVAILABLE;
import static android.hardware.camera2.CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES;
import static android.hardware.camera2.CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES;
import static android.hardware.camera2.CameraCharacteristics.CONTROL_AWB_LOCK_AVAILABLE;
import static android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_OFF;
import static android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON;
import static android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH;
import static android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH;
import static android.hardware.camera2.CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE;
import static android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_AUTO;
import static android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE;
import static android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO;
import static android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_EDOF;
import static android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_MACRO;
import static android.hardware.camera2.CameraMetadata.CONTROL_AF_MODE_OFF;
import static android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_AUTO;
import static android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_CLOUDY_DAYLIGHT;
import static android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_DAYLIGHT;
import static android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_FLUORESCENT;
import static android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_INCANDESCENT;
import static android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_OFF;
import static android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_SHADE;
import static android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_TWILIGHT;
import static android.hardware.camera2.CameraMetadata.CONTROL_AWB_MODE_WARM_FLUORESCENT;
import static android.hardware.camera2.CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_3;
import static android.hardware.camera2.CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_FULL;
import static android.hardware.camera2.CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY;
import static android.hardware.camera2.CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED;

/**
 * Created by johnson on 6/11/19.
 */

public class CameraProbeFragment extends Fragment {

    CameraManager mCameraManager;
    WebView mWebView;
    String result = "Probing ...";
    String fpos = "<font style=\"color:#00aa00;\">";
    String fneg = "<font style=\"color:#990000;\">";
    String check = "<div style=\"float:left;width:20px;color:#00aa00;\">&#x2713;</div>";
    String cross = "<div style=\"float:left;width:20px;color:#990000;\">&#x2717;</div>";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_probe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();

        mWebView = view.findViewById(R.id.textview_probe);
        mWebView.loadData(result, "text/html", "utf-8");
        mCameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        startProbe();
    }

    public void startProbe() {
        result = "";
        model();
        try {
            for (String cameraId : mCameraManager.getCameraIdList()) {
                result += "<br><b>CameraId: " + cameraId + "</b></br>";
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                general(characteristics);
                ae(characteristics);
                af(characteristics);
                awb(characteristics);
                check_raw(characteristics);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        mWebView.loadData(result, "text/html", "utf-8");
    }

    public void model() {
        result += "<b>Model</b><br>";
        result += "Model: " + Build.MODEL + "<br>";
        result += "Manufacturer: " + Build.MANUFACTURER + "<br>";
        result += "Build version: " + android.os.Build.VERSION.RELEASE + "<br>";
        result += "SDK version: " + android.os.Build.VERSION.SDK_INT + "<br>";
    }

    private static boolean contains(int[] modes, int mode) {
        if (modes == null) {
            return false;
        }
        for (int i : modes) {
            if (i == mode) {
                return true;
            }
        }
        return false;
    }

    public void check_raw(CameraCharacteristics characteristics){
        result += "<br><b>RAW capture</b><br>";
        if (contains(characteristics.get(
                CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES),
                CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW)){
            result += check + fpos + "RAW capture available</font><br style=\"clear:both;\">";
        }else{
            result += cross + fneg + "RAW capture NOT available</font><br style=\"clear:both;\">";
        }

    }

    public void general(CameraCharacteristics characteristics) {
        result += "<br><b>Hardware Level Support Category</b><br>";
        Integer mylevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        List<Pair> levels = new ArrayList<>();
        levels.add(new Pair<>(INFO_SUPPORTED_HARDWARE_LEVEL_3, "Level_3"));
        levels.add(new Pair<>(INFO_SUPPORTED_HARDWARE_LEVEL_FULL, "Full"));
        levels.add(new Pair<>(INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED, "Limited"));
        levels.add(new Pair<>(INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY, "Legacy"));

        /*
        Log.d("SL:", "Full:"+INFO_SUPPORTED_HARDWARE_LEVEL_FULL);
        Log.d("SL:", "Limited:"+INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED);
        Log.d("SL:", "Legacy:"+INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY);
        Log.d("SL:", "Level3:"+INFO_SUPPORTED_HARDWARE_LEVEL_3);
        */

        for (Pair<Integer, String> l : levels) {
            if (l.first == mylevel) {
                result += check + fpos + l.second + "</font><br style=\"clear:both;\">";
            } else {
                result += cross + fneg + l.second + "</font><br style=\"clear:both;\">";
            }
        }

    }

    public void awb(CameraCharacteristics characteristics) {
        result += "<br><b>Whitebalance</b><br>";
        List<Pair> ml = new ArrayList<>();
        ml.add(new Pair<>(CONTROL_AWB_MODE_OFF, "Whitebalance off"));
        ml.add(new Pair<>(CONTROL_AWB_MODE_AUTO, "Automatic whitebalance"));
        ml.add(new Pair<>(CONTROL_AWB_MODE_CLOUDY_DAYLIGHT, "WB: cloudy day"));
        ml.add(new Pair<>(CONTROL_AWB_MODE_DAYLIGHT, "WB: day"));
        ml.add(new Pair<>(CONTROL_AWB_MODE_FLUORESCENT, "WB: fluorescent"));
        ml.add(new Pair<>(CONTROL_AWB_MODE_INCANDESCENT, "WB: incandescent"));
        ml.add(new Pair<>(CONTROL_AWB_MODE_SHADE, "WB: shade"));
        ml.add(new Pair<>(CONTROL_AWB_MODE_TWILIGHT, "WB: twilight"));
        ml.add(new Pair<>(CONTROL_AWB_MODE_WARM_FLUORESCENT, "WB: warm fluorescent"));

        int[] tmp = characteristics.get(CONTROL_AWB_AVAILABLE_MODES);
        List<Integer> aelist = new ArrayList<Integer>();
        for (int index = 0; index < tmp.length; index++) {
            aelist.add(tmp[index]);
        }

        for (Pair<Integer, String> kv : ml) {
            if (aelist.contains(kv.first)) {
                result += check + fpos + kv.second + "</font><br style=\"clear:both;\">";
            } else {
                result += cross + fneg + kv.second + "</font><br style=\"clear:both;\">";
            }
        }

        try {
            if (characteristics.get(CONTROL_AWB_LOCK_AVAILABLE)) {
                result += check + fpos + "AWB Lock" + "</font><br style=\"clear:both;\">";
            } else {
                result += cross + fneg + "AWB Lock" + "</font><br style=\"clear:both;\">";
            }
        }catch(Exception e){}
    }

    public void af(CameraCharacteristics characteristics) {
        result += "<br><b>Focus</b><br>";
        // not able to get the enum/key names from the ints,
        // so I am doing it myself
        List<Pair> ml = new ArrayList<>();
        ml.add(new Pair<>(CONTROL_AF_MODE_OFF, "Manual focus"));
        ml.add(new Pair<>(CONTROL_AF_MODE_AUTO, "Auto focus"));
        ml.add(new Pair<>(CONTROL_AF_MODE_MACRO, "Auto focus macro"));
        ml.add(new Pair<>(CONTROL_AF_MODE_CONTINUOUS_PICTURE, "Auto focus continuous picture"));
        ml.add(new Pair<>(CONTROL_AF_MODE_CONTINUOUS_VIDEO, "Auto focus continuous video"));
        ml.add(new Pair<>(CONTROL_AF_MODE_EDOF, "Auto focus EDOF"));

        int[] tmp = characteristics.get(CONTROL_AF_AVAILABLE_MODES);
        List<Integer> aelist = new ArrayList<Integer>();
        for (int index = 0; index < tmp.length; index++) {
            aelist.add(tmp[index]);
        }

        for (Pair<Integer, String> kv : ml) {
            if (aelist.contains(kv.first)) {
                result += check + fpos + kv.second + "</font><br style=\"clear:both;\">";
            } else {
                result += cross + fneg + kv.second + "</font><br style=\"clear:both;\">";
            }
        }
    }

    public void ae(CameraCharacteristics characteristics) {
        result += "<br><b>Exposure</b><br>";
        // not able to get the enum/key names from the ints,
        // so I am doing it myself
        List<Pair> ml = new ArrayList<>();
        ml.add(new Pair<>(CONTROL_AE_MODE_OFF, "Manual exposure"));
        ml.add(new Pair<>(CONTROL_AE_MODE_ON, "Auto exposure"));
        ml.add(new Pair<>(CONTROL_AE_MODE_ON_ALWAYS_FLASH, "Auto exposure, always flash"));
        ml.add(new Pair<>(CONTROL_AE_MODE_ON_AUTO_FLASH, "Auto exposure, auto flash"));
        ml.add(new Pair<>(CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE, "Auto exposure, auto flash redeye"));

        int[] tmp = characteristics.get(CONTROL_AE_AVAILABLE_MODES);
        List<Integer> aelist = new ArrayList<Integer>();
        for (int index = 0; index < tmp.length; index++) {
            aelist.add(tmp[index]);
        }

        for (Pair<Integer, String> kv : ml) {
            if (aelist.contains(kv.first)) {
                result += check + fpos + kv.second + "</font><br style=\"clear:both;\">";
            } else {
                result += cross + fneg + kv.second + "</font><br style=\"clear:both;\">";
            }
        }

        try {
            if (characteristics.get(CONTROL_AE_LOCK_AVAILABLE)) {
                result += check + fpos + "AE Lock" + "</font><br style=\"clear:both;\">";
            } else {
                result += cross + fneg + "AE Lock" + "</font><br style=\"clear:both;\">";
            }
        }catch(Exception e){}

    }
}

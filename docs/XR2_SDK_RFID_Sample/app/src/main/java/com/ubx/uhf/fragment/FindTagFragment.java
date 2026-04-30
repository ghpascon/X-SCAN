package com.ubx.uhf.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ubx.rfid.bean.TagScan;
import com.ubx.uhf.R;
import com.ubx.uhf.activity.MainActivity;
import com.ubx.uhf.adapter.AdapterScanList;
import com.ubx.uhf.base.BaseApplication;
import com.ubx.uhf.utils.CircleProgress;
import com.ubx.uhf.utils.SoundTool;
import com.ubx.uhf.utils.ToastUtil;
import com.ubx.usdk.RFIDSDKManager;
import com.ubx.usdk.bean.Tag6C;
import com.ubx.usdk.rfid.aidl.IRfidCallback;
import com.ubx.usdk.util.LogUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindTagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindTagFragment extends Fragment implements View.OnClickListener {

    private CircleProgress mCircleProgress;
    TextView epcid;
    public Button btFinding;
    private volatile boolean mWorking = true;
    private volatile Thread mThread = null;
    Handler handler;
    int rssi = 0;
    private String locationEpcSelect;
    private static MainActivity mActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindTagFragment newInstance(MainActivity activity) {
        mActivity = activity;
        FindTagFragment fragment = new FindTagFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_tag, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCircleProgress = view.findViewById(R.id.circle_progress);
        epcid = view.findViewById(R.id.epc_id);
        btFinding = view.findViewById(R.id.btfind);
        btFinding.setOnClickListener(this);
        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                try {
                    switch (msg.what) {
                        case 0:
                            String rssistr = msg.obj + "";
                            int rssi = (int) Integer.valueOf(rssistr);
                            mCircleProgress.setValue((float) rssi);
                            break;
                        default:
                            break;
                    }
                } catch (Exception ex) {
                    ex.toString();
                }
            }
        };

    }

    @Override
    public void onClick(View view) {
        if (view == btFinding) {
            locationEpcSelect = epcid.getText().toString();
            if (TextUtils.isEmpty(locationEpcSelect)){
                ToastUtil.toast(getString(R.string.choice_epc_first));
                return;
            }
            readTag();
        }
    }

    private void readTag() {
        if (btFinding.getText().toString().equals(getString(R.string.finding))) {
            if (mThread == null) {
                mWorking = true;
                btFinding.setText(R.string.btstop);
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mWorking) {
                            Tag6C mtag = RFIDSDKManager.getInstance().getRfidManager().findEpc(locationEpcSelect);
                            if (mtag != null) {
                                rssi = mtag.rssi;
                                SoundTool.getInstance(BaseApplication.getContext()).playBeep(1);
                                Message msg = handler.obtainMessage();
                                msg.what = 0;
                                msg.obj = rssi + "";
                                handler.sendMessage(msg);
                            } else {
                                if (rssi > 0)
                                    rssi -= 2;
                                if (rssi < 0) rssi = 0;
                                Message msg = handler.obtainMessage();
                                msg.what = 0;
                                msg.obj = rssi + "";
                                handler.sendMessage(msg);
                            }
                        }
                    }
                });
                mThread.start();
            }
        } else {
            stopThread();
        }
    }

    private void stopThread(){
        if (mThread != null) {
            mWorking = false;
//                try {
//                    mThread.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            mThread = null;
            btFinding.setText(R.string.finding);
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }


    @Override
    public void onResume() {
        mCircleProgress.setValue(0.00f);
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            epcid.setText(mActivity.EpcSelect);
        } else {
            stopThread();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
    }



}
package com.ubx.uhf.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.ubx.uhf.R;
import com.ubx.uhf.fragment.TagScanFragment;
import com.ubx.uhf.utils.ToastUtil;
import com.ubx.usdk.RFIDSDKManager;
import com.ubx.usdk.USDKManager;
import com.ubx.usdk.listener.GripStateListener;
import com.ubx.usdk.listener.InitListener;
import com.ubx.usdk.rfid.RFIDGripManager;
import com.ubx.usdk.rfid.RfidManager;
import com.ubx.usdk.util.LogUtils;


public class BaseApplication extends Application {
    private static Context mContext;
    private int countActivity = 0;
    //Monitor if application enters background
    private boolean isBackground = false;

    private String TAG = BaseApplication.class.getSimpleName();

    private static RfidManager rfidManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ToastUtil.init(mContext);
        //Monitor if application enters background and returns to foreground
        initBackgroundCallBack();
        initRfid();
    }



    public void initRfid() {

        RFIDSDKManager.getInstance().power(true);//true:Module is powered on  false:Module is off


        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);

                boolean status =  RFIDSDKManager.getInstance().connect();

                Log.i(TAG, "initRfid()  status : "+status);
                if ( status) {
                    rfidManager = RFIDSDKManager.getInstance().getRfidManager();
                }else {
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.toast(status?"RFID initialization successful":"RFID initialization failed");
                    }
                });

            }
        }).start();


    }

    public static RfidManager getRFIDManager() {
        return rfidManager;
    }

    public static Context getContext() {
        return mContext;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG, "onTerminate()");
        rfidManager = null;
    }

    private void initBackgroundCallBack() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                countActivity++;
                if (countActivity == 1 && isBackground) {
                    Log.i(TAG, "RFIDDemo in foreground");
                    isBackground = false;

                    //Long distance device, open the handle button to control the scanning head light）
                    RFIDSDKManager.getInstance().enableScanHead(false);
                    //Indicates that the application has re-entered the foreground
                    RFIDSDKManager.getInstance().init(new InitListener() {
                        @Override
                        public void onStatus(boolean status) {
                            Log.i(TAG, "initRfid()  status : "+status);
                            if ( status) {
                                rfidManager = RFIDSDKManager.getInstance().getRfidManager();
                            }else {
                            }
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.toast(status?"RFID initialization successful":"RFID initialization failed");
                                }
                            });
                        }
                    });
                }

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                countActivity--;
                if (countActivity <= 0 && !isBackground) {
                    Log.i(TAG, "RFIDDemo enters the background");
                    isBackground = true;

                    //Turn on the handle button to trigger the light emission of the scanning head. For long-distance equipment, turn on the handle button to control the light emission of the scanning head.（Long distance device, open the handle button to control the scanning head light）
                    RFIDSDKManager.getInstance().enableScanHead(true);
                    //Indicates that the application has entered the background
                    RFIDSDKManager.getInstance().disConnect();
                    RFIDSDKManager.getInstance().power(false);
                }

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }



}

package com.ubx.uhf.activity;

import android.device.scanner.configuration.PropertyID;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.ubx.rfid.bean.TagScan;
import com.ubx.uhf.R;
import com.ubx.uhf.adapter.AdapterSectionsPager;
import com.ubx.uhf.base.BaseApplication;
import com.ubx.uhf.fragment.FindTagFragment;
import com.ubx.uhf.fragment.SettingFragment;
import com.ubx.uhf.fragment.TagManageFragment;
import com.ubx.uhf.fragment.TagScanFragment;
import com.ubx.uhf.utils.SoundTool;
import com.ubx.uhf.utils.ToastUtil;
import com.ubx.usdk.RFIDSDKManager;
import com.ubx.usdk.grip.ModelStatus;
import com.ubx.usdk.grip.RFID53RStatusCallBack;
import com.ubx.usdk.listener.GripStateListener;
import com.ubx.usdk.rfid.RFIDGripManager;
import com.ubx.usdk.util.LogUtils;
import com.ubx.usdk.util.QueryMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "RFIDTAG";

    public List<String> mDataParents;
    public List<TagScan> tagScanSpinner;
    private List<Fragment> fragments ;

    private ViewPager viewPager ;
    private TabLayout tabs;

    public String EpcSelect = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SoundTool.getInstance(BaseApplication.getContext());
        mDataParents = new ArrayList<>();
        tagScanSpinner = new ArrayList<>();
        fragments = Arrays.asList(TagScanFragment.newInstance(MainActivity.this)
                , TagManageFragment.newInstance(MainActivity.this)
                , FindTagFragment.newInstance(MainActivity.this)
                , SettingFragment.newInstance(MainActivity.this));
        AdapterSectionsPager adapterSectionsPager = new AdapterSectionsPager(this, getSupportFragmentManager(), fragments);
        viewPager =  findViewById(R.id.view_pager);
        viewPager.setAdapter(adapterSectionsPager);
        viewPager.setOffscreenPageLimit(4);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);



    }

    @Override
    protected void onResume() {
        super.onResume();
    }






    /**
     * Set query mode
     * @param mode
     */
    private void setQueryMode(int mode){
        if ( RFIDSDKManager.getInstance().getRfidManager() != null) {
            RFIDSDKManager.getInstance().getRfidManager().setQueryMode(QueryMode.EPC_TID);
        }
    }

    /**
     * Write labels by TID
     */
    private void writeTagByTid(){
        if ( RFIDSDKManager.getInstance().getRfidManager() != null) {
            String tid = "24 length TID";
            String writeData = "need write EPC datas ";
            RFIDSDKManager.getInstance().getRfidManager().writeTagByTid(tid, 0, 2, "00000000", writeData);
        }
    }

    /**
     * Press back twice to exit
     */
    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {


                releaseAll();
                System.exit(0);
            } else {
                ToastUtil.toast(getString(R.string.press_again_exit_app));
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void releaseAll(){
        Log.d(TAG, "releaseAll() ");
        RFIDSDKManager.getInstance().disConnect();
        RFIDSDKManager.getInstance().power(false);

        SoundTool.getInstance(BaseApplication.getContext()).release();//sound playback
    }


    @Override
    protected void onPause() {
        SoundTool.getInstance(BaseApplication.getContext()).release();//sound playback
        super.onPause();
    }

    /**
     * Set the inventory time
     * @param interal 0-200 ms
     */
    private void setScanInteral(int interal){
        if ( RFIDSDKManager.getInstance().getRfidManager() != null) {
            int setScanInterval = RFIDSDKManager.getInstance().getRfidManager().setScanInterval(interal);
            Log.v(TAG, "--- setScanInterval()   ----" + setScanInterval);
        }
    }

    /**
     * Get inventory time
     */
    private void getScanInteral(){
        if ( RFIDSDKManager.getInstance().getRfidManager() != null) {
            int getScanInterval = RFIDSDKManager.getInstance().getRfidManager().getScanInterval();
            Log.v(TAG, "--- getScanInterval()   ----" + getScanInterval);
        }
    }



//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        return super.onPrepareOptionsMenu(menu);//Method called before popup menu
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_rate_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_6b:
//                Intent intent = new Intent(MainActivity.this,  Activity6BTag.class);
//                startActivity(intent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//        if( event.getKeyCode() == 523 &&  event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0){
//            //TODO Monitor Button Press    Side scan button is：521、520
//            return true;
//        }else  if(event.getKeyCode() == 523 &&  event.getAction() == KeyEvent.ACTION_UP && event.getRepeatCount() == 0){
//            //TODO Monitor Button Release     Side scan button is：521、520
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == 523 &&  event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0){
            //TODO Press to start inventory
            if (viewPager.getCurrentItem()==0) {
                TagScanFragment fragment = (TagScanFragment) fragments.get(0);
                fragment.scanStartBtn.callOnClick();
            }else if (viewPager.getCurrentItem()==2){
                FindTagFragment fragment = (FindTagFragment) fragments.get(2);
                fragment.btFinding.callOnClick();
            }
            return true;
        }else if (event.getKeyCode() == 523 &&  event.getAction() == KeyEvent.ACTION_UP && event.getRepeatCount() == 0){
            //TODO Release to stop inventory
            return true;
        }
        return super.dispatchKeyEvent(event);
    }



}
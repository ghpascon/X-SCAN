package com.ubx.uhf.fragment;


import android.device.DeviceManager;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import com.ubx.uhf.utils.SoundTool;
import com.ubx.uhf.utils.ToastUtil;
import com.ubx.usdk.RFIDSDKManager;
import com.ubx.usdk.bean.RfidParameter;
import com.ubx.usdk.listener.GripStateListener;
import com.ubx.usdk.rfid.RFIDGripManager;
import com.ubx.usdk.rfid.aidl.IRfidCallback;
import com.ubx.usdk.util.LogUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TagScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagScanFragment extends Fragment {

    public static final String TAG =  TagScanFragment.class.getSimpleName();
    private List<TagScan> data;
    private HashMap<String, TagScan> mapData;
    private ScanCallback callback;
    private AdapterScanList adapterScanList;
    private static MainActivity mActivity;
    private int tagTotal = 0;
    private int errorCount = 0;

    private String firmware ;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TagScanFragment newInstance(MainActivity activity) {
        mActivity = activity;
        TagScanFragment fragment = new TagScanFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag_scan, container, false);
    }

    public Button scanStartBtn,clearBtn;
    private RecyclerView scanListRv;
    private TextView scanCountText, scanTotalText;
    public TextView textFirmware;
    private Spinner spinnerMode;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerMode = view.findViewById(R.id.spinner_inventory_mode);
        scanStartBtn = view.findViewById(R.id.scan_start_btn);
        clearBtn =  view.findViewById(R.id.btn_clear);
        scanListRv = view.findViewById(R.id.scan_list_rv);
        scanCountText = view.findViewById(R.id.scan_count_text);
        scanTotalText = view.findViewById(R.id.scan_total_text);
        textFirmware = view.findViewById(R.id.text_firmware);


        scanStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String node1 = "/sys/devices/soc/c170000.serial/pogo_uart";
//                isChange(node1);
//                String node2 = "/sys/devices/soc/soc:sectrl/ugp_ctrl/gp_pogo_5v_ctrl/enable";
//                isChange(node2);

//                String node53x = "/sys/kernel/kobject_pogo_otg_status/pogo_otg5v_en";
//                isChange(node53x);

//                String node55_5G = "/sys/devices/platform/otg_iddig/pogo_5v";
//                isChange(node55_5G);

//                if (node1!=null){
//                    return;
//                }

                if (BaseApplication.getRFIDManager()!=null) {

                    if (scanStartBtn.getText().equals(getString(R.string.btInventory))) {
                        setCallback();
                        scanStartBtn.setText(getString(R.string.btn_stop_Inventory));
                        setScanStatus(true);
                    } else {
                        scanStartBtn.setText(getString(R.string.btInventory));
                        setScanStatus(false);
                    }
                } else {
                    Log.d(TAG, "scanStartBtn  RFID is not initialized ");
                    Toast.makeText(getActivity(), "RFID Not initialized", Toast.LENGTH_SHORT).show();
                }

            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tagTotal = 0;
           if (mapData != null) {
                mapData.clear();
            }
            if (mActivity.mDataParents != null) {
                mActivity.mDataParents.clear();
            }
            if (mActivity.tagScanSpinner != null) {
                mActivity.tagScanSpinner.clear();
            }
            if (data != null) {
                data.clear();
                adapterScanList.setData(data);
            }

                showView();


            }
        });

        mapData = new HashMap<>();
        spinnerMode.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if ( BaseApplication.getRFIDManager() != null) {
                             BaseApplication.getRFIDManager().setQueryMode(position);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );


        scanListRv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        scanListRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapterScanList = new AdapterScanList(null, getActivity());
        scanListRv.setAdapter(adapterScanList);

    }

    private final int MSG_UPDATE_UI = 0;
    private final int MSG_STOP_INVENTORY = 1;

    private final int MSG_UPDATE_SINGLE_INV = 2 ;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_UI:
                    adapterScanList.notifyDataSetChanged();
                    handlerUpdateUI();
                    break;
                case MSG_STOP_INVENTORY:
                    adapterScanList.notifyDataSetChanged();
                    mHandler.removeCallbacksAndMessages(null);
                    if (scanStartBtn.getText().equals(mActivity.getString(R.string.btn_stop_Inventory))) {
                        scanStartBtn.setText(getContext().getString(R.string.btInventory));
                    }
                    break;

                case MSG_UPDATE_SINGLE_INV:
                     BaseApplication.getRFIDManager().inventorySingle();
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_SINGLE_INV,200);
                    break;
            }

        }
    };



    @Override
    public void onStart() {
        super.onStart();


    }

    private String mmEPC = "";

    private void setScanStatus(boolean isScan) {
        if (isScan) {
            Log.v(TAG, "--- startInventory()   ----");
            handlerUpdateUI();
            try {
                BaseApplication.getRFIDManager().startRead();//It is recommended to use: 0 when counting a small number of tags; when counting more than 100-200 tags, it is recommended to use：1.
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.v(TAG, "--- stopInventory()   ----");
            if ( BaseApplication.getRFIDManager() != null ) {
                BaseApplication.getRFIDManager().stopInventory();

            }
            handlerStopUI();
        }

    }

    /**
     * Control A/B side reading
     */
    private void myScan(){

//               int ret = BaseApplication.getRFIDManager().inventoryOnce((byte)1,(byte)4,(byte)0,(byte)6,(byte)0,(byte)50);//自定义扫描
//                Log.v(TAG, " inventoryOnce()   ret == "+ret);

        byte[] readAdr = new byte[]{ 0x00 , 0x06 };//Fixed 2 bytes, the first byte is the high bit, the second byte is the low bit, for example, the length is 6
        int ret = BaseApplication.getRFIDManager().inventoryOnceMix(1,4,0,50,2,readAdr, 6,  "00000000" );//Custom scan
        Log.v(TAG, " InventoryOnceMix()   ret == "+ret);

    }

    private void handlerUpdateUI() {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_UI, 500);
        }
    }

    private void handlerStopUI() {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(MSG_STOP_INVENTORY, 200);
        }
    }


    private long time = 0l;

    /**
     * Read EPC or tid individually
     */
    private void inventorySingle() {
         BaseApplication.getRFIDManager().inventorySingle();
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(MSG_STOP_INVENTORY, 10);
        }
    }


    /**
     * Set mask (tag filter inventory)
     */
    private void setTagMask() {
         BaseApplication.getRFIDManager().addMask(2, 24, 16, "7020");
    }


    /**
     * Write tag data via TID
     *
     * @param TID     Selected TID
     * @param Mem     Label area: 0-Password area, the first 2 characters are the destruction password, the last 2 characters are the access password      1-EPC area   2-TID area    3-User area
     * @param WordPtr Starting word address for writing
     * @param pwd     password
     * @param datas   Data to be written
     */
    private void writeTagByTid(String TID, int Mem, int WordPtr, String pwd, String datas) {
//                String TID = "E280110C20007642903D094D";
//               String pwd =  "00000000";
//                String datas = "1111111111111111";
        int ret =  BaseApplication.getRFIDManager().writeTagByTid(TID,  1,  2, pwd, datas);
        if (ret == -6) {
            Toast.makeText(mActivity, getContext().getString(R.string.gj_no_support), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Write EPC to a tag randomly
     *
     * @param epc      EPC value to be written hexadecimal string
     * @param password Tag access password
     */
    private void writeEpcString(String epc, String password) {
         BaseApplication.getRFIDManager().writeEpcString(epc, password);
    }


    class ScanCallback implements IRfidCallback {
        @Override
        public void onInventoryTag(String EPC, final String TID, final String strRSSI) {
            Log.e(TAG, "onInventoryTag:............... epc:" + EPC + "    tid:" + TID);
            mmEPC = EPC;
            notiyDatas(EPC, TID, strRSSI);

        }

        /**
         * Inventory end callback(Inventory Command Operate End)
         */
        @Override
        public void onInventoryTagEnd() {
            Log.d(TAG, "onInventoryTagEnd()");

        }
    }

    int num = 0;

    private void notiyDatas(  String s2,   String TID, final String strRSSI) {
        final String mapContainStrFinal = s2+TID;
        Log.d(TAG, "onInventoryTag: EPC: " + s2);

        SoundTool.getInstance(BaseApplication.getContext()).playBeep(1);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                {
                    if (mapData.containsKey(mapContainStrFinal)) {
                        TagScan tagScan = mapData.get(mapContainStrFinal);
                        tagScan.setCount(mapData.get(mapContainStrFinal).getCount() + 1);
//                    tagScan.setTid(TID);
                        tagScan.setRssi(strRSSI);
                        mapData.put(mapContainStrFinal, tagScan);
                    } else {
                        mActivity.mDataParents.add(s2);

                        TagScan tagScan = new TagScan(s2, TID, strRSSI, 1);
                        mapData.put(mapContainStrFinal, tagScan);
                        mActivity.tagScanSpinner.add(tagScan);
                    }

                    tagTotal++;
                    data = new ArrayList<>(mapData.values());
//                        Log.d(TAG, "onInventoryTag: data = " + Arrays.toString(data.toArray()));

                    showView();
                    /*long nowTime = System.currentTimeMillis();
                    if ((nowTime - time)>500){
                        time = nowTime;
                        data = new ArrayList<>(mapData.values());
//                        Log.d(TAG, "onInventoryTag: data = " + Arrays.toString(data.toArray()));
                        scanListAdapterRv.setData(data);
                    }*/


                }
            }
        });
    }


    private void showView(){
        scanTotalText.setText(tagTotal + "");
        adapterScanList.setData(data);
        scanCountText.setText(mapData.keySet().size() + "");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            setCallback();
            showFirmware();
        } else {
            if ( BaseApplication.getRFIDManager() != null) {
                if (scanStartBtn!=null) {
                    scanStartBtn.setText(getContext().getResources().getString(R.string.btInventory));
                }
                setScanStatus(false);
            }
//            Log.e(TAG, "setUserVisibleHint: Scan to release sound resources..........." );
//            SoundTool.getInstance(BaseApplication.getContext()).release();

        }



    }

    /**
     * Custom read USER length
     */
    private void setEPCAndUser(){
        //Set read EPC+USER
        RfidParameter parameter = RFIDSDKManager.getInstance().getRfidManager().getInventoryParameter();
        parameter.IvtType = 1;
        parameter.Memory = 0x03;
        parameter.WordPtr = 0x00;
        parameter.Length = 6;//Set the length of reading USER, which can be changed according to needs
        RFIDSDKManager.getInstance().getRfidManager().setInventoryParameter(parameter);
    }

    private void showFirmware(){
        Log.i(TAG, "showFirmware()  "+BaseApplication.getRFIDManager());
            if (TextUtils.isEmpty(firmware)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if ( BaseApplication.getRFIDManager() != null) {
                            Log.i(TAG, "getFirmwareVersion()");
                            firmware = BaseApplication.getRFIDManager().getFirmwareVersion();
                            try {
                                textFirmware.setText(getString(R.string.firmware) + firmware);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, 2500);

            }
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ........");
        if ( BaseApplication.getRFIDManager() != null) {
            setScanStatus(false);
        }
    }


    public void setCallback() {
        if ( BaseApplication.getRFIDManager() != null) {

            if (callback == null) {
                callback = new ScanCallback();
            }
             BaseApplication.getRFIDManager().registerCallback(callback);
        }
    }

//-----------------------------------------
private  boolean isChange(String nodepath_pogo5ven ) {


    try {

        FileInputStream fileInputStream = new FileInputStream(nodepath_pogo5ven);
        byte[] b = new byte[1024];
        String nodeStr = "";
        //Start reading file
        int len = fileInputStream.read(b);
        if (len > 0) {
            nodeStr = new String(b, 0, len);
        }
        LogUtils.v("OtgUtils", "isChange()  nodeStr:" + "    " + nodeStr );
    } catch (IOException e) {
        e.printStackTrace();
        LogUtils.e("OtgUtils", "isChange() " + nodepath_pogo5ven + "   Exception:" + e.getMessage());
    }
    return true;
}

}
package com.ubx.uhf.fragment;

import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ubx.rfid.bean.TagScan;
import com.ubx.uhf.R;
import com.ubx.uhf.activity.MainActivity;
import com.ubx.uhf.adapter.AdapterManageList;
import com.ubx.uhf.base.BaseApplication;
import com.ubx.uhf.utils.ByteUtils;
import com.ubx.uhf.utils.SoundTool;
import com.ubx.uhf.utils.ToastUtil;
import com.ubx.usdk.RFIDSDKManager;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TagManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagManageFragment extends Fragment {

    public static final String TAG =  TagManageFragment.class.getSimpleName();

    /**
     * (0x00:RESERVED, 0x01:EPC, 0x02:TID, 0x03:USER)
     */
    private int btMemBank;
    private AdapterManageList adapterManageList;
    private static MainActivity mActivity;
    private ArrayAdapter epcArrayAdapter;
    private List<TagScan> data;
    private HashMap<String, TagScan> map = new HashMap<>();

    private RecyclerView manageListRv;
    private Spinner manageBankSpinner,manageEpcDatasSpinner;
    private TextView tvChoiceEpcTid,tvWriteTitle;
    private EditText manageWriteEdit,manageCntEdit,manageAddressEdit,managePasswordEdit;
    private Button manageReadBtn,manageWriteBtn;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TagManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TagManageFragment newInstance(MainActivity activity) {
        mActivity = activity;
        TagManageFragment fragment = new TagManageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag_manage, container, false);
    }

    @Override
    public void onViewCreated(  View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        manageListRv = view.findViewById(R.id.manage_list_rv);
        manageBankSpinner = view.findViewById(R.id.manage_bank_spinner);
        manageEpcDatasSpinner = view.findViewById(R.id.manage_epc_datas_spinner );

        manageCntEdit = view.findViewById(R.id.manage_cnt_edit);
        manageAddressEdit= view.findViewById(R.id.manage_address_edit);
        managePasswordEdit= view.findViewById(R.id.manage_password_edit);

        tvWriteTitle= view.findViewById(R.id.write_title);

        tvChoiceEpcTid = view.findViewById(R.id.tv_choice_epc_tid);
        manageWriteEdit = view.findViewById(R.id.manage_write_edit);

        manageReadBtn  = view.findViewById(R.id.manage_read_btn);
        manageWriteBtn= view.findViewById(R.id.manage_write_btn);

        manageListRv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        manageListRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapterManageList = new AdapterManageList(null, getActivity());
        manageListRv.setAdapter(adapterManageList);

        initEvents();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initEvents() {


        manageWriteEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {




            }

            @Override
            public void afterTextChanged(Editable editable) {
                    String content = manageWriteEdit.getText().toString();
                    if (TextUtils.isEmpty(content)){
                        tvWriteTitle.setText("(0)"+getActivity().getString(R.string.writestr));
                    }else {
                        tvWriteTitle.setText("("+content.length()+")"+getActivity().getString(R.string.writestr));
                    }
            }
        });

        manageBankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        btMemBank = 0x00;
                        break;
                    case 1:
                        btMemBank = 0x01;
                        break;
                    case 2:
                        btMemBank = 0x02;
                        break;
                    case 3:
                        btMemBank = 0x03;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        manageBankSpinner.setSelection(1, true);


        epcArrayAdapter =new ArrayAdapter(mActivity,android.R.layout.simple_spinner_dropdown_item,mActivity.tagScanSpinner){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(getActivity(),R.layout.spinner_epc_tid_item,null);//获得Spinner布局View
                if(convertView!=null)
                {
                    TextView tvEpc =  convertView.findViewById(R.id.sp_epc_item);
                    TextView tvTid =  convertView.findViewById(R.id.sp_tid_item);
                    try
                    {
                        String epc = mActivity.tagScanSpinner.get(position).getEpc();
                        mActivity.EpcSelect = epc;
//                        String tid = mActivity.tagScanSpinner.get(position).getTid();
                        tvEpc.setText("EPC:"+epc);
//                        tvTid.setText("TID:"+tid);
                        tvTid.setVisibility(View.INVISIBLE);
                    }catch (Exception e){}

                }
                return convertView;
            }
        };
        //Give Spinner set adapter
        manageEpcDatasSpinner.setAdapter(epcArrayAdapter);
        manageEpcDatasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override//Override the event when Item is selected
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                List<TagScan> datas = mActivity.tagScanSpinner;
                if (datas!=null && datas.size()>0){
                    TagScan tagScan = datas.get(position);
                    String tid =  tagScan.getTid();
                    if (!TextUtils.isEmpty(tid)){

                    }else {

                    }

                    String epc =  tagScan.getEpc();
                    mActivity.EpcSelect = epc;
                    tvChoiceEpcTid.setText(epc);
                    manageWriteEdit.setText(epc+"");
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        tvChoiceEpcTid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageEpcDatasSpinner.performClick();
            }
        });

        manageReadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.getRFIDManager()==null) {
                    toast(getString(R.string.rfid_not_init));
                    return;
                }

                String s_epc = tvChoiceEpcTid.getText().toString().trim();
                String strPwd = managePasswordEdit.getText().toString();
                String s_address = manageAddressEdit.getText().toString();
                String s_length = manageCntEdit.getText().toString();
                if (  strPwd.equals("") || s_address.equals("") || s_length.equals("")) {
                    toast("Please fill in the parameters first.");
                    return;
                }

                map.clear();
                byte cnt = Integer.valueOf(s_length).byteValue();
                byte address = Integer.valueOf(s_address).byteValue();



                int mem = manageBankSpinner.getSelectedItemPosition();
                Log.d(TAG, "initEvents: epc:"+s_epc +"  cnt =  "+ cnt + ", address = " + address + ", strPwd = " + strPwd  );

                String dataRead = BaseApplication.getRFIDManager().readTag(s_epc, (byte) mem, address, cnt, strPwd);

//                String epc = "";//If no EPC is passed in, a piece of tag data will be read randomly. If the specified EPC is passed in, the data corresponding to the EPC will be read.
//                int ptrStr = 0;
//                int cntStr = 15;
//                String pwdStr = "00000000";
//                int Bank=3;
//                String dataRead = RFIDSDKManager.getInstance().getRfidManager().readTag(epc,  Bank, ptrStr, cntStr, pwdStr);

                Log.e(TAG, "onClick: ......");
                if (TextUtils.isEmpty(dataRead)) {
                    Log.e(TAG, "onClick: ......");
                    toast("read tag fail");
                } else {
                    toast("read tag success");
                    readTag(s_epc, dataRead);
                }
                //1fb1f280951t
            }
        });
        manageWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "initEvents: read tag");
                if(BaseApplication.getRFIDManager()==null) {
                    toast(getString(R.string.rfid_not_init));
                    return;
                }
                //map.clear();
                String s_epc = tvChoiceEpcTid.getText().toString().trim();
                String s_pwd = managePasswordEdit.getText().toString();
                String s_address = manageAddressEdit.getText().toString();
                String s_length = manageCntEdit.getText().toString();

                if (  s_pwd.equals("") || s_address.equals("") || s_length.equals("")) {
                    toast("Please fill in the parameters first.");
                    return;
                }

                String pwd = managePasswordEdit.getText().toString();
                int add = Integer.parseInt(manageAddressEdit.getText().toString());
                int cnt = Integer.parseInt(manageCntEdit.getText().toString());
                String dataEd = manageWriteEdit.getText().toString();

                if (TextUtils.isEmpty(dataEd)) {
                    toast(getString(R.string.write_epc_first));
                    return;
                }
                String data = dataEd.replaceAll(" ", "");

                if (data.length() % 4 != 0) {//TODO data If the length is not a multiple of 4, 0 will be added automatically.
                    int less = data.length() % 4;
                    for (int i = 0; i < 4 - less; i++) {
                        data = data + "0";
                    }
                }
                int mem = manageBankSpinner.getSelectedItemPosition();
                Log.e(TAG, "onClick: epc:" + s_epc + " mem:" + mem + " address:" + add + " data:" + data  );
                int i = BaseApplication.getRFIDManager().writeTag(s_epc, pwd,   mem,  add,  data);//
//                int i = BaseApplication.getRFIDManager().writeTagEpc(s_epc, pwd,  data);//The entire contents of the replacement EPC
                if (i == 0) {
                    toast("Data was written successfully");
                    SoundTool.getInstance(BaseApplication.getContext()).playBeep(1);
                } else {
                    toast("Data write failed " + i);
                }


            }
        });

    }
    private void readTag(final String epc, final String tid){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                {
                    if (!map.containsKey(epc)) {
                        TagScan tagManage = new TagScan(epc,  tid, "", 1);
                        map.put(epc, tagManage);
                    } else {
                        TagScan tagManage = map.get(epc);
                        tagManage.setTid(tid);
                        map.put(epc, tagManage);
                    }
                    data = new ArrayList<>(map.values());
                    Log.d(TAG, "onOperationTag: data = " + Arrays.toString(data.toArray()));
                    adapterManageList.setData(data);
                    SoundTool.getInstance(BaseApplication.getContext()).playBeep(1);

                }
            }
        });
    }

    private void write(final String epc, final String tid){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                {
                    if (!map.containsKey(epc)) {
                        TagScan tagManage = new TagScan(epc,  tid, "", 1);
                        map.put(epc, tagManage);
                    } else {
                        TagScan tagManage = map.get(epc);
                        tagManage.setTid(tid);
                        map.put(epc, tagManage);
                    }
                    data = new ArrayList<>(map.values());
                    Log.d(TAG, "onOperationTag: data = " + Arrays.toString(data.toArray()));
                    adapterManageList.setData(data);

                }
            }
        });
    }





    private void toast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toast(message);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();

        if(mActivity.tagScanSpinner.size()!=0){
            manageEpcDatasSpinner.setSelection(0);
        }
    }

    @Override
    public void onPause() {

        super.onPause();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            manageBankSpinner.setSelection(1);
            if(mActivity.tagScanSpinner.size()>0){
                manageEpcDatasSpinner.setSelection(0);
                tvChoiceEpcTid.setText(mActivity.tagScanSpinner.get(0).getEpc());
                manageWriteEdit.setText(mActivity.tagScanSpinner.get(0).getEpc()+"");
            }
            epcArrayAdapter.notifyDataSetChanged();
        }else{
        }
    }

    /**
     * Modify PC values Write data to the PC
     */
    private void writeTagForPC(){


        String epc = "3424B35C9DD73A4694223874";//The EPC of the label needs to be read first；（EPC of the label (needs to be read first)）

        String datas = "3424B35C9DD73A469422387498770000";//Content to be written （To be written）

        //actual written content （Actual write）
        String pcValue = ByteUtils.getPC(datas);//Calculate PC value （Calculate PC value）
        String writeDatas = pcValue+datas;//Pin the data to be written （Splice the data written）
        String pwd =  "00000000" ;//password
        String writeData = managePasswordEdit.getText().toString();
        BaseApplication.getRFIDManager().writeTag(epc,pwd, 1, 1,writeData);

    }

}
package com.ubx.uhf.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.ubx.uhf.R;
import com.ubx.uhf.activity.MainActivity;
import com.ubx.uhf.base.BaseApplication;
import com.ubx.uhf.utils.SoundTool;
import com.ubx.uhf.utils.ToastUtil;
import com.ubx.usdk.rfid.aidl.IRfidCallback;
import com.ubx.usdk.rfid.aidl.RfidDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = SettingFragment.class.getSimpleName();


    public Button btnSetPower, btnCfMask, btnGetMask;
    private EditText etSetPower, edtMaskStartAddr, edtMaskLen, edtMaskData;
    private Spinner maskAreaSpinner, spinnerMaskData;
    private Toast toast;
    private List<String> maskData = new ArrayList<>();
    private SpinnerAdapter spinnerAdapter;
    private RadioButton rbEU, rbEU3, rbCN, rbURk;
    private Spinner spinnerStart, spinnerEnd, spinnerEndEpc;
    private List<String> starts = new ArrayList<>(), ends = new ArrayList<>();
    private List<String> EUS, EU3S, CNS, URKS;
    private ArrayAdapter startAdapter, endAdapter;
    private byte btyregion;
    private Button btnSetF, btnGetF;

//    private String locationEpcSelect;

    public static SettingFragment newInstance(MainActivity activity) {
        mActivity = activity;
        return new SettingFragment();
    }

    private static MainActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSetPower = view.findViewById(R.id.btn_setPower);
        etSetPower = view.findViewById(R.id.et_setPower);

        maskAreaSpinner = view.findViewById(R.id.spinner_mask_area);
        edtMaskStartAddr = view.findViewById(R.id.edt_maskStartAddr);
        edtMaskLen = view.findViewById(R.id.edt_maskLength);
        edtMaskData = view.findViewById(R.id.edt_maskData);
        btnCfMask = view.findViewById(R.id.btn_configMask);
        btnGetMask = view.findViewById(R.id.btn_getMask);

        spinnerMaskData = view.findViewById(R.id.spinner_mask_data);
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, maskData);
        spinnerMaskData.setAdapter(spinnerAdapter);


        rbCN = view.findViewById(R.id.Chinese);
        rbEU = view.findViewById(R.id.EU);
        rbEU3 = view.findViewById(R.id.EU3);
        rbURk = view.findViewById(R.id.Ukraine);
        rbEU.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btyregion = 0x04;
                    starts.clear();
                    ends.clear();
                    starts.addAll(EUS);
                    ends.addAll(EUS);
                    startAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, starts);
                    endAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, ends);
                    spinnerStart.setAdapter(startAdapter);
                    spinnerEnd.setAdapter(endAdapter);
                    spinnerEnd.setSelection(ends.size() - 1);
                }
            }
        });
        rbEU3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btyregion = 0x09;
                    starts.clear();
                    ends.clear();
                    starts.addAll(EU3S);
                    ends.addAll(EU3S);
                    startAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, starts);
                    endAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, ends);
                    spinnerStart.setAdapter(startAdapter);
                    spinnerEnd.setAdapter(endAdapter);
                    spinnerEnd.setSelection(ends.size() - 1);
                }
            }
        });
        rbURk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btyregion = 0x06;
                    starts.clear();
                    ends.clear();
                    starts.addAll(URKS);
                    ends.addAll(URKS);
                    startAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, starts);
                    endAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, ends);
                    spinnerStart.setAdapter(startAdapter);
                    spinnerEnd.setAdapter(endAdapter);
                    spinnerEnd.setSelection(ends.size() - 1);
                }
            }
        });
        rbCN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btyregion = 0x08;
                    starts.clear();
                    ends.clear();
                    starts.addAll(CNS);
                    ends.addAll(CNS);
                    startAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, starts);
                    endAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, ends);
                    spinnerStart.setAdapter(startAdapter);
                    spinnerEnd.setAdapter(endAdapter);
                    spinnerEnd.setSelection(ends.size() - 1);
                }
            }
        });


        spinnerStart = view.findViewById(R.id.spinner_start);
        spinnerEnd = view.findViewById(R.id.spinner_end);
        EUS = Arrays.asList(getContext().getResources().getStringArray(R.array.spinner_etsi_royal));
        EU3S = Arrays.asList(getContext().getResources().getStringArray(R.array.spinner_etsi3_royal));
        CNS = Arrays.asList(getContext().getResources().getStringArray(R.array.spinner_cn_royal));
        URKS = Arrays.asList(getContext().getResources().getStringArray(R.array.spinner_ukraine));
//        rbEU.setChecked(true);
        btyregion = 0x04;
        starts.addAll(EUS);
        ends.addAll(EUS);
        startAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, starts);
        endAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, ends);
        spinnerStart.setAdapter(startAdapter);
        spinnerEnd.setAdapter(endAdapter);
        btnSetF = view.findViewById(R.id.setFreq);
        btnGetF = view.findViewById(R.id.getF);


        initEvents();
    }

    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index > hexString.length() - 1) {
                return byteArray;
            }
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }

    private void initEvents() {
        btnSetPower.setOnClickListener(this);
        btnCfMask.setOnClickListener(this);
        btnGetMask.setOnClickListener(this);
        btnSetF.setOnClickListener(this);
        btnGetF.setOnClickListener(this);


    }

    private void getStandFrequency() {
        Log.v(TAG, "--- getStandFrequency()   ----");
        RfidDate frequencyRegion = BaseApplication.getRFIDManager().getFrequencyRegion();
        if (frequencyRegion == null) {
            Toast.makeText(getContext(), getString(R.string.get_fail), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.v(TAG, "--- getStandFrequency()  2 ----   "+ frequencyRegion.btRegion);
        if (frequencyRegion.btRegion != 15) {
            switch (frequencyRegion.btRegion) {
                case 4:
                    rbEU.setChecked(true);
                    starts.clear();
                    ends.clear();
                    starts.addAll(EUS);
                    ends.addAll(EUS);
                    break;
                case 6:
                    rbURk.setChecked(true);
                    starts.clear();
                    ends.clear();
                    starts.addAll(URKS);
                    ends.addAll(URKS);
                    break;
                case 8:
                    rbCN.setChecked(true);
                    starts.clear();
                    ends.clear();
                    starts.addAll(CNS);
                    ends.addAll(CNS);
                    break;
                case 9:
                    rbEU3.setChecked(true);
                    starts.clear();
                    ends.clear();
                    starts.addAll(EU3S);
                    ends.addAll(EU3S);
                    break;
                default:
                    break;
            }
            try {
                startAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, starts);
                endAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, ends);
                spinnerStart.setAdapter(startAdapter);
                spinnerEnd.setAdapter(endAdapter);
                spinnerStart.setSelection(frequencyRegion.btFrequencyStart);
                spinnerEnd.setSelection(frequencyRegion.btFrequencyEnd);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(getContext(), getString(R.string.get_fail), Toast.LENGTH_SHORT).show();
        }
    }

    private void setStandFrequency() {
        if (starts.size() > 0 && ends.size() > 0) {
            int startp = spinnerStart.getSelectedItemPosition();
            int endp = spinnerEnd.getSelectedItemPosition();
            if (startp >= endp) {
                Toast.makeText(getContext(), getString(R.string.set_fail), Toast.LENGTH_SHORT).show();
                return;
            }
            if (BaseApplication.getRFIDManager() != null) {
                int i = BaseApplication.getRFIDManager().setFrequencyRegion(btyregion, (byte) startp, (byte) endp);
                if (i == 0) {
                    Toast.makeText(getContext(), getContext().getString(R.string.set_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.set_fail), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void btnSetPower() {
        String str = etSetPower.getText().toString().trim();
        if (TextUtils.isEmpty(str)) {
            toast(getString(R.string.content_not_null));
            return;
        }
        int power = Integer.parseInt(str);
        if (power < 0 || power > 33) {
            toast(getString(R.string.power_value_not_allow));
            return;
        }
        int ret = BaseApplication.getRFIDManager().setOutputPower((byte) power);
        if (ret != 0) {
            toast(getString(R.string.set_power_fail));
        } else {
            SharedPreferences rfid_demo = getContext().getSharedPreferences("rfid_demo", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = rfid_demo.edit();
            edit.putInt("power", power);
            edit.apply();
        }
    }


    private void btnCfMask() {
        int area = maskAreaSpinner.getSelectedItemPosition() + 1;
        String startAddr = edtMaskStartAddr.getText().toString().trim();
        String len = edtMaskLen.getText().toString().trim();
        String data = edtMaskData.getText().toString().trim();
        if (startAddr.equals("") || data.equals("")) {
            return;
        }
        int i = BaseApplication.getRFIDManager().addMask(area, Integer.parseInt(startAddr), data.length(), data);
        if (i == 0) {
            maskData.add(area + "," + startAddr + "," + data);
            toast(getContext().getString(R.string.set_success));
        } else {
            toast(getContext().getString(R.string.set_fail));
        }

    }


    private void btnGetMask() {
        int i = BaseApplication.getRFIDManager().clearMask();
        if (i == 0) {
            edtMaskData.setText("");
            edtMaskStartAddr.setText("");
            edtMaskLen.setText("");
            maskData.clear();
            toast("clear successfully");
        } else {
            toast("clear failed");
        }

    }





    @Override
    public void onClick(View view) {

        if (BaseApplication.getRFIDManager() == null) {
            toast(getString(R.string.rfid_not_init));
            return;
        }

        if (view == btnSetPower) {
            btnSetPower();
        } else if (view == btnCfMask) {
            btnCfMask();
        } else if (view == btnGetMask) {
            btnGetMask();
        } else if (view == btnSetF) {
            setStandFrequency();
        } else if (view == btnGetF) {
            getStandFrequency();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            setPowerInfo();
        }
    }

    private void setPowerInfo() {
        if (BaseApplication.getRFIDManager() != null) {
            int outputPower = BaseApplication.getRFIDManager().getOutputPower();
            if (outputPower >= 0) {
                etSetPower.setText("" + outputPower);
            }
        }
    }

    private void toast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toast(message);
            }
        });
    }



}
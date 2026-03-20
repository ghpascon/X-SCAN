package com.example.at907_plugin;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.atid.lib.dev.ATRfidManager;
import com.atid.lib.dev.ATRfidReader;
import com.atid.lib.dev.event.RfidReaderEventListener;
import com.atid.lib.dev.rfid.exception.ATRfidReaderException;
import com.atid.lib.dev.rfid.type.ConnectionState;
import com.atid.lib.dev.rfid.type.ActionState;
import com.atid.lib.dev.rfid.type.ResultCode;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;

public class At907Plugin implements FlutterPlugin, MethodChannel.MethodCallHandler {
    private MethodChannel channel;
    private ATRfidReader mReader;
    private Context context;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    // Gatilho: listener
    private final RfidReaderEventListener triggerListener = new RfidReaderEventListener() {
        @Override
        public void onReaderStateChanged(ATRfidReader reader, ConnectionState state) {}

        @Override
        public void onReaderActionChanged(ATRfidReader reader, ActionState action) {
            android.util.Log.d("AT907Plugin", "onReaderActionChanged: " + action);
            if (action == ActionState.CarrierWaveOn) {
                android.util.Log.d("AT907Plugin", "Trigger: CarrierWaveOn - iniciando inventário");
                mainHandler.post(() -> {
                    if (mReader != null) mReader.inventory6cTag();
                });
            } else if (action == ActionState.Stop) {
                android.util.Log.d("AT907Plugin", "Trigger: Stop - parando inventário");
                mainHandler.post(() -> {
                    if (mReader != null) mReader.stop();
                });
            }
        }

        @Override
        public void onReaderReadTag(ATRfidReader reader, String tag, float rssi, float phase) {}

        @Override
        public void onReaderResult(ATRfidReader reader, ResultCode code, ActionState action, String epc, String data, float rssi, float phase) {}
    };

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), "at907_plugin");
        channel.setMethodCallHandler(this);
        context = binding.getApplicationContext();
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        switch (call.method) {
            case "connect":
                connect(result);
                break;
            case "getFirmwareVersion":
                getFirmwareVersion(result);
                break;
            case "startInventory":
                startInventory(result);
                break;
            case "stopInventory":
                stopInventory(result);
                break;
            case "getPower":
                getPower(result);
                break;
            case "setPower":
                setPower(call, result);
                break;
            default:
                result.notImplemented();
        }
    }
    private void getPower(Result result) {
        try {
            if (mReader == null) {
                result.error("NO_READER", "Reader not initialized", null);
                return;
            }
            int power = mReader.getPower();
            result.success(power);
        } catch (ATRfidReaderException e) {
            result.error("GET_POWER_FAIL", e.getMessage(), null);
        }
    }

    private void setPower(MethodCall call, Result result) {
        try {
            if (mReader == null) {
                result.error("NO_READER", "Reader not initialized", null);
                return;
            }
            int power = call.argument("power");
            mReader.setPower(power); // método retorna void
            result.success(true);
        } catch (ATRfidReaderException e) {
            result.error("SET_POWER_FAIL", e.getMessage(), null);
        }
    }

    private void connect(Result result) {
        try {
            mReader = ATRfidManager.getInstance();
            if (mReader == null) {
                result.error("NO_MODULE", "AT907 module not found or busy", null);
                return;
            }
            mReader.connect();
            // Adiciona listener de gatilho
            mReader.setEventListener(triggerListener);
            result.success(true);
        } catch (Exception e) {
            result.error("CONNECT_FAIL", e.getMessage(), null);
        }
    }

    private void getFirmwareVersion(Result result) {
        try {
            if (mReader == null) {
                result.error("NO_READER", "Reader not initialized", null);
                return;
            }
            String version = mReader.getFirmwareVersion();
            result.success(version);
        } catch (ATRfidReaderException e) {
            result.error("FW_FAIL", e.getMessage(), null);
        }
    }

    private void startInventory(Result result) {
        if (mReader == null) {
            result.error("NO_READER", "Reader not initialized", null);
            return;
        }
        // Inventário padrão: Tag6C
        ResultCode res = mReader.inventory6cTag();
        if (res == ResultCode.NoError) {
            result.success(true);
        } else {
            result.error("INV_FAIL", "inventory6cTag falhou: " + res, null);
        }
    }

    private void stopInventory(Result result) {
        if (mReader == null) {
            result.error("NO_READER", "Reader not initialized", null);
            return;
        }
        ResultCode res = mReader.stop();
        if (res == ResultCode.NoError) {
            result.success(true);
        } else {
            result.error("STOP_FAIL", "stop falhou: " + res, null);
        }
    }
}

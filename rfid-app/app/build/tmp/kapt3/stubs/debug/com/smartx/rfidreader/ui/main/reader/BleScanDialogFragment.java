package com.smartx.rfidreader.ui.main.reader;

/**
 * Diálogo que escaneia dispositivos BLE e permite ao usuário selecionar um.
 * Retorna o endereço MAC via [onDeviceSelected].
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J$\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\'2\b\u0010(\u001a\u0004\u0018\u00010)2\b\u0010*\u001a\u0004\u0018\u00010+H\u0016J\b\u0010,\u001a\u00020\u0017H\u0016J\b\u0010-\u001a\u00020\u0017H\u0016J\u001a\u0010.\u001a\u00020\u00172\u0006\u0010/\u001a\u00020%2\b\u0010*\u001a\u0004\u0018\u00010+H\u0016J\b\u00100\u001a\u00020\u0017H\u0002J\b\u00101\u001a\u00020\u0017H\u0003J\b\u00102\u001a\u00020\u0017H\u0003R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000fR\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000RL\u0010\u0012\u001a4\u0012\u0013\u0012\u00110\u000e\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u000e\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u0017\u0018\u00010\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001a\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00063"}, d2 = {"Lcom/smartx/rfidreader/ui/main/reader/BleScanDialogFragment;", "Landroidx/fragment/app/DialogFragment;", "()V", "SCAN_TIMEOUT_MS", "", "_binding", "Lcom/smartx/rfidreader/databinding/DialogBleScanBinding;", "binding", "getBinding", "()Lcom/smartx/rfidreader/databinding/DialogBleScanBinding;", "bleAdapter", "Lcom/smartx/rfidreader/ui/main/reader/BleDeviceAdapter;", "blePermissions", "", "", "[Ljava/lang/String;", "bluetoothAdapter", "Landroid/bluetooth/BluetoothAdapter;", "onDeviceSelected", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "address", "", "getOnDeviceSelected", "()Lkotlin/jvm/functions/Function2;", "setOnDeviceSelected", "(Lkotlin/jvm/functions/Function2;)V", "permissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "scanCallback", "Landroid/bluetooth/le/ScanCallback;", "scanHandler", "Landroid/os/Handler;", "scanning", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onStart", "onViewCreated", "view", "requestPermissionsAndScan", "startScan", "stopScan", "app_debug"})
public final class BleScanDialogFragment extends androidx.fragment.app.DialogFragment {
    @org.jetbrains.annotations.Nullable()
    private com.smartx.rfidreader.databinding.DialogBleScanBinding _binding;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onDeviceSelected;
    private com.smartx.rfidreader.ui.main.reader.BleDeviceAdapter bleAdapter;
    @org.jetbrains.annotations.Nullable()
    private android.bluetooth.BluetoothAdapter bluetoothAdapter;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler scanHandler = null;
    private boolean scanning = false;
    private final long SCAN_TIMEOUT_MS = 15000L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String[] blePermissions = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String[]> permissionLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final android.bluetooth.le.ScanCallback scanCallback = null;
    
    public BleScanDialogFragment() {
        super();
    }
    
    private final com.smartx.rfidreader.databinding.DialogBleScanBinding getBinding() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function2<java.lang.String, java.lang.String, kotlin.Unit> getOnDeviceSelected() {
        return null;
    }
    
    public final void setOnDeviceSelected(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> p0) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onStart() {
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void requestPermissionsAndScan() {
    }
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    private final void startScan() {
    }
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    private final void stopScan() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}
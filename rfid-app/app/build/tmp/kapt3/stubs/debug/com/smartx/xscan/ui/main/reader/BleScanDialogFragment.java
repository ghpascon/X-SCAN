package com.smartx.xscan.ui.main.reader;

/**
 * Diálogo que escaneia dispositivos BLE (via [BleScanner] da biblioteca) e permite ao usuário
 * selecionar um. Retorna o endereço MAC via [onDeviceSelected].
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J$\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010!2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010$\u001a\u00020\u0010H\u0016J\b\u0010%\u001a\u00020\u0010H\u0016J\u001a\u0010&\u001a\u00020\u00102\u0006\u0010\'\u001a\u00020\u001d2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010(\u001a\u00020\u0010H\u0002J\b\u0010)\u001a\u00020\u0010H\u0002J\b\u0010*\u001a\u00020\u0010H\u0002J\b\u0010+\u001a\u00020\u0010H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000RL\u0010\n\u001a4\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b\r\u0012\b\b\u000e\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00170\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lcom/smartx/xscan/ui/main/reader/BleScanDialogFragment;", "Landroidx/fragment/app/DialogFragment;", "()V", "_binding", "Lcom/smartx/xscan/databinding/DialogBleScanBinding;", "binding", "getBinding", "()Lcom/smartx/xscan/databinding/DialogBleScanBinding;", "bleAdapter", "Lcom/smartx/xscan/ui/main/reader/BleDeviceAdapter;", "onDeviceSelected", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "address", "", "getOnDeviceSelected", "()Lkotlin/jvm/functions/Function2;", "setOnDeviceSelected", "(Lkotlin/jvm/functions/Function2;)V", "permissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "scanJob", "Lkotlinx/coroutines/Job;", "scanner", "Lcom/smartx/rfid/ble/BleScanner;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onStart", "onViewCreated", "view", "requestPermissionsAndScan", "showScanFinished", "startScan", "stopScan", "app_debug"})
public final class BleScanDialogFragment extends androidx.fragment.app.DialogFragment {
    @org.jetbrains.annotations.Nullable()
    private com.smartx.xscan.databinding.DialogBleScanBinding _binding;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onDeviceSelected;
    private com.smartx.xscan.ui.main.reader.BleDeviceAdapter bleAdapter;
    private com.smartx.rfid.ble.BleScanner scanner;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job scanJob;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String[]> permissionLauncher = null;
    
    public BleScanDialogFragment() {
        super();
    }
    
    private final com.smartx.xscan.databinding.DialogBleScanBinding getBinding() {
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
    
    private final void startScan() {
    }
    
    private final void stopScan() {
    }
    
    private final void showScanFinished() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}
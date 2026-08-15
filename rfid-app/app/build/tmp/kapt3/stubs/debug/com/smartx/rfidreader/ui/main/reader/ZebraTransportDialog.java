package com.smartx.rfidreader.ui.main.reader;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J*\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\n0\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u000eJ,\u0010\u0010\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\n0\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u000eH\u0002J,\u0010\u0011\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\n0\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u000eH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/smartx/rfidreader/ui/main/reader/ZebraTransportDialog;", "", "()V", "TAG", "", "hasUsableButtons", "", "binding", "Lcom/smartx/rfidreader/databinding/DialogZebraTransportBinding;", "show", "", "context", "Landroid/content/Context;", "onBluetoothSelected", "Lkotlin/Function0;", "onSerialSelected", "showCustomDialog", "showFallbackListDialog", "app_debug"})
public final class ZebraTransportDialog {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "ZebraTransportDialog";
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.rfidreader.ui.main.reader.ZebraTransportDialog INSTANCE = null;
    
    private ZebraTransportDialog() {
        super();
    }
    
    public final void show(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBluetoothSelected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSerialSelected) {
    }
    
    private final void showCustomDialog(android.content.Context context, kotlin.jvm.functions.Function0<kotlin.Unit> onBluetoothSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onSerialSelected) {
    }
    
    private final boolean hasUsableButtons(com.smartx.rfidreader.databinding.DialogZebraTransportBinding binding) {
        return false;
    }
    
    private final void showFallbackListDialog(android.content.Context context, kotlin.jvm.functions.Function0<kotlin.Unit> onBluetoothSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onSerialSelected) {
    }
}
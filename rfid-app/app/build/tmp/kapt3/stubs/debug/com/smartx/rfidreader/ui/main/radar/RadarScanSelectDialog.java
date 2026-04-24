package com.smartx.rfidreader.ui.main.radar;

/**
 * BottomSheet expandida que mostra as tags atualmente visíveis e permite
 * selecionar quais adicionar como targets do radar.
 * O gatilho físico também inicia/para o inventário enquanto o dialog está aberto.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 #2\u00020\u0001:\u0001#B-\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0018\u0010\u0005\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u0007\u0012\u0004\u0012\u00020\b0\u0006\u00a2\u0006\u0002\u0010\tJ\b\u0010\u0017\u001a\u00020\bH\u0002J$\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010 \u001a\u00020\bH\u0016J\u001a\u0010!\u001a\u00020\b2\u0006\u0010\"\u001a\u00020\u00192\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\u000b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0012R \u0010\u0005\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/smartx/rfidreader/ui/main/radar/RadarScanSelectDialog;", "Lcom/google/android/material/bottomsheet/BottomSheetDialogFragment;", "existingEpcs", "", "", "onTagsSelected", "Lkotlin/Function1;", "", "", "(Ljava/util/Set;Lkotlin/jvm/functions/Function1;)V", "_binding", "Lcom/smartx/rfidreader/databinding/DialogRadarScanBinding;", "binding", "getBinding", "()Lcom/smartx/rfidreader/databinding/DialogRadarScanBinding;", "mainViewModel", "Lcom/smartx/rfidreader/ui/main/MainViewModel;", "getMainViewModel", "()Lcom/smartx/rfidreader/ui/main/MainViewModel;", "mainViewModel$delegate", "Lkotlin/Lazy;", "scanAdapter", "Lcom/smartx/rfidreader/ui/main/radar/RadarScanAdapter;", "observeState", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "Companion", "app_debug"})
public final class RadarScanSelectDialog extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> existingEpcs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<java.util.List<java.lang.String>, kotlin.Unit> onTagsSelected = null;
    @org.jetbrains.annotations.NotNull()
    private static final int[] TRIGGER_KEYCODES = {131, 80, 293, 79, 103};
    @org.jetbrains.annotations.Nullable()
    private com.smartx.rfidreader.databinding.DialogRadarScanBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy mainViewModel$delegate = null;
    private com.smartx.rfidreader.ui.main.radar.RadarScanAdapter scanAdapter;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.rfidreader.ui.main.radar.RadarScanSelectDialog.Companion Companion = null;
    
    public RadarScanSelectDialog(@org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> existingEpcs, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<java.lang.String>, kotlin.Unit> onTagsSelected) {
        super();
    }
    
    private final com.smartx.rfidreader.databinding.DialogRadarScanBinding getBinding() {
        return null;
    }
    
    private final com.smartx.rfidreader.ui.main.MainViewModel getMainViewModel() {
        return null;
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
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void observeState() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/smartx/rfidreader/ui/main/radar/RadarScanSelectDialog$Companion;", "", "()V", "TRIGGER_KEYCODES", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
package com.smartx.rfidreader.ui.main.radar;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0006\n\u0002\b\b\u0018\u0000 72\u00020\u0001:\u00017B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u001eH\u0002J\b\u0010 \u001a\u00020\u001eH\u0002J$\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0016J\b\u0010)\u001a\u00020\u001eH\u0016J\b\u0010*\u001a\u00020\u001eH\u0016J\b\u0010+\u001a\u00020\u001eH\u0016J\u001a\u0010,\u001a\u00020\u001e2\u0006\u0010-\u001a\u00020\"2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0016J\u0010\u0010.\u001a\u00020\u001a2\u0006\u0010/\u001a\u000200H\u0002J\b\u00101\u001a\u00020\u001eH\u0002J\b\u00102\u001a\u00020\u001eH\u0002J\b\u00103\u001a\u00020\u001eH\u0002J\b\u00104\u001a\u00020\u001eH\u0002J\b\u00105\u001a\u00020\u001eH\u0002J\b\u00106\u001a\u00020\u001eH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u001b\u0010\f\u001a\u00020\r8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u000e\u0010\u000fR\u001b\u0010\u0012\u001a\u00020\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\u0011\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00068"}, d2 = {"Lcom/smartx/rfidreader/ui/main/radar/RadarFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/smartx/rfidreader/databinding/FragmentRadarBinding;", "audioJob", "Lkotlinx/coroutines/Job;", "audioTrack", "Landroid/media/AudioTrack;", "binding", "getBinding", "()Lcom/smartx/rfidreader/databinding/FragmentRadarBinding;", "mainViewModel", "Lcom/smartx/rfidreader/ui/main/MainViewModel;", "getMainViewModel", "()Lcom/smartx/rfidreader/ui/main/MainViewModel;", "mainViewModel$delegate", "Lkotlin/Lazy;", "radarViewModel", "Lcom/smartx/rfidreader/ui/main/radar/RadarViewModel;", "getRadarViewModel", "()Lcom/smartx/rfidreader/ui/main/radar/RadarViewModel;", "radarViewModel$delegate", "targetAdapter", "Lcom/smartx/rfidreader/ui/main/radar/RadarTargetAdapter;", "targetFreq", "", "wasInventorying", "", "addManualTarget", "", "hideKeyboard", "observeState", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onStart", "onStop", "onViewCreated", "view", "rssiToFrequency", "rssi", "", "setupAddTarget", "setupButtons", "setupPowerSlider", "setupRecyclerView", "startAudio", "stopAudio", "Companion", "app_debug"})
public final class RadarFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.smartx.rfidreader.databinding.FragmentRadarBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy mainViewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy radarViewModel$delegate = null;
    private com.smartx.rfidreader.ui.main.radar.RadarTargetAdapter targetAdapter;
    @org.jetbrains.annotations.Nullable()
    private android.media.AudioTrack audioTrack;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job audioJob;
    
    /**
     * Frequência alvo em Hz; 0 = silenciar. @Volatile para acesso de thread IO.
     */
    @kotlin.jvm.Volatile()
    private volatile float targetFreq = 0.0F;
    private boolean wasInventorying = false;
    private static final int SAMPLE_RATE = 8000;
    private static final int CHUNK = 400;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.rfidreader.ui.main.radar.RadarFragment.Companion Companion = null;
    
    public RadarFragment() {
        super();
    }
    
    private final com.smartx.rfidreader.databinding.FragmentRadarBinding getBinding() {
        return null;
    }
    
    private final com.smartx.rfidreader.ui.main.MainViewModel getMainViewModel() {
        return null;
    }
    
    private final com.smartx.rfidreader.ui.main.radar.RadarViewModel getRadarViewModel() {
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
    
    @java.lang.Override()
    public void onStart() {
    }
    
    @java.lang.Override()
    public void onStop() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupPowerSlider() {
    }
    
    private final void setupAddTarget() {
    }
    
    private final void setupButtons() {
    }
    
    private final void addManualTarget() {
    }
    
    private final void hideKeyboard() {
    }
    
    private final void observeState() {
    }
    
    /**
     * Mapeia RSSI para frequência:
     * -90 dBm (longe) → 300 Hz  |  -30 dBm (perto) → 1200 Hz
     */
    private final float rssiToFrequency(double rssi) {
        return 0.0F;
    }
    
    /**
     * Cria o AudioTrack e inicia o loop de escrita de onda senoidal em background.
     * O loop lê [targetFreq] a cada chunk (50 ms); freq=0 gera silêncio.
     */
    private final void startAudio() {
    }
    
    private final void stopAudio() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/smartx/rfidreader/ui/main/radar/RadarFragment$Companion;", "", "()V", "CHUNK", "", "SAMPLE_RATE", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
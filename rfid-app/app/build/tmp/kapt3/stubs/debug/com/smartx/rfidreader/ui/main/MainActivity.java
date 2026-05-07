package com.smartx.rfidreader.ui.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000  2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001 B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u0012H\u0014J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\u001a\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00172\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0019J\b\u0010\u001a\u001a\u00020\u0014H\u0002J\b\u0010\u001b\u001a\u00020\u0014H\u0002J\u0012\u0010\u001c\u001a\u00020\u00142\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0014J\b\u0010\u001f\u001a\u00020\u0014H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\t\u00a8\u0006!"}, d2 = {"Lcom/smartx/rfidreader/ui/main/MainActivity;", "Lcom/smartx/rfidreader/ui/base/BaseActivity;", "Lcom/smartx/rfidreader/databinding/ActivityMainBinding;", "()V", "backPressedTime", "", "viewModel", "Lcom/smartx/rfidreader/ui/main/MainViewModel;", "getViewModel", "()Lcom/smartx/rfidreader/ui/main/MainViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "dispatchKeyEvent", "", "event", "Landroid/view/KeyEvent;", "inflateBinding", "inflater", "Landroid/view/LayoutInflater;", "loadHome", "", "navigateTo", "fragment", "Landroidx/fragment/app/Fragment;", "tag", "", "observeHeader", "observeNavigation", "onActivityReady", "savedInstanceState", "Landroid/os/Bundle;", "setupBackHandler", "Companion", "app_debug"})
public final class MainActivity extends com.smartx.rfidreader.ui.base.BaseActivity<com.smartx.rfidreader.databinding.ActivityMainBinding> {
    @org.jetbrains.annotations.NotNull()
    private static final int[] TRIGGER_KEYCODES = {131, 80, 293, 79, 103, 523};
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TAG_HOME = "home";
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private long backPressedTime = 0L;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.rfidreader.ui.main.MainActivity.Companion Companion = null;
    
    public MainActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartx.rfidreader.ui.main.MainViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    protected com.smartx.rfidreader.databinding.ActivityMainBinding inflateBinding(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater) {
        return null;
    }
    
    @java.lang.Override()
    protected void onActivityReady(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void observeHeader() {
    }
    
    private final void loadHome() {
    }
    
    /**
     * Navega para um fragmento, adicionando ao back stack
     */
    public final void navigateTo(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.Fragment fragment, @org.jetbrains.annotations.Nullable()
    java.lang.String tag) {
    }
    
    private final void setupBackHandler() {
    }
    
    private final void observeNavigation() {
    }
    
    @java.lang.Override()
    public boolean dispatchKeyEvent(@org.jetbrains.annotations.NotNull()
    android.view.KeyEvent event) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/smartx/rfidreader/ui/main/MainActivity$Companion;", "", "()V", "TAG_HOME", "", "TRIGGER_KEYCODES", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
package com.smartx.rfidreader.ui.main.config;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\n\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0002J\u0010\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0014H\u0002J$\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\b\u0010#\u001a\u00020\u0014H\u0016J\u001a\u0010$\u001a\u00020\u00142\u0006\u0010%\u001a\u00020\u001c2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\b\u0010&\u001a\u00020\u0014H\u0002J\b\u0010\'\u001a\u00020\u0014H\u0002J\b\u0010(\u001a\u00020\u0014H\u0002J\b\u0010)\u001a\u00020\u0014H\u0002J\u0010\u0010*\u001a\u00020\u00142\u0006\u0010+\u001a\u00020\nH\u0002J\u0010\u0010,\u001a\u00020\u00142\u0006\u0010-\u001a\u00020.H\u0002J\u0010\u0010/\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u0019H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000f\u0010\u0010\u00a8\u00060"}, d2 = {"Lcom/smartx/rfidreader/ui/main/config/ConfigFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/smartx/rfidreader/databinding/FragmentConfigBinding;", "binding", "getBinding", "()Lcom/smartx/rfidreader/databinding/FragmentConfigBinding;", "currentPrefixes", "", "", "isUpdatingFromState", "", "viewModel", "Lcom/smartx/rfidreader/ui/main/MainViewModel;", "getViewModel", "()Lcom/smartx/rfidreader/ui/main/MainViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "addPrefixFromInput", "", "buildReaderConfigFromInputs", "Lcom/smartx/rfidreader/core/reader/ReaderConfig;", "handleConfigSaveResult", "state", "Lcom/smartx/rfidreader/ui/main/MainUiState;", "observeState", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "renderPrefixChips", "saveAppSettings", "setupAppSettingsSection", "setupReaderConfigSection", "showSnackbar", "msg", "updateAppSettingsSection", "settings", "Lcom/smartx/rfidreader/core/settings/AppSettings;", "updateReaderSection", "app_debug"})
public final class ConfigFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.smartx.rfidreader.databinding.FragmentConfigBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> currentPrefixes = null;
    private boolean isUpdatingFromState = false;
    
    public ConfigFragment() {
        super();
    }
    
    private final com.smartx.rfidreader.databinding.FragmentConfigBinding getBinding() {
        return null;
    }
    
    private final com.smartx.rfidreader.ui.main.MainViewModel getViewModel() {
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
    
    private final void setupReaderConfigSection() {
    }
    
    private final com.smartx.rfidreader.core.reader.ReaderConfig buildReaderConfigFromInputs() {
        return null;
    }
    
    private final void setupAppSettingsSection() {
    }
    
    private final void addPrefixFromInput() {
    }
    
    private final void renderPrefixChips() {
    }
    
    private final void saveAppSettings() {
    }
    
    private final void observeState() {
    }
    
    private final void updateReaderSection(com.smartx.rfidreader.ui.main.MainUiState state) {
    }
    
    private final void updateAppSettingsSection(com.smartx.rfidreader.core.settings.AppSettings settings) {
    }
    
    private final void handleConfigSaveResult(com.smartx.rfidreader.ui.main.MainUiState state) {
    }
    
    private final void showSnackbar(java.lang.String msg) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}
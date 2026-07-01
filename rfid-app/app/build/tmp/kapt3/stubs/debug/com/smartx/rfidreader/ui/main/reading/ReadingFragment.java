package com.smartx.rfidreader.ui.main.reading;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u000bH\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001c\u001a\u00020\u0018H\u0002J$\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\"2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\b\u0010%\u001a\u00020\u0018H\u0016J\b\u0010&\u001a\u00020\u0018H\u0016J\b\u0010\'\u001a\u00020\u0018H\u0016J\u001a\u0010(\u001a\u00020\u00182\u0006\u0010)\u001a\u00020\u001e2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\b\u0010*\u001a\u00020\u0018H\u0002J\u0010\u0010+\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u000bH\u0002J\b\u0010,\u001a\u00020\u0018H\u0002J\b\u0010-\u001a\u00020\u0018H\u0002J\b\u0010.\u001a\u00020\u0018H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0011\u001a\u00020\u00128BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006/"}, d2 = {"Lcom/smartx/rfidreader/ui/main/reading/ReadingFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/smartx/rfidreader/databinding/FragmentReadingBinding;", "binding", "getBinding", "()Lcom/smartx/rfidreader/databinding/FragmentReadingBinding;", "locationPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "", "pendingInventoryName", "tagAdapter", "Lcom/smartx/rfidreader/ui/main/reading/TagListAdapter;", "toneGenerator", "Landroid/media/ToneGenerator;", "viewModel", "Lcom/smartx/rfidreader/ui/main/MainViewModel;", "getViewModel", "()Lcom/smartx/rfidreader/ui/main/MainViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "doSaveInventory", "", "inventoryName", "hasLocationPermission", "", "observeState", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onStart", "onStop", "onViewCreated", "view", "playBeep", "requestLocationAndSave", "setupButtons", "setupRecyclerView", "showInventoryNameDialog", "app_debug"})
public final class ReadingFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.smartx.rfidreader.databinding.FragmentReadingBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.smartx.rfidreader.ui.main.reading.TagListAdapter tagAdapter;
    
    /**
     * ToneGenerator reutilizado — criado em onStart, liberado em onStop
     */
    @org.jetbrains.annotations.Nullable()
    private android.media.ToneGenerator toneGenerator;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pendingInventoryName;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String[]> locationPermissionLauncher = null;
    
    public ReadingFragment() {
        super();
    }
    
    private final com.smartx.rfidreader.databinding.FragmentReadingBinding getBinding() {
        return null;
    }
    
    private final com.smartx.rfidreader.ui.main.MainViewModel getViewModel() {
        return null;
    }
    
    private final boolean hasLocationPermission() {
        return false;
    }
    
    private final void playBeep() {
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
    
    private final void setupRecyclerView() {
    }
    
    private final void setupButtons() {
    }
    
    private final void showInventoryNameDialog() {
    }
    
    private final void requestLocationAndSave(java.lang.String inventoryName) {
    }
    
    private final void doSaveInventory(java.lang.String inventoryName) {
    }
    
    private final void observeState() {
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
}
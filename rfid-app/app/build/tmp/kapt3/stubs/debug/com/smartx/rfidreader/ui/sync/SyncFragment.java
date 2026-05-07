package com.smartx.rfidreader.ui.sync;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0016\u001a\u00020\u0013H\u0002J$\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010\u001f\u001a\u00020\u0013H\u0016J\u001a\u0010 \u001a\u00020\u00132\u0006\u0010!\u001a\u00020\u00182\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010\"\u001a\u00020\u0013H\u0002J\b\u0010#\u001a\u00020\u0013H\u0002J\b\u0010$\u001a\u00020\u0013H\u0002J\b\u0010%\u001a\u00020\u0013H\u0002J\u0010\u0010&\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\f\u001a\u00020\r8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\'"}, d2 = {"Lcom/smartx/rfidreader/ui/sync/SyncFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/smartx/rfidreader/databinding/FragmentSyncBinding;", "binding", "getBinding", "()Lcom/smartx/rfidreader/databinding/FragmentSyncBinding;", "eventAdapter", "Lcom/smartx/rfidreader/ui/sync/EventListAdapter;", "progressAdapter", "Lcom/smartx/rfidreader/ui/sync/SyncProgressAdapter;", "viewModel", "Lcom/smartx/rfidreader/ui/sync/SyncViewModel;", "getViewModel", "()Lcom/smartx/rfidreader/ui/sync/SyncViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "confirmDelete", "", "event", "Lcom/smartx/rfidreader/core/db/EventEntity;", "observeState", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "setupEventsList", "setupProgressList", "setupSyncButton", "setupWebhookSection", "showJsonDialog", "app_debug"})
public final class SyncFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.smartx.rfidreader.databinding.FragmentSyncBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.smartx.rfidreader.ui.sync.EventListAdapter eventAdapter;
    private com.smartx.rfidreader.ui.sync.SyncProgressAdapter progressAdapter;
    
    public SyncFragment() {
        super();
    }
    
    private final com.smartx.rfidreader.databinding.FragmentSyncBinding getBinding() {
        return null;
    }
    
    private final com.smartx.rfidreader.ui.sync.SyncViewModel getViewModel() {
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
    public void onDestroyView() {
    }
    
    private final void setupEventsList() {
    }
    
    private final void setupProgressList() {
    }
    
    private final void setupWebhookSection() {
    }
    
    private final void setupSyncButton() {
    }
    
    private final void confirmDelete(com.smartx.rfidreader.core.db.EventEntity event) {
    }
    
    private final void showJsonDialog(com.smartx.rfidreader.core.db.EventEntity event) {
    }
    
    private final void observeState() {
    }
}
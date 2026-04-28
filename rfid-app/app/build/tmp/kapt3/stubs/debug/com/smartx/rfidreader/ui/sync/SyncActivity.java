package com.smartx.rfidreader.ui.sync;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0010H\u0002J\u0012\u0010\u0014\u001a\u00020\u00102\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\b\u0010\u0017\u001a\u00020\u0010H\u0002J\b\u0010\u0018\u001a\u00020\u0010H\u0002J\b\u0010\u0019\u001a\u00020\u0010H\u0002J\b\u0010\u001a\u001a\u00020\u0010H\u0002J\u0010\u0010\u001b\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u001c"}, d2 = {"Lcom/smartx/rfidreader/ui/sync/SyncActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/smartx/rfidreader/databinding/ActivitySyncBinding;", "eventAdapter", "Lcom/smartx/rfidreader/ui/sync/EventListAdapter;", "progressAdapter", "Lcom/smartx/rfidreader/ui/sync/SyncProgressAdapter;", "viewModel", "Lcom/smartx/rfidreader/ui/sync/SyncViewModel;", "getViewModel", "()Lcom/smartx/rfidreader/ui/sync/SyncViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "confirmDelete", "", "event", "Lcom/smartx/rfidreader/core/db/EventEntity;", "observeState", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "setupEventsList", "setupProgressList", "setupSyncButton", "setupWebhookSection", "showJsonDialog", "app_debug"})
public final class SyncActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.smartx.rfidreader.databinding.ActivitySyncBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.smartx.rfidreader.ui.sync.EventListAdapter eventAdapter;
    private com.smartx.rfidreader.ui.sync.SyncProgressAdapter progressAdapter;
    
    public SyncActivity() {
        super();
    }
    
    private final com.smartx.rfidreader.ui.sync.SyncViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
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
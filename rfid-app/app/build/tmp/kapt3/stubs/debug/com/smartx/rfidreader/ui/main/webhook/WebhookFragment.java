package com.smartx.rfidreader.ui.main.webhook;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 12\u00020\u0001:\u00011B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u001d\u001a\u00020\tH\u0002J\b\u0010\u001e\u001a\u00020\tH\u0002J\b\u0010\u001f\u001a\u00020 H\u0002J$\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0016J\b\u0010)\u001a\u00020 H\u0016J\b\u0010*\u001a\u00020 H\u0016J\u001a\u0010+\u001a\u00020 2\u0006\u0010,\u001a\u00020\"2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0016J\b\u0010-\u001a\u00020 H\u0002J\b\u0010.\u001a\u00020 H\u0002J\b\u0010/\u001a\u00020 H\u0002J\b\u00100\u001a\u00020 H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0015\u001a\u00020\u00168BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u0017\u0010\u0018\u00a8\u00062"}, d2 = {"Lcom/smartx/rfidreader/ui/main/webhook/WebhookFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/smartx/rfidreader/databinding/FragmentWebhookBinding;", "binding", "getBinding", "()Lcom/smartx/rfidreader/databinding/FragmentWebhookBinding;", "isInventorying", "", "isReaderConnected", "isSendingNow", "isWebhookRunning", "lastClearedWebhookAt", "", "lastToggleClickMs", "lastWebhookStatus", "Lcom/smartx/rfidreader/core/webhook/WebhookSendStatus;", "timeFormat", "Ljava/text/SimpleDateFormat;", "toggleInFlight", "viewModel", "Lcom/smartx/rfidreader/ui/main/MainViewModel;", "getViewModel", "()Lcom/smartx/rfidreader/ui/main/MainViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "buildValidatedSettings", "Lcom/smartx/rfidreader/core/settings/AppSettings;", "requireUrl", "canToggleWebhookNow", "lockToggleButton", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onResume", "onViewCreated", "view", "refreshMonitoringUi", "refreshReadingToggleUi", "refreshServiceUi", "syncRunningStateFromService", "Companion", "app_debug"})
public final class WebhookFragment extends androidx.fragment.app.Fragment {
    private static final long TOGGLE_DEBOUNCE_MS = 700L;
    private static final long TOGGLE_RECOVERY_MS = 1800L;
    @org.jetbrains.annotations.Nullable()
    private com.smartx.rfidreader.databinding.FragmentWebhookBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private long lastClearedWebhookAt = -1L;
    private boolean isWebhookRunning = false;
    private boolean isSendingNow = false;
    private boolean isReaderConnected = false;
    private boolean isInventorying = false;
    @org.jetbrains.annotations.Nullable()
    private com.smartx.rfidreader.core.webhook.WebhookSendStatus lastWebhookStatus;
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat timeFormat = null;
    private long lastToggleClickMs = 0L;
    private boolean toggleInFlight = false;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.rfidreader.ui.main.webhook.WebhookFragment.Companion Companion = null;
    
    public WebhookFragment() {
        super();
    }
    
    private final com.smartx.rfidreader.databinding.FragmentWebhookBinding getBinding() {
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
    
    @java.lang.Override()
    public void onResume() {
    }
    
    private final com.smartx.rfidreader.core.settings.AppSettings buildValidatedSettings(boolean requireUrl) {
        return null;
    }
    
    private final void refreshServiceUi() {
    }
    
    private final void refreshReadingToggleUi() {
    }
    
    private final void syncRunningStateFromService() {
    }
    
    private final void refreshMonitoringUi() {
    }
    
    private final boolean canToggleWebhookNow() {
        return false;
    }
    
    private final void lockToggleButton() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/smartx/rfidreader/ui/main/webhook/WebhookFragment$Companion;", "", "()V", "TOGGLE_DEBOUNCE_MS", "", "TOGGLE_RECOVERY_MS", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
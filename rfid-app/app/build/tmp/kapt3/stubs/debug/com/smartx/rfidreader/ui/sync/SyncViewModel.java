package com.smartx.rfidreader.ui.sync;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u001d\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020\u000fJ\u0006\u0010!\u001a\u00020\u001eJ\u000e\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020$J\u0014\u0010%\u001a\u00020\u001e2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u001e0\'R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00070\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0011R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0011\u00a8\u0006("}, d2 = {"Lcom/smartx/rfidreader/ui/sync/SyncViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_syncProgress", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/smartx/rfidreader/ui/sync/SyncProgressState;", "_uiState", "Lcom/smartx/rfidreader/ui/sync/SyncUiState;", "eventRepo", "Lcom/smartx/rfidreader/core/events/EventRepository;", "events", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/smartx/rfidreader/core/db/EventEntity;", "getEvents", "()Lkotlinx/coroutines/flow/StateFlow;", "pendingCount", "", "getPendingCount", "rfidApp", "Lcom/smartx/rfidreader/RfidApplication;", "settingsRepo", "Lcom/smartx/rfidreader/core/settings/AppSettingsRepository;", "syncProgress", "getSyncProgress", "uiState", "getUiState", "deleteAllEvents", "", "deleteEvent", "event", "resetProgress", "saveWebhookUrl", "url", "", "startSyncWithProgress", "onNoUrl", "Lkotlin/Function0;", "app_debug"})
public final class SyncViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.rfidreader.RfidApplication rfidApp = null;
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.rfidreader.core.events.EventRepository eventRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.rfidreader.core.settings.AppSettingsRepository settingsRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.smartx.rfidreader.ui.sync.SyncUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.ui.sync.SyncUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.smartx.rfidreader.ui.sync.SyncProgressState> _syncProgress = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.ui.sync.SyncProgressState> syncProgress = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartx.rfidreader.core.db.EventEntity>> events = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> pendingCount = null;
    
    public SyncViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.ui.sync.SyncUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.ui.sync.SyncProgressState> getSyncProgress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartx.rfidreader.core.db.EventEntity>> getEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getPendingCount() {
        return null;
    }
    
    public final void saveWebhookUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String url) {
    }
    
    /**
     * Inicia o envio com progresso em tempo real por evento.
     * [onNoUrl] é chamado na thread principal se a URL estiver vazia.
     */
    public final void startSyncWithProgress(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNoUrl) {
    }
    
    public final void resetProgress() {
    }
    
    public final void deleteEvent(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.EventEntity event) {
    }
    
    public final void deleteAllEvents() {
    }
}
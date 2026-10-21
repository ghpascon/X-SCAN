package com.smartx.xscan;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\t\u001a\u00020\n8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\b\u001a\u0004\b\u000b\u0010\fR\u001b\u0010\u000e\u001a\u00020\u000f8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\b\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0015"}, d2 = {"Lcom/smartx/xscan/RfidApplication;", "Landroid/app/Application;", "()V", "database", "Lcom/smartx/xscan/core/db/AppDatabase;", "getDatabase", "()Lcom/smartx/xscan/core/db/AppDatabase;", "database$delegate", "Lkotlin/Lazy;", "eventRepository", "Lcom/smartx/xscan/core/events/EventRepository;", "getEventRepository", "()Lcom/smartx/xscan/core/events/EventRepository;", "eventRepository$delegate", "settingsRepository", "Lcom/smartx/xscan/core/settings/AppSettingsRepository;", "getSettingsRepository", "()Lcom/smartx/xscan/core/settings/AppSettingsRepository;", "settingsRepository$delegate", "onCreate", "", "app_debug"})
public final class RfidApplication extends android.app.Application {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy database$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy eventRepository$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy settingsRepository$delegate = null;
    
    public RfidApplication() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartx.xscan.core.db.AppDatabase getDatabase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartx.xscan.core.events.EventRepository getEventRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartx.xscan.core.settings.AppSettingsRepository getSettingsRepository() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
}
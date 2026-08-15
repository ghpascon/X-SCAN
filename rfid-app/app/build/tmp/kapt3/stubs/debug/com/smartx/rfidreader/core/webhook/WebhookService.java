package com.smartx.rfidreader.core.webhook;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\t\u0018\u0000 92\u00020\u0001:\u00019B\u0005\u00a2\u0006\u0002\u0010\u0002J&\u0010\u001a\u001a\u00020\u00042\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00180\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0011H\u0002J\b\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020!H\u0002J\u0014\u0010#\u001a\u0004\u0018\u00010$2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\b\u0010\'\u001a\u00020!H\u0016J\b\u0010(\u001a\u00020!H\u0016J\"\u0010)\u001a\u00020*2\b\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010+\u001a\u00020*2\u0006\u0010,\u001a\u00020*H\u0016J\u0016\u0010-\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001eH\u0082@\u00a2\u0006\u0002\u0010.J:\u0010/\u001a\u0010\u0012\u0004\u0012\u000201\u0012\u0006\u0012\u0004\u0018\u00010\u0004002\u0006\u00102\u001a\u00020\u00042\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00180\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0082@\u00a2\u0006\u0002\u00103J\u000e\u00104\u001a\b\u0012\u0004\u0012\u00020\u00180\u001cH\u0002J\b\u00105\u001a\u00020!H\u0002J\b\u00106\u001a\u00020!H\u0002J\u0010\u00107\u001a\u00020!2\u0006\u00108\u001a\u00020\u0018H\u0002R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\b\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00180\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006:"}, d2 = {"Lcom/smartx/rfidreader/core/webhook/WebhookService;", "Landroid/app/Service;", "()V", "deviceId", "", "getDeviceId", "()Ljava/lang/String;", "deviceId$delegate", "Lkotlin/Lazy;", "httpClient", "Lokhttp3/OkHttpClient;", "getHttpClient", "()Lokhttp3/OkHttpClient;", "httpClient$delegate", "isoFormat", "Ljava/text/SimpleDateFormat;", "lastKnownReaderConfig", "Lcom/smartx/rfidreader/core/reader/ReaderConfig;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "tagJob", "Lkotlinx/coroutines/Job;", "tagsMap", "Ljava/util/LinkedHashMap;", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "tickerJob", "buildWebhookPayload", "tags", "", "settings", "Lcom/smartx/rfidreader/core/settings/AppSettings;", "readerConfig", "clearCollectedTags", "", "createChannel", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "resolveReaderConfig", "(Lcom/smartx/rfidreader/core/settings/AppSettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendPost", "Lkotlin/Pair;", "", "url", "(Ljava/lang/String;Ljava/util/List;Lcom/smartx/rfidreader/core/settings/AppSettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "snapshotTags", "startForegroundServiceWork", "stopForegroundServiceWork", "upsertTag", "tag", "Companion", "app_debug"})
public final class WebhookService extends android.app.Service {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "WebhookService";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_START = "com.smartx.rfidreader.action.START_WEBHOOK";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP = "com.smartx.rfidreader.action.STOP_WEBHOOK";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "webhook_channel";
    private static final int NOTIF_ID = 43981;
    @kotlin.jvm.Volatile()
    private static volatile boolean isRunning = false;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy httpClient$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat isoFormat = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy deviceId$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.LinkedHashMap<java.lang.String, com.smartx.rfidreader.core.reader.RfidTag> tagsMap = null;
    @org.jetbrains.annotations.NotNull()
    private com.smartx.rfidreader.core.reader.ReaderConfig lastKnownReaderConfig;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job tagJob;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job tickerJob;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.rfidreader.core.webhook.WebhookService.Companion Companion = null;
    
    public WebhookService() {
        super();
    }
    
    private final okhttp3.OkHttpClient getHttpClient() {
        return null;
    }
    
    private final java.lang.String getDeviceId() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void startForegroundServiceWork() {
    }
    
    private final java.lang.Object sendPost(java.lang.String url, java.util.List<com.smartx.rfidreader.core.reader.RfidTag> tags, com.smartx.rfidreader.core.settings.AppSettings settings, kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Boolean, java.lang.String>> $completion) {
        return null;
    }
    
    private final java.lang.Object resolveReaderConfig(com.smartx.rfidreader.core.settings.AppSettings settings, kotlin.coroutines.Continuation<? super com.smartx.rfidreader.core.reader.ReaderConfig> $completion) {
        return null;
    }
    
    private final java.lang.String buildWebhookPayload(java.util.List<com.smartx.rfidreader.core.reader.RfidTag> tags, com.smartx.rfidreader.core.settings.AppSettings settings, com.smartx.rfidreader.core.reader.ReaderConfig readerConfig) {
        return null;
    }
    
    private final java.util.List<com.smartx.rfidreader.core.reader.RfidTag> snapshotTags() {
        return null;
    }
    
    private final void clearCollectedTags() {
    }
    
    private final void upsertTag(com.smartx.rfidreader.core.reader.RfidTag tag) {
    }
    
    private final void stopForegroundServiceWork() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    private final void createChannel() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/smartx/rfidreader/core/webhook/WebhookService$Companion;", "", "()V", "ACTION_START", "", "ACTION_STOP", "CHANNEL_ID", "NOTIF_ID", "", "TAG", "isRunning", "", "()Z", "setRunning", "(Z)V", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final boolean isRunning() {
            return false;
        }
        
        public final void setRunning(boolean p0) {
        }
    }
}
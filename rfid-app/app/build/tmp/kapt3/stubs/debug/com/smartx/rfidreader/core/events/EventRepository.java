package com.smartx.rfidreader.core.events;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0016\u001a\u00020\u00062\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\tH\u0002J\u000e\u0010\u0019\u001a\u00020\u001aH\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u0018\u0010\u001f\u001a\u00020 2\u0006\u0010\u001d\u001a\u00020\n2\u0006\u0010!\u001a\u00020\u0006H\u0002J&\u0010\"\u001a\u0010\u0012\u0004\u0012\u00020 \u0012\u0006\u0012\u0004\u0018\u00010\u00060#2\u0006\u0010\u001d\u001a\u00020\n2\u0006\u0010!\u001a\u00020\u0006H\u0002Jx\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u00062\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\t2\b\b\u0002\u0010\'\u001a\u00020(2\b\b\u0002\u0010)\u001a\u00020(2\b\b\u0002\u0010*\u001a\u00020 2\b\b\u0002\u0010+\u001a\u00020\u00122\b\b\u0002\u0010,\u001a\u00020\u00122\b\b\u0002\u0010-\u001a\u00020\u00122\u000e\b\u0002\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00060\t2\u0006\u0010/\u001a\u00020\u0006H\u0086@\u00a2\u0006\u0002\u00100J\"\u00101\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00120#2\u0006\u00102\u001a\u00020\u0006H\u0086@\u00a2\u0006\u0002\u00103J\u00ad\u0001\u00104\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00120#2\u0006\u00102\u001a\u00020\u00062\u0088\u0001\u00105\u001a\u0083\u0001\b\u0001\u0012\u0013\u0012\u00110\u0012\u00a2\u0006\f\b7\u0012\b\b8\u0012\u0004\b\b(9\u0012\u0013\u0012\u00110\u0012\u00a2\u0006\f\b7\u0012\b\b8\u0012\u0004\b\b(:\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b7\u0012\b\b8\u0012\u0004\b\b(\u001d\u0012\u0013\u0012\u00110 \u00a2\u0006\f\b7\u0012\b\b8\u0012\u0004\b\b(;\u0012\u0015\u0012\u0013\u0018\u00010\u0006\u00a2\u0006\f\b7\u0012\b\b8\u0012\u0004\b\b(<\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0=\u0012\u0006\u0012\u0004\u0018\u00010\u000106H\u0086@\u00a2\u0006\u0002\u0010>R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\fR\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00120\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\f\u00a8\u0006?"}, d2 = {"Lcom/smartx/rfidreader/core/events/EventRepository;", "", "dao", "Lcom/smartx/rfidreader/core/db/EventDao;", "(Lcom/smartx/rfidreader/core/db/EventDao;)V", "TAG", "", "allEventsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/smartx/rfidreader/core/db/EventEntity;", "getAllEventsFlow", "()Lkotlinx/coroutines/flow/Flow;", "httpClient", "Lokhttp3/OkHttpClient;", "isoFormat", "Ljava/text/SimpleDateFormat;", "pendingCountFlow", "", "getPendingCountFlow", "totalCountFlow", "getTotalCountFlow", "buildTagsJson", "tags", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "deleteAllEvents", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteEvent", "event", "(Lcom/smartx/rfidreader/core/db/EventEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "postEvent", "", "url", "postEventSafe", "Lkotlin/Pair;", "saveInventory", "", "deviceId", "gpsLat", "", "gpsLng", "hasGps", "txPower", "session", "rssiFilter", "prefixes", "inventoryName", "(Ljava/lang/String;Ljava/util/List;DDZIIILjava/util/List;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendPending", "webhookUrl", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendPendingWithProgress", "onProgress", "Lkotlin/Function6;", "Lkotlin/ParameterName;", "name", "current", "total", "success", "error", "Lkotlin/coroutines/Continuation;", "(Ljava/lang/String;Lkotlin/jvm/functions/Function6;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class EventRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.rfidreader.core.db.EventDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "EventRepository";
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient httpClient = null;
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat isoFormat = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.EventEntity>> allEventsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Integer> pendingCountFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Integer> totalCountFlow = null;
    
    public EventRepository(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.EventDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.EventEntity>> getAllEventsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getPendingCountFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getTotalCountFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveInventory(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    java.util.List<com.smartx.rfidreader.core.reader.RfidTag> tags, double gpsLat, double gpsLng, boolean hasGps, int txPower, int session, int rssiFilter, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> prefixes, @org.jetbrains.annotations.NotNull()
    java.lang.String inventoryName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteEvent(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.EventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteAllEvents(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Envia todos os eventos pendentes ao webhook.
     * Retorna Pair(successCount, failCount).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendPending(@org.jetbrains.annotations.NotNull()
    java.lang.String webhookUrl, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Integer, java.lang.Integer>> $completion) {
        return null;
    }
    
    /**
     * Envia pendentes com callback de progresso por evento.
     * Apaga do DB em caso de sucesso; mantém em caso de falha.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendPendingWithProgress(@org.jetbrains.annotations.NotNull()
    java.lang.String webhookUrl, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function6<? super java.lang.Integer, ? super java.lang.Integer, ? super com.smartx.rfidreader.core.db.EventEntity, ? super java.lang.Boolean, ? super java.lang.String, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> onProgress, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Integer, java.lang.Integer>> $completion) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Boolean, java.lang.String> postEventSafe(com.smartx.rfidreader.core.db.EventEntity event, java.lang.String url) {
        return null;
    }
    
    private final boolean postEvent(com.smartx.rfidreader.core.db.EventEntity event, java.lang.String url) {
        return false;
    }
    
    private final java.lang.String buildTagsJson(java.util.List<com.smartx.rfidreader.core.reader.RfidTag> tags) {
        return null;
    }
}
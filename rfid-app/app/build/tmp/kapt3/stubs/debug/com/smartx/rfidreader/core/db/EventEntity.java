package com.smartx.rfidreader.core.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b1\b\u0087\b\u0018\u00002\u00020\u0001B\u0095\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u000e\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0016J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\t\u0010-\u001a\u00020\u0010H\u00c6\u0003J\t\u0010.\u001a\u00020\u0010H\u00c6\u0003J\t\u0010/\u001a\u00020\u0010H\u00c6\u0003J\t\u00100\u001a\u00020\u0005H\u00c6\u0003J\t\u00101\u001a\u00020\u000eH\u00c6\u0003J\t\u00102\u001a\u00020\u0005H\u00c6\u0003J\t\u00103\u001a\u00020\u0005H\u00c6\u0003J\t\u00104\u001a\u00020\u0005H\u00c6\u0003J\t\u00105\u001a\u00020\u0005H\u00c6\u0003J\t\u00106\u001a\u00020\u0005H\u00c6\u0003J\t\u00107\u001a\u00020\u0005H\u00c6\u0003J\t\u00108\u001a\u00020\u000bH\u00c6\u0003J\t\u00109\u001a\u00020\u000bH\u00c6\u0003J\t\u0010:\u001a\u00020\u000eH\u00c6\u0003J\u009f\u0001\u0010;\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u0012\u001a\u00020\u00102\b\b\u0002\u0010\u0013\u001a\u00020\u00052\b\b\u0002\u0010\u0014\u001a\u00020\u000e2\b\b\u0002\u0010\u0015\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010<\u001a\u00020\u000e2\b\u0010=\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010>\u001a\u00020\u0010H\u00d6\u0001J\t\u0010?\u001a\u00020\u0005H\u00d6\u0001J\u0006\u0010@\u001a\u00020\u0005R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001bR\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0016\u0010\u0007\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0018R\u0011\u0010\u0014\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u001eR\u0011\u0010\u0013\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0018R\u0011\u0010\u0012\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\t\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0018R\u0011\u0010\u0011\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010$R\u0011\u0010\u0015\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u0018R\u0011\u0010(\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b)\u0010$R\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0018R\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010$\u00a8\u0006A"}, d2 = {"Lcom/smartx/rfidreader/core/db/EventEntity;", "", "id", "", "deviceId", "", "eventType", "inventoryName", "tagsJson", "savedAt", "gpsLat", "", "gpsLng", "hasGps", "", "txPower", "", "session", "rssiFilter", "prefixesJson", "isSynced", "syncedAt", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;DDZIIILjava/lang/String;ZLjava/lang/String;)V", "getDeviceId", "()Ljava/lang/String;", "getEventType", "getGpsLat", "()D", "getGpsLng", "getHasGps", "()Z", "getId", "()J", "getInventoryName", "getPrefixesJson", "getRssiFilter", "()I", "getSavedAt", "getSession", "getSyncedAt", "tagCount", "getTagCount", "getTagsJson", "getTxPower", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "toWebhookJson", "app_debug"})
@androidx.room.Entity(tableName = "rfid_events")
public final class EventEntity {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String deviceId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String eventType = null;
    @androidx.room.ColumnInfo(name = "inventory_name")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String inventoryName = null;
    
    /**
     * JSON string representando a lista de tags
     */
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tagsJson = null;
    
    /**
     * ISO 8601 — momento em que o inventário foi salvo
     */
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String savedAt = null;
    private final double gpsLat = 0.0;
    private final double gpsLng = 0.0;
    private final boolean hasGps = false;
    private final int txPower = 0;
    private final int session = 0;
    private final int rssiFilter = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String prefixesJson = null;
    private final boolean isSynced = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String syncedAt = null;
    
    public EventEntity(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    java.lang.String eventType, @org.jetbrains.annotations.NotNull()
    java.lang.String inventoryName, @org.jetbrains.annotations.NotNull()
    java.lang.String tagsJson, @org.jetbrains.annotations.NotNull()
    java.lang.String savedAt, double gpsLat, double gpsLng, boolean hasGps, int txPower, int session, int rssiFilter, @org.jetbrains.annotations.NotNull()
    java.lang.String prefixesJson, boolean isSynced, @org.jetbrains.annotations.NotNull()
    java.lang.String syncedAt) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDeviceId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEventType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getInventoryName() {
        return null;
    }
    
    /**
     * JSON string representando a lista de tags
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTagsJson() {
        return null;
    }
    
    /**
     * ISO 8601 — momento em que o inventário foi salvo
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSavedAt() {
        return null;
    }
    
    public final double getGpsLat() {
        return 0.0;
    }
    
    public final double getGpsLng() {
        return 0.0;
    }
    
    public final boolean getHasGps() {
        return false;
    }
    
    public final int getTxPower() {
        return 0;
    }
    
    public final int getSession() {
        return 0;
    }
    
    public final int getRssiFilter() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPrefixesJson() {
        return null;
    }
    
    public final boolean isSynced() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSyncedAt() {
        return null;
    }
    
    public final int getTagCount() {
        return 0;
    }
    
    /**
     * Monta o JSON completo pronto para envio ao webhook
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String toWebhookJson() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final int component10() {
        return 0;
    }
    
    public final int component11() {
        return 0;
    }
    
    public final int component12() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    public final boolean component14() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    public final double component7() {
        return 0.0;
    }
    
    public final double component8() {
        return 0.0;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartx.rfidreader.core.db.EventEntity copy(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    java.lang.String eventType, @org.jetbrains.annotations.NotNull()
    java.lang.String inventoryName, @org.jetbrains.annotations.NotNull()
    java.lang.String tagsJson, @org.jetbrains.annotations.NotNull()
    java.lang.String savedAt, double gpsLat, double gpsLng, boolean hasGps, int txPower, int session, int rssiFilter, @org.jetbrains.annotations.NotNull()
    java.lang.String prefixesJson, boolean isSynced, @org.jetbrains.annotations.NotNull()
    java.lang.String syncedAt) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}
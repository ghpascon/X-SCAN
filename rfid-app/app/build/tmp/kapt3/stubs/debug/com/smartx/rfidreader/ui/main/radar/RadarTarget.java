package com.smartx.rfidreader.ui.main.radar;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\u0007\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0014J\t\u0010\u001c\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\tH\u00c6\u0003J8\u0010\u001e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001\u00a2\u0006\u0002\u0010\u001fJ\u0013\u0010 \u001a\u00020\t2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020#H\u00d6\u0001J\t\u0010$\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\fR\u0011\u0010\u0010\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u0015\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0016\u001a\u00020\u00178F\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019\u00a8\u0006%"}, d2 = {"Lcom/smartx/rfidreader/ui/main/radar/RadarTarget;", "", "epc", "", "rssi", "", "lastSeenMs", "", "detectedInSession", "", "(Ljava/lang/String;Ljava/lang/Double;JZ)V", "getDetectedInSession", "()Z", "getEpc", "()Ljava/lang/String;", "isRecentlyRead", "isVisible", "getLastSeenMs", "()J", "getRssi", "()Ljava/lang/Double;", "Ljava/lang/Double;", "signalStrength", "", "getSignalStrength", "()F", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/String;Ljava/lang/Double;JZ)Lcom/smartx/rfidreader/ui/main/radar/RadarTarget;", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class RadarTarget {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String epc = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double rssi = null;
    private final long lastSeenMs = 0L;
    
    /**
     * true se foi detectada na sessão de leitura atual (limpo ao iniciar nova leitura)
     */
    private final boolean detectedInSession = false;
    
    public RadarTarget(@org.jetbrains.annotations.NotNull()
    java.lang.String epc, @org.jetbrains.annotations.Nullable()
    java.lang.Double rssi, long lastSeenMs, boolean detectedInSession) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEpc() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getRssi() {
        return null;
    }
    
    public final long getLastSeenMs() {
        return 0L;
    }
    
    /**
     * true se foi detectada na sessão de leitura atual (limpo ao iniciar nova leitura)
     */
    public final boolean getDetectedInSession() {
        return false;
    }
    
    public final boolean isVisible() {
        return false;
    }
    
    public final boolean isRecentlyRead() {
        return false;
    }
    
    public final float getSignalStrength() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component2() {
        return null;
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartx.rfidreader.ui.main.radar.RadarTarget copy(@org.jetbrains.annotations.NotNull()
    java.lang.String epc, @org.jetbrains.annotations.Nullable()
    java.lang.Double rssi, long lastSeenMs, boolean detectedInSession) {
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
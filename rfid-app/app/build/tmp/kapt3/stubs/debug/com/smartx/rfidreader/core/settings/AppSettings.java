package com.smartx.rfidreader.core.settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u001c\b\u0086\b\u0018\u00002\u00020\u0001BQ\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0007H\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00030\tH\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003JU\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00072\b\b\u0002\u0010\f\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010!\u001a\u00020\u00052\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020\u0007H\u00d6\u0001J\t\u0010$\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u000b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0011\u00a8\u0006%"}, d2 = {"Lcom/smartx/rfidreader/core/settings/AppSettings;", "", "lastReaderId", "", "buzzerEnabled", "", "rssiFilter", "", "prefixes", "", "webhookUrl", "webhookIntervalSeconds", "lastBleAddress", "(Ljava/lang/String;ZILjava/util/List;Ljava/lang/String;ILjava/lang/String;)V", "getBuzzerEnabled", "()Z", "getLastBleAddress", "()Ljava/lang/String;", "getLastReaderId", "getPrefixes", "()Ljava/util/List;", "getRssiFilter", "()I", "getWebhookIntervalSeconds", "getWebhookUrl", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class AppSettings {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String lastReaderId = null;
    private final boolean buzzerEnabled = false;
    private final int rssiFilter = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> prefixes = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String webhookUrl = null;
    private final int webhookIntervalSeconds = 0;
    
    /**
     * Último endereço MAC BLE selecionado (global)
     */
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String lastBleAddress = null;
    
    public AppSettings(@org.jetbrains.annotations.NotNull()
    java.lang.String lastReaderId, boolean buzzerEnabled, int rssiFilter, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> prefixes, @org.jetbrains.annotations.NotNull()
    java.lang.String webhookUrl, int webhookIntervalSeconds, @org.jetbrains.annotations.NotNull()
    java.lang.String lastBleAddress) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLastReaderId() {
        return null;
    }
    
    public final boolean getBuzzerEnabled() {
        return false;
    }
    
    public final int getRssiFilter() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getPrefixes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWebhookUrl() {
        return null;
    }
    
    public final int getWebhookIntervalSeconds() {
        return 0;
    }
    
    /**
     * Último endereço MAC BLE selecionado (global)
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLastBleAddress() {
        return null;
    }
    
    public AppSettings() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final int component3() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    public final int component6() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartx.rfidreader.core.settings.AppSettings copy(@org.jetbrains.annotations.NotNull()
    java.lang.String lastReaderId, boolean buzzerEnabled, int rssiFilter, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> prefixes, @org.jetbrains.annotations.NotNull()
    java.lang.String webhookUrl, int webhookIntervalSeconds, @org.jetbrains.annotations.NotNull()
    java.lang.String lastBleAddress) {
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
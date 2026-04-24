package com.smartx.rfidreader.readers.ih25;

/**
 * Adaptador para o leitor Honeywell IH25.
 * SDK: com.honeywell.rfidservice (RfidManager + RfidReader)
 *
 * Fluxo:
 * 1. connect() → manager.connect(null) → onDeviceConnected → manager.createReader()
 * 2. onReaderCreated(success, reader) → reader pronto, setOnTagReadListener configurado
 * 3. onRfidTriggered(pressed) → dispara startInventory / stopInventory automático
 * 4. disconnect() → reader.release() + manager.disconnect()
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010,\u001a\u00020\t2\u0006\u0010-\u001a\u00020.H\u0096@\u00a2\u0006\u0002\u0010/J\u0016\u00100\u001a\u00020\t2\u0006\u00101\u001a\u000202H\u0096@\u00a2\u0006\u0002\u00103J\u000e\u00104\u001a\u000205H\u0096@\u00a2\u0006\u0002\u00106J\b\u00107\u001a\u00020\tH\u0016J\b\u00108\u001a\u00020\tH\u0016J\b\u00109\u001a\u00020\tH\u0016J\u000e\u0010:\u001a\u00020.H\u0096@\u00a2\u0006\u0002\u00106J\b\u0010;\u001a\u00020\tH\u0016J\b\u0010<\u001a\u00020\tH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u0012X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\u00020\tX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0014\u0010\u001c\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0017R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020\f0#X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u000e\u0010&\u001a\u00020\'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010(\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0017\"\u0004\b*\u0010+\u00a8\u0006="}, d2 = {"Lcom/smartx/rfidreader/readers/ih25/IH25Reader;", "Lcom/smartx/rfidreader/core/reader/IRfidReader;", "()V", "TAG", "", "_connectionState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/smartx/rfidreader/core/reader/ReaderConnectionState;", "_isInventorying", "", "_tagChannel", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "connectStartMs", "", "connectionDeferred", "Lkotlinx/coroutines/CompletableDeferred;", "connectionState", "Lkotlinx/coroutines/flow/StateFlow;", "getConnectionState", "()Lkotlinx/coroutines/flow/StateFlow;", "displayName", "getDisplayName", "()Ljava/lang/String;", "eventListener", "Lcom/honeywell/rfidservice/EventListener;", "isBle", "()Z", "readerId", "getReaderId", "rfidManager", "Lcom/honeywell/rfidservice/RfidManager;", "rfidReader", "Lcom/honeywell/rfidservice/rfid/RfidReader;", "tagFlow", "Lkotlinx/coroutines/flow/Flow;", "getTagFlow", "()Lkotlinx/coroutines/flow/Flow;", "tagReadListener", "Lcom/honeywell/rfidservice/rfid/OnTagReadListener;", "targetMacAddress", "getTargetMacAddress", "setTargetMacAddress", "(Ljava/lang/String;)V", "applyConfig", "config", "Lcom/smartx/rfidreader/core/reader/ReaderConfig;", "(Lcom/smartx/rfidreader/core/reader/ReaderConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "connect", "context", "Landroid/content/Context;", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isInventorying", "onTriggerPressed", "onTriggerReleased", "readConfig", "startInventory", "stopInventory", "app_debug"})
public final class IH25Reader implements com.smartx.rfidreader.core.reader.IRfidReader {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String readerId = "IH25";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = "Honeywell IH25";
    private final boolean isBle = true;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "IH25Reader";
    
    /**
     * Endereço MAC BLE do dispositivo selecionado pelo usuário
     */
    @org.jetbrains.annotations.Nullable()
    private java.lang.String targetMacAddress;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.smartx.rfidreader.core.reader.ReaderConnectionState> _connectionState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.core.reader.ReaderConnectionState> connectionState = null;
    @org.jetbrains.annotations.Nullable()
    private com.honeywell.rfidservice.RfidManager rfidManager;
    @org.jetbrains.annotations.Nullable()
    private com.honeywell.rfidservice.rfid.RfidReader rfidReader;
    private boolean _isInventorying = false;
    private long connectStartMs = 0L;
    
    /**
     * Completa quando onReaderCreated dispara; permite suspender connect() até o leitor estar pronto.
     */
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.CompletableDeferred<java.lang.Boolean> connectionDeferred;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.smartx.rfidreader.core.reader.RfidTag> _tagChannel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.smartx.rfidreader.core.reader.RfidTag> tagFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final com.honeywell.rfidservice.rfid.OnTagReadListener tagReadListener = null;
    @org.jetbrains.annotations.NotNull()
    private final com.honeywell.rfidservice.EventListener eventListener = null;
    
    public IH25Reader() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getReaderId() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getDisplayName() {
        return null;
    }
    
    @java.lang.Override()
    public boolean isBle() {
        return false;
    }
    
    /**
     * Endereço MAC BLE do dispositivo selecionado pelo usuário
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTargetMacAddress() {
        return null;
    }
    
    /**
     * Endereço MAC BLE do dispositivo selecionado pelo usuário
     */
    public final void setTargetMacAddress(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.core.reader.ReaderConnectionState> getConnectionState() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.smartx.rfidreader.core.reader.RfidTag> getTagFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object connect(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object disconnect(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    public boolean startInventory() {
        return false;
    }
    
    @java.lang.Override()
    public boolean stopInventory() {
        return false;
    }
    
    @java.lang.Override()
    public boolean isInventorying() {
        return false;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object applyConfig(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.reader.ReaderConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object readConfig(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.smartx.rfidreader.core.reader.ReaderConfig> $completion) {
        return null;
    }
    
    @java.lang.Override()
    public boolean onTriggerPressed() {
        return false;
    }
    
    @java.lang.Override()
    public boolean onTriggerReleased() {
        return false;
    }
}
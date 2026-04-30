package com.smartx.rfidreader.readers.xr2;

/**
 * Adaptador para o leitor XR2 (Ubx / URFIDLibrary).
 * SDK: com.ubx.usdk (RFIDSDKManager / RfidManager)
 *
 * Conexão: ligar módulo via power(true), aguardar ~1500ms, chamar connect() em thread IO.
 * Escala de potência: SDK usa dBm diretamente em setOutputPower(byte).
 * Modo de inventário: setQueryMode(int) — 0=EPC, 1=EPC+TID, 2=EPC+TID+User.
 * Callback de tags: registerCallback(IRfidCallback) → onInventoryTag(epc, tid, rssi).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010#\u001a\u00020\t2\u0006\u0010$\u001a\u00020%H\u0096@\u00a2\u0006\u0002\u0010&J\u0010\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020\tH\u0002J\u0016\u0010*\u001a\u00020\t2\u0006\u0010+\u001a\u00020,H\u0096@\u00a2\u0006\u0002\u0010-J\u000e\u0010.\u001a\u00020(H\u0096@\u00a2\u0006\u0002\u0010/J\b\u00100\u001a\u00020\tH\u0016J\b\u00101\u001a\u00020\tH\u0016J\b\u00102\u001a\u00020\tH\u0016J\u000e\u00103\u001a\u00020%H\u0096@\u00a2\u0006\u0002\u0010/J\b\u00104\u001a\u00020\tH\u0016J\b\u00105\u001a\u00020\tH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u0011X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0014\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0017\u001a\u00020\tX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0014\u0010\u0019\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\f0 X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"\u00a8\u00066"}, d2 = {"Lcom/smartx/rfidreader/readers/xr2/XR2Reader;", "Lcom/smartx/rfidreader/core/reader/IRfidReader;", "()V", "TAG", "", "_connectionState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/smartx/rfidreader/core/reader/ReaderConnectionState;", "_isInventorying", "", "_tagChannel", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "cachedPower", "", "cachedQueryMode", "connectionState", "Lkotlinx/coroutines/flow/StateFlow;", "getConnectionState", "()Lkotlinx/coroutines/flow/StateFlow;", "displayName", "getDisplayName", "()Ljava/lang/String;", "isBle", "()Z", "readerId", "getReaderId", "rfidManager", "Lcom/ubx/usdk/rfid/RfidManager;", "tagCallback", "Lcom/ubx/usdk/rfid/aidl/IRfidCallback;", "tagFlow", "Lkotlinx/coroutines/flow/Flow;", "getTagFlow", "()Lkotlinx/coroutines/flow/Flow;", "applyConfig", "config", "Lcom/smartx/rfidreader/core/reader/ReaderConfig;", "(Lcom/smartx/rfidreader/core/reader/ReaderConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "configureTriggerForRfid", "", "forRfid", "connect", "context", "Landroid/content/Context;", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isInventorying", "onTriggerPressed", "onTriggerReleased", "readConfig", "startInventory", "stopInventory", "app_debug"})
public final class XR2Reader implements com.smartx.rfidreader.core.reader.IRfidReader {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String readerId = "XR2";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = "XR2";
    private final boolean isBle = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "XR2Reader";
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.smartx.rfidreader.core.reader.ReaderConnectionState> _connectionState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.core.reader.ReaderConnectionState> connectionState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.smartx.rfidreader.core.reader.RfidTag> _tagChannel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.smartx.rfidreader.core.reader.RfidTag> tagFlow = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile com.ubx.usdk.rfid.RfidManager rfidManager;
    @kotlin.jvm.Volatile()
    private volatile boolean _isInventorying = false;
    private int cachedPower = 30;
    private int cachedQueryMode = 1;
    @org.jetbrains.annotations.NotNull()
    private final com.ubx.usdk.rfid.aidl.IRfidCallback tagCallback = null;
    
    public XR2Reader() {
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
    
    /**
     * Configura o keycode 523 (gatilho físico) para RFID ou código de barras.
     * [forRfid] = true  → remove 523 da lista de scan (trigger aciona RFID via KeyEvent)
     * [forRfid] = false → restaura 523 na lista de scan (trigger aciona leitor óptico)
     */
    private final void configureTriggerForRfid(boolean forRfid) {
    }
}
package com.smartx.rfidreader.readers.tsl1128;

/**
 * Adaptador para o leitor TSL 1128.
 * Padrão: InventoryModel.java do sample TSL — dois objetos InventoryCommand separados:
 *  • inventoryResponder: passivo, adicionado ao chain, captura respostas e callbacks
 *  • inventoryCommand:   ativo, executado a cada round (contém power + session atuais)
 *
 * Loop contínuo: responseEnded() chama executeCommand(inventoryCommand) DIRETAMENTE
 * (sem ioScope), exatamente como o sample da TSL — o callback já está em thread BG.
 *
 * startInventory() / stopInventory() usam ioScope porque podem ser chamados da
 * main thread (trigger KeyEvent, botão UI).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0098\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u00100\u001a\u00020\u00122\u0006\u00101\u001a\u000202H\u0096@\u00a2\u0006\u0002\u00103J\u0010\u00104\u001a\u0002052\u0006\u00106\u001a\u00020\u001eH\u0002J\u0010\u00107\u001a\u0002052\u0006\u00108\u001a\u000209H\u0002J\u0016\u0010:\u001a\u00020\u00122\u0006\u0010;\u001a\u00020<H\u0096@\u00a2\u0006\u0002\u0010=J\u000e\u0010>\u001a\u000205H\u0096@\u00a2\u0006\u0002\u0010?J\u0012\u0010@\u001a\u0004\u0018\u00010\u00192\u0006\u0010A\u001a\u00020\u0007H\u0002J\b\u0010B\u001a\u00020\u0012H\u0016J\u0010\u0010C\u001a\u0002052\u0006\u0010D\u001a\u00020EH\u0002J\b\u0010F\u001a\u00020\u0012H\u0016J\b\u0010G\u001a\u00020\u0012H\u0016J\u000e\u0010H\u001a\u000202H\u0096@\u00a2\u0006\u0002\u0010?J\u0010\u0010I\u001a\u00020J2\u0006\u0010K\u001a\u00020\u0004H\u0002J\b\u0010L\u001a\u00020\u0012H\u0016J\b\u0010M\u001a\u00020\u0012H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\n0\u0014X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\u00020\u0007X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\"\u001a\u00020\u0012X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0014\u0010$\u001a\u00020\u0007X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001cR\u000e\u0010&\u001a\u00020\'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010(\u001a\b\u0012\u0004\u0012\u00020\r0)X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u001c\u0010,\u001a\u0004\u0018\u00010\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u001c\"\u0004\b.\u0010/\u00a8\u0006N"}, d2 = {"Lcom/smartx/rfidreader/readers/tsl1128/Tsl1128Reader;", "Lcom/smartx/rfidreader/core/reader/IRfidReader;", "()V", "POWER_MAX", "", "POWER_MIN", "TAG", "", "_connectionState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/smartx/rfidreader/core/reader/ReaderConnectionState;", "_tagChannel", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "cachedPower", "cachedSession", "connectDeferred", "Lkotlinx/coroutines/CompletableDeferred;", "", "connectionState", "Lkotlinx/coroutines/flow/StateFlow;", "getConnectionState", "()Lkotlinx/coroutines/flow/StateFlow;", "continuousScanEnabled", "currentReader", "Lcom/uk/tsl/rfid/asciiprotocol/device/Reader;", "displayName", "getDisplayName", "()Ljava/lang/String;", "inventoryCommand", "Lcom/uk/tsl/rfid/asciiprotocol/commands/InventoryCommand;", "inventoryResponder", "ioScope", "Lkotlinx/coroutines/CoroutineScope;", "isBle", "()Z", "readerId", "getReaderId", "switchResponder", "Lcom/uk/tsl/rfid/asciiprotocol/responders/SwitchResponder;", "tagFlow", "Lkotlinx/coroutines/flow/Flow;", "getTagFlow", "()Lkotlinx/coroutines/flow/Flow;", "targetMacAddress", "getTargetMacAddress", "setTargetMacAddress", "(Ljava/lang/String;)V", "applyConfig", "config", "Lcom/smartx/rfidreader/core/reader/ReaderConfig;", "(Lcom/smartx/rfidreader/core/reader/ReaderConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "applyParamsToCommand", "", "cmd", "configureSwitchActions", "commander", "Lcom/uk/tsl/rfid/asciiprotocol/AsciiCommander;", "connect", "context", "Landroid/content/Context;", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findReaderByMac", "mac", "isInventorying", "onTransponderReceived", "transponder", "Lcom/uk/tsl/rfid/asciiprotocol/responders/TransponderData;", "onTriggerPressed", "onTriggerReleased", "readConfig", "sessionToQuerySession", "Lcom/uk/tsl/rfid/asciiprotocol/enumerations/QuerySession;", "session", "startInventory", "stopInventory", "app_debug"})
public final class Tsl1128Reader implements com.smartx.rfidreader.core.reader.IRfidReader {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String readerId = "TSL1128";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = "TSL 1128";
    private final boolean isBle = true;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "Tsl1128Reader";
    @org.jetbrains.annotations.Nullable()
    private java.lang.String targetMacAddress;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.smartx.rfidreader.core.reader.ReaderConnectionState> _connectionState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.core.reader.ReaderConnectionState> connectionState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.smartx.rfidreader.core.reader.RfidTag> _tagChannel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.smartx.rfidreader.core.reader.RfidTag> tagFlow = null;
    @org.jetbrains.annotations.Nullable()
    private com.uk.tsl.rfid.asciiprotocol.device.Reader currentReader;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.CompletableDeferred<java.lang.Boolean> connectDeferred;
    private final int POWER_MIN = 10;
    private final int POWER_MAX = 29;
    @kotlin.jvm.Volatile()
    private volatile int cachedPower;
    @kotlin.jvm.Volatile()
    private volatile int cachedSession = 0;
    @kotlin.jvm.Volatile()
    private volatile boolean continuousScanEnabled = false;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope ioScope = null;
    @org.jetbrains.annotations.NotNull()
    private final com.uk.tsl.rfid.asciiprotocol.commands.InventoryCommand inventoryCommand = null;
    @org.jetbrains.annotations.NotNull()
    private final com.uk.tsl.rfid.asciiprotocol.commands.InventoryCommand inventoryResponder = null;
    @org.jetbrains.annotations.NotNull()
    private final com.uk.tsl.rfid.asciiprotocol.responders.SwitchResponder switchResponder = null;
    
    public Tsl1128Reader() {
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTargetMacAddress() {
        return null;
    }
    
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
    
    private final void onTransponderReceived(com.uk.tsl.rfid.asciiprotocol.responders.TransponderData transponder) {
    }
    
    /**
     * Stampa power e session atuais num objeto InventoryCommand existente.
     */
    private final void applyParamsToCommand(com.uk.tsl.rfid.asciiprotocol.commands.InventoryCommand cmd) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object connect(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    /**
     * Desativa ação de HW no trigger e habilita report assíncrono de SwitchState.
     */
    private final void configureSwitchActions(com.uk.tsl.rfid.asciiprotocol.AsciiCommander commander) {
    }
    
    private final com.uk.tsl.rfid.asciiprotocol.device.Reader findReaderByMac(java.lang.String mac) {
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
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object disconnect(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.uk.tsl.rfid.asciiprotocol.enumerations.QuerySession sessionToQuerySession(int session) {
        return null;
    }
}
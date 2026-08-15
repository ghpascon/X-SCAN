package com.smartx.rfidreader.readers.cfh301;

/**
 * Adaptador para CF-H301 (protocolo 816UBT) via BLE.
 *
 * O demo oficial utiliza a característica 0xFFE1 para escrita e notificação
 * de frames binários com CRC-16 (polinômio 0x8408, init 0xFFFF).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ce\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u001c\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u0005\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010E\u001a\u00020F2\u0006\u0010G\u001a\u00020\u001b2\u0006\u0010H\u001a\u00020\u0016H\u0002J\u0016\u0010I\u001a\u00020\r2\u0006\u0010J\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010KJ\u0016\u0010L\u001a\u00020\r2\u0006\u0010M\u001a\u00020\u0016H\u0082@\u00a2\u0006\u0002\u0010NJ\"\u0010O\u001a\u00020\u001b2\u0006\u0010P\u001a\u00020\u00162\u0006\u0010Q\u001a\u00020\u00162\b\b\u0002\u0010R\u001a\u00020\u001bH\u0002J\b\u0010S\u001a\u00020\u001bH\u0002J\u0018\u0010T\u001a\u00020\u001b2\u0006\u0010U\u001a\u00020\u00162\u0006\u0010V\u001a\u00020\rH\u0002J\u0010\u0010W\u001a\u00020\u001b2\u0006\u0010X\u001a\u00020\u0016H\u0002J\u0018\u0010Y\u001a\u00020\u001b2\u0006\u0010Z\u001a\u00020\u00162\u0006\u0010[\u001a\u00020\u0016H\u0002J\u0016\u0010\\\u001a\u00020F2\f\u0010]\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aH\u0002J\b\u0010^\u001a\u00020FH\u0002J\u0010\u0010_\u001a\u00020F2\u0006\u0010`\u001a\u00020\rH\u0002J\u0016\u0010a\u001a\u00020\r2\u0006\u0010b\u001a\u00020cH\u0096@\u00a2\u0006\u0002\u0010dJ\u000e\u0010e\u001a\u00020FH\u0096@\u00a2\u0006\u0002\u0010fJ\b\u0010g\u001a\u00020FH\u0002J\u0018\u0010h\u001a\u0004\u0018\u00010C2\f\u0010i\u001a\b\u0012\u0004\u0012\u00020k0jH\u0002J\u0010\u0010l\u001a\u00020F2\u0006\u0010G\u001a\u00020\u001bH\u0002J\u0012\u0010m\u001a\u0004\u0018\u00010\u001b2\u0006\u0010n\u001a\u00020\bH\u0002J\u0010\u0010o\u001a\u00020\r2\u0006\u0010p\u001a\u00020\u001bH\u0002J\b\u0010q\u001a\u00020\rH\u0016J\b\u0010r\u001a\u00020\rH\u0016J\b\u0010s\u001a\u00020\rH\u0016J\u0010\u0010t\u001a\u00020F2\u0006\u0010p\u001a\u00020\u001bH\u0002J\u0010\u0010u\u001a\u00020F2\u0006\u0010p\u001a\u00020\u001bH\u0002J\u000e\u0010v\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010fJ\u0016\u0010w\u001a\u00020\b2\u0006\u0010x\u001a\u00020\bH\u0082@\u00a2\u0006\u0002\u0010yJ\u0012\u0010z\u001a\u00020F2\b\b\u0002\u0010{\u001a\u00020\rH\u0002J\u0010\u0010|\u001a\u00020F2\u0006\u0010}\u001a\u00020\bH\u0002J+\u0010~\u001a\u0004\u0018\u00010\u001b2\u0006\u0010p\u001a\u00020\u001b2\u0006\u0010\u007f\u001a\u00020\u00162\b\u0010\u0080\u0001\u001a\u00030\u0081\u0001H\u0082@\u00a2\u0006\u0003\u0010\u0082\u0001J\u0018\u0010\u0083\u0001\u001a\u00020\r2\u0006\u0010p\u001a\u00020\u001bH\u0082@\u00a2\u0006\u0003\u0010\u0084\u0001J\t\u0010\u0085\u0001\u001a\u00020\rH\u0016J\t\u0010\u0086\u0001\u001a\u00020\rH\u0016J$\u0010\u0087\u0001\u001a\u00020\b2\u0007\u0010\u0088\u0001\u001a\u00020\u001b2\u0007\u0010\u0089\u0001\u001a\u00020\u00162\u0007\u0010\u008a\u0001\u001a\u00020\u0016H\u0002J\u0013\u0010\u008b\u0001\u001a\u00020\u00162\b\u0010\u008c\u0001\u001a\u00030\u008d\u0001H\u0002J\u0011\u0010\u008e\u0001\u001a\u00020\r2\u0006\u0010p\u001a\u00020\u001bH\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001c\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001fX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0014\u0010\"\u001a\u00020\bX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u000e\u0010%\u001a\u00020&X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010(\u001a\u0004\u0018\u00010)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010*\u001a\u00020\rX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u000e\u0010,\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010-\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010.\u001a\u00020\bX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010$R\u000e\u00100\u001a\u000201X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u000203X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u000205X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u00106\u001a\b\u0012\u0004\u0012\u00020\u001007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u00109R\u001c\u0010:\u001a\u0004\u0018\u00010\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b;\u0010$\"\u0004\b<\u0010=R\u001a\u0010>\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0?X\u0082\u0004\u00a2\u0006\u0002\n\u0000RN\u0010@\u001aB\u0012\f\u0012\n \u0005*\u0004\u0018\u00010\b0\b\u0012\f\u0012\n \u0005*\u0004\u0018\u00010\r0\r \u0005* \u0012\f\u0012\n \u0005*\u0004\u0018\u00010\b0\b\u0012\f\u0012\n \u0005*\u0004\u0018\u00010\r0\r\u0018\u00010A0AX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010B\u001a\u0004\u0018\u00010CX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010D\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u008f\u0001"}, d2 = {"Lcom/smartx/rfidreader/readers/cfh301/CfH301Reader;", "Lcom/smartx/rfidreader/core/reader/IRfidReader;", "()V", "CCCD_UUID", "Ljava/util/UUID;", "kotlin.jvm.PlatformType", "FFE1_UUID", "TAG", "", "_connectionState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/smartx/rfidreader/core/reader/ReaderConnectionState;", "_isInventorying", "", "_tagChannel", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "bluetoothGatt", "Landroid/bluetooth/BluetoothGatt;", "cachedConfig", "Lcom/smartx/rfidreader/core/reader/ReaderConfig;", "commandAddress", "", "commandMutex", "Lkotlinx/coroutines/sync/Mutex;", "commandResponses", "Lkotlinx/coroutines/channels/Channel;", "", "connectDeferred", "Lkotlinx/coroutines/CompletableDeferred;", "connectionState", "Lkotlinx/coroutines/flow/StateFlow;", "getConnectionState", "()Lkotlinx/coroutines/flow/StateFlow;", "displayName", "getDisplayName", "()Ljava/lang/String;", "gattCallback", "Landroid/bluetooth/BluetoothGattCallback;", "inventoryFrames", "inventoryJob", "Lkotlinx/coroutines/Job;", "isBle", "()Z", "mtuPayload", "notifyDeferred", "readerId", "getReaderId", "readerScope", "Lkotlinx/coroutines/CoroutineScope;", "rxBuffer", "Ljava/io/ByteArrayOutputStream;", "rxLock", "", "tagFlow", "Lkotlinx/coroutines/flow/Flow;", "getTagFlow", "()Lkotlinx/coroutines/flow/Flow;", "targetMacAddress", "getTargetMacAddress", "setTargetMacAddress", "(Ljava/lang/String;)V", "tidCache", "Ljava/util/concurrent/ConcurrentHashMap;", "tidInflight", "Ljava/util/concurrent/ConcurrentHashMap$KeySetView;", "txRxCharacteristic", "Landroid/bluetooth/BluetoothGattCharacteristic;", "writeDeferred", "appendCrc", "", "data", "dataLen", "applyConfig", "config", "(Lcom/smartx/rfidreader/core/reader/ReaderConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "applyRegionIfSupported", "region", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buildCommand", "address", "cmd", "payload", "buildGetReaderInfoCommand", "buildInventoryCommand", "session", "includeTid", "buildSetPowerCommand", "power", "buildSetRegionCommand", "maxFre", "minFre", "clearChannel", "channel", "closeGattInternal", "completeWrite", "ok", "connect", "context", "Landroid/content/Context;", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "drainInventoryFrames", "findFfe1Characteristic", "services", "", "Landroid/bluetooth/BluetoothGattService;", "handleIncoming", "hexToBytes", "hex", "isInventoryTagFrame", "frame", "isInventorying", "onTriggerPressed", "onTriggerReleased", "parseInventoryFrame", "parseReaderInfo", "readConfig", "readTidForEpc", "epcHex", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resetRuntimeState", "clearCaches", "scheduleTidRead", "epc", "sendCommand", "expectedCmd", "timeoutMs", "", "([BIJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendRaw", "([BLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startInventory", "stopInventory", "toHex", "bytes", "offset", "length", "u", "b", "", "verifyFrameCrc", "app_debug"})
@android.annotation.SuppressLint(value = {"MissingPermission"})
public final class CfH301Reader implements com.smartx.rfidreader.core.reader.IRfidReader {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String readerId = "CF-H301";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = "CF-H301";
    private final boolean isBle = true;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String targetMacAddress;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "CfH301Reader";
    private final java.util.UUID FFE1_UUID = null;
    private final java.util.UUID CCCD_UUID = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.smartx.rfidreader.core.reader.ReaderConnectionState> _connectionState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.core.reader.ReaderConnectionState> connectionState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.smartx.rfidreader.core.reader.RfidTag> _tagChannel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.smartx.rfidreader.core.reader.RfidTag> tagFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope readerScope = null;
    @org.jetbrains.annotations.Nullable()
    private android.bluetooth.BluetoothGatt bluetoothGatt;
    @org.jetbrains.annotations.Nullable()
    private android.bluetooth.BluetoothGattCharacteristic txRxCharacteristic;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.CompletableDeferred<java.lang.Boolean> connectDeferred;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.CompletableDeferred<java.lang.Boolean> notifyDeferred;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.CompletableDeferred<java.lang.Boolean> writeDeferred;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.sync.Mutex commandMutex = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<byte[]> commandResponses = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<byte[]> inventoryFrames = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Object rxLock = null;
    @org.jetbrains.annotations.NotNull()
    private final java.io.ByteArrayOutputStream rxBuffer = null;
    @kotlin.jvm.Volatile()
    private volatile int mtuPayload = 20;
    @kotlin.jvm.Volatile()
    private volatile int commandAddress = 0;
    @kotlin.jvm.Volatile()
    private volatile boolean _isInventorying = false;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job inventoryJob;
    @org.jetbrains.annotations.NotNull()
    private com.smartx.rfidreader.core.reader.ReaderConfig cachedConfig;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.String> tidCache = null;
    private final java.util.concurrent.ConcurrentHashMap.KeySetView<java.lang.String, java.lang.Boolean> tidInflight = null;
    @org.jetbrains.annotations.NotNull()
    private final android.bluetooth.BluetoothGattCallback gattCallback = null;
    
    public CfH301Reader() {
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
    
    private final java.lang.Object sendCommand(byte[] frame, int expectedCmd, long timeoutMs, kotlin.coroutines.Continuation<? super byte[]> $completion) {
        return null;
    }
    
    private final java.lang.Object sendRaw(byte[] frame, kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final void handleIncoming(byte[] data) {
    }
    
    private final void drainInventoryFrames() {
    }
    
    private final void parseInventoryFrame(byte[] frame) {
    }
    
    private final void scheduleTidRead(java.lang.String epc) {
    }
    
    private final java.lang.Object readTidForEpc(java.lang.String epcHex, kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    private final java.lang.Object applyRegionIfSupported(int region, kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final void parseReaderInfo(byte[] frame) {
    }
    
    private final byte[] buildGetReaderInfoCommand() {
        return null;
    }
    
    private final byte[] buildSetPowerCommand(int power) {
        return null;
    }
    
    private final byte[] buildSetRegionCommand(int maxFre, int minFre) {
        return null;
    }
    
    private final byte[] buildInventoryCommand(int session, boolean includeTid) {
        return null;
    }
    
    private final byte[] buildCommand(int address, int cmd, byte[] payload) {
        return null;
    }
    
    private final void appendCrc(byte[] data, int dataLen) {
    }
    
    private final boolean verifyFrameCrc(byte[] frame) {
        return false;
    }
    
    private final boolean isInventoryTagFrame(byte[] frame) {
        return false;
    }
    
    private final android.bluetooth.BluetoothGattCharacteristic findFfe1Characteristic(java.util.List<? extends android.bluetooth.BluetoothGattService> services) {
        return null;
    }
    
    private final void completeWrite(boolean ok) {
    }
    
    private final void closeGattInternal() {
    }
    
    private final void resetRuntimeState(boolean clearCaches) {
    }
    
    private final void clearChannel(kotlinx.coroutines.channels.Channel<byte[]> channel) {
    }
    
    private final java.lang.String toHex(byte[] bytes, int offset, int length) {
        return null;
    }
    
    private final byte[] hexToBytes(java.lang.String hex) {
        return null;
    }
    
    private final int u(byte b) {
        return 0;
    }
}
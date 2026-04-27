package com.smartx.rfidreader.readers.x714;

/**
 * Adaptador para o leitor próprio X714 via BLE (Nordic UART Service — NUS).
 *
 * Protocolo:
 * - Serviço NUS:  6E400001-B5A3-F393-E0A9-E50E24DCCA9E
 * - RX (write):   6E400002-B5A3-F393-E0A9-E50E24DCCA9E  ← app envia comandos
 * - TX (notify):  6E400003-B5A3-F393-E0A9-E50E24DCCA9E  ← leitor notifica eventos
 *
 * Setup: envia os comandos de configuração um a um ao conectar (gpi_start sempre off).
 *       Só considera conectado ao receber "#setup_done".
 *
 * Inicio/parada de leitura:
 * - Comando software: envia "#read:on" / "#read:off"
 * - Gatilho físico GPI: recebe "#in_1:on" / "#in_1:off" → mesma ação
 * - Resposta do leitor: "#read:on" / "#read:off" (quando iniciado por software)
 * Ambos os modos funcionam simultaneamente.
 *
 * Tags: "#t+@epc|tid|ant|rssi|protect"
 * Configs:
 * - Ler potência:  envia "#get_power"   → recebe "#power:x"
 * - Ler session:   envia "#get_session" → recebe "#session:x"
 * - Setar potência: envia "#read_power:x" → recebe "#power:x"
 * - Setar session:  envia "#session:x"    → recebe "#session:x"
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00be\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0017\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010P\u001a\u00020\u00132\u0006\u0010Q\u001a\u00020RH\u0096@\u00a2\u0006\u0002\u0010SJ\u0016\u0010T\u001a\u00020\u00132\u0006\u0010U\u001a\u00020VH\u0096@\u00a2\u0006\u0002\u0010WJ\u000e\u0010X\u001a\u000200H\u0096@\u00a2\u0006\u0002\u0010YJ\b\u0010Z\u001a\u000200H\u0002J\u0010\u0010[\u001a\u0002002\u0006\u0010\\\u001a\u00020\u000bH\u0002J\b\u0010]\u001a\u00020\u0013H\u0016J\u0010\u0010^\u001a\u0002002\u0006\u0010_\u001a\u00020\u000bH\u0002J\b\u0010`\u001a\u00020\u0013H\u0016J\b\u0010a\u001a\u00020\u0013H\u0016J\u0010\u0010b\u001a\u0002002\u0006\u0010c\u001a\u00020\u000bH\u0002J\u0010\u0010d\u001a\u0002002\u0006\u0010\\\u001a\u00020\u000bH\u0002J\u000e\u0010e\u001a\u00020RH\u0096@\u00a2\u0006\u0002\u0010YJ\u0010\u0010f\u001a\u0002002\u0006\u0010g\u001a\u00020\u000bH\u0002J\b\u0010h\u001a\u000200H\u0002J\b\u0010i\u001a\u00020\u0013H\u0016J\b\u0010j\u001a\u000200H\u0002J\b\u0010k\u001a\u00020\u0013H\u0016J\b\u0010l\u001a\u000200H\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00110\u001bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0014\u0010\u001e\u001a\u00020\u000bX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00130\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001dR\u0014\u0010\'\u001a\u00020\u0013X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010(R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010+\u001a\u00060,j\u0002`-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R(\u0010.\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u000200\u0018\u00010/X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u000e\u00105\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u00106\u001a\n\u0012\u0004\u0012\u00020\u0018\u0018\u000107X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u00108\u001a\u00020\u000bX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010 R\u000e\u0010:\u001a\u00020;X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010<\u001a\u0004\u0018\u00010=X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010>\u001a\n\u0012\u0004\u0012\u00020\u0018\u0018\u000107X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010?\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u000107X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010@\u001a\b\u0012\u0004\u0012\u00020\u00160AX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010CR\u001c\u0010D\u001a\u0004\u0018\u00010\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bE\u0010 \"\u0004\bF\u0010GR\u0010\u0010H\u001a\u0004\u0018\u00010IX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020LX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010M\u001a\b\u0012\u0004\u0012\u00020*0NX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010O\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006m"}, d2 = {"Lcom/smartx/rfidreader/readers/x714/X714Reader;", "Lcom/smartx/rfidreader/core/reader/IRfidReader;", "()V", "CCCD_UUID", "Ljava/util/UUID;", "kotlin.jvm.PlatformType", "CHAR_RX_UUID", "CHAR_TX_UUID", "SERVICE_UUID", "SETUP_COMMANDS", "", "", "TAG", "WRITE_TIMEOUT_MS", "", "_connectionState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/smartx/rfidreader/core/reader/ReaderConnectionState;", "_inventoryState", "", "_tagChannel", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "cachedPower", "", "cachedSession", "connectionState", "Lkotlinx/coroutines/flow/StateFlow;", "getConnectionState", "()Lkotlinx/coroutines/flow/StateFlow;", "displayName", "getDisplayName", "()Ljava/lang/String;", "gatt", "Landroid/bluetooth/BluetoothGatt;", "gattCallback", "Landroid/bluetooth/BluetoothGattCallback;", "inventoryStateFlow", "getInventoryStateFlow", "isBle", "()Z", "lastSentChunk", "", "lineBuffer", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "logSink", "Lkotlin/Function1;", "", "getLogSink", "()Lkotlin/jvm/functions/Function1;", "setLogSink", "(Lkotlin/jvm/functions/Function1;)V", "mtuPayload", "powerDeferred", "Lkotlinx/coroutines/CompletableDeferred;", "readerId", "getReaderId", "readerScope", "Lkotlinx/coroutines/CoroutineScope;", "rxChar", "Landroid/bluetooth/BluetoothGattCharacteristic;", "sessionDeferred", "setupDeferred", "tagFlow", "Lkotlinx/coroutines/flow/Flow;", "getTagFlow", "()Lkotlinx/coroutines/flow/Flow;", "targetMacAddress", "getTargetMacAddress", "setTargetMacAddress", "(Ljava/lang/String;)V", "watchdogJob", "Lkotlinx/coroutines/Job;", "writeInProgress", "writeLock", "", "writeQueue", "Ljava/util/concurrent/LinkedBlockingQueue;", "writeSentAtMs", "applyConfig", "config", "Lcom/smartx/rfidreader/core/reader/ReaderConfig;", "(Lcom/smartx/rfidreader/core/reader/ReaderConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "connect", "context", "Landroid/content/Context;", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "drainWriteQueue", "enqueueWrite", "text", "isInventorying", "log", "msg", "onTriggerPressed", "onTriggerReleased", "parseTag", "line", "processIncoming", "readConfig", "sendCommand", "cmd", "sendSetupCommands", "startInventory", "startWriteWatchdog", "stopInventory", "stopWriteWatchdog", "app_debug"})
@android.annotation.SuppressLint(value = {"MissingPermission"})
public final class X714Reader implements com.smartx.rfidreader.core.reader.IRfidReader {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String readerId = "X714";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = "X714";
    private final boolean isBle = true;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "X714Reader";
    private final java.util.UUID SERVICE_UUID = null;
    private final java.util.UUID CHAR_RX_UUID = null;
    private final java.util.UUID CHAR_TX_UUID = null;
    private final java.util.UUID CCCD_UUID = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> SETUP_COMMANDS = null;
    
    /**
     * Endereço MAC BLE selecionado antes de connect()
     */
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
    private android.bluetooth.BluetoothGatt gatt;
    @org.jetbrains.annotations.Nullable()
    private android.bluetooth.BluetoothGattCharacteristic rxChar;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _inventoryState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> inventoryStateFlow = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile kotlinx.coroutines.CompletableDeferred<java.lang.Boolean> setupDeferred;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile kotlinx.coroutines.CompletableDeferred<java.lang.Integer> powerDeferred;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile kotlinx.coroutines.CompletableDeferred<java.lang.Integer> sessionDeferred;
    private int cachedPower = 20;
    private int cachedSession = 1;
    
    /**
     * Buffer para montar linhas de fragmentos BLE
     */
    @org.jetbrains.annotations.NotNull()
    private final java.lang.StringBuilder lineBuffer = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.LinkedBlockingQueue<byte[]> writeQueue = null;
    @kotlin.jvm.Volatile()
    private volatile boolean writeInProgress = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Object writeLock = null;
    
    /**
     * Guarda o último chunk enviado para permitir 1 retry em caso de falha GATT.
     */
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile byte[] lastSentChunk;
    @kotlin.jvm.Volatile()
    private volatile long writeSentAtMs = 0L;
    private final long WRITE_TIMEOUT_MS = 3000L;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope readerScope = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job watchdogJob;
    
    /**
     * Payload máximo por pacote BLE (atualizado após negociação MTU via onMtuChanged).
     * Padrão BLE: 23 bytes ATT − 3 bytes overhead = 20 bytes de dados.
     * IMPORTANTE: comandos como "#protected_inventory:off\n" têm 26 bytes — excedem o padrão!
     * Por isso iniciamos em 20 e atualizamos após requestMtu.
     */
    @kotlin.jvm.Volatile()
    private volatile int mtuPayload = 20;
    
    /**
     * Callback opcional para exibir progresso de conexão na UI.
     * Atribuído pelo MainViewModel antes de chamar connect().
     */
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> logSink;
    @org.jetbrains.annotations.NotNull()
    private final android.bluetooth.BluetoothGattCallback gattCallback = null;
    
    public X714Reader() {
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
     * Endereço MAC BLE selecionado antes de connect()
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTargetMacAddress() {
        return null;
    }
    
    /**
     * Endereço MAC BLE selecionado antes de connect()
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
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getInventoryStateFlow() {
        return null;
    }
    
    private final void startWriteWatchdog() {
    }
    
    private final void stopWriteWatchdog() {
    }
    
    /**
     * Callback opcional para exibir progresso de conexão na UI.
     * Atribuído pelo MainViewModel antes de chamar connect().
     */
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> getLogSink() {
        return null;
    }
    
    /**
     * Callback opcional para exibir progresso de conexão na UI.
     * Atribuído pelo MainViewModel antes de chamar connect().
     */
    public final void setLogSink(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> p0) {
    }
    
    private final void log(java.lang.String msg) {
    }
    
    private final void sendSetupCommands() {
    }
    
    private final void processIncoming(java.lang.String text) {
    }
    
    /**
     * Parseia linha "#t+@epc|tid|ant|rssi|protect" e emite um RfidTag
     */
    private final void parseTag(java.lang.String line) {
    }
    
    /**
     * Enfileira [text] para envio via characteristic RX.
     * Strings maiores que [MTU_PAYLOAD] bytes são fragmentadas automaticamente.
     */
    private final void enqueueWrite(java.lang.String text) {
    }
    
    /**
     * Processa o próximo item da fila se nenhum write estiver em andamento.
     * Deve ser chamado depois de enfileirar ou em onCharacteristicWrite.
     */
    private final void drainWriteQueue() {
    }
    
    private final void sendCommand(java.lang.String cmd) {
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
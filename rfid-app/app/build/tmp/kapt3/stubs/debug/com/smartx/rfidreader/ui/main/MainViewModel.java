package com.smartx.rfidreader.ui.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0082\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u00104\u001a\u00020\tJ\u0006\u00105\u001a\u00020\tJ\u0006\u00106\u001a\u00020\tJ\u000e\u00107\u001a\u00020\t2\u0006\u00108\u001a\u00020\u001bJ\u0006\u00109\u001a\u00020\tJ\u0006\u0010:\u001a\u00020\tJ\u0006\u0010;\u001a\u00020\tJ\u0010\u0010<\u001a\u00020\t2\u0006\u0010=\u001a\u00020\u001bH\u0002J\b\u0010>\u001a\u00020\tH\u0014J\u0006\u0010?\u001a\u00020\tJ\u0006\u0010@\u001a\u00020\tJ\u000e\u0010A\u001a\u00020\t2\u0006\u0010B\u001a\u00020CJ\u000e\u0010D\u001a\u00020\t2\u0006\u0010E\u001a\u00020FJ\u0006\u0010G\u001a\u00020\tJ\u0015\u0010H\u001a\u00020\t2\b\u0010I\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010JJ\u0006\u0010K\u001a\u00020\tJ\u0006\u0010L\u001a\u00020\tR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00140\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00160\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\t0\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0019\u0010\"\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0#\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020\t0\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010!R\"\u0010)\u001a\u0004\u0018\u00010\u001b2\b\u0010(\u001a\u0004\u0018\u00010\u001b@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0017\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00110\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010!R\u000e\u0010.\u001a\u00020/X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00160#\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010%R\u0017\u00102\u001a\b\u0012\u0004\u0012\u00020\u00190#\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010%\u00a8\u0006M"}, d2 = {"Lcom/smartx/rfidreader/ui/main/MainViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "TAG", "", "_buzzerEvent", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "", "_displayLimit", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_lastBuzzerMs", "", "_navigateToReading", "_saveInventoryResult", "", "_tagMap", "Ljava/util/LinkedHashMap;", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "_tags", "", "_tagsDirty", "_uiState", "Lcom/smartx/rfidreader/ui/main/MainUiState;", "availableReaders", "Lcom/smartx/rfidreader/core/reader/IRfidReader;", "getAvailableReaders", "()Ljava/util/List;", "buzzerEvent", "Lkotlinx/coroutines/flow/SharedFlow;", "getBuzzerEvent", "()Lkotlinx/coroutines/flow/SharedFlow;", "displayLimit", "Lkotlinx/coroutines/flow/StateFlow;", "getDisplayLimit", "()Lkotlinx/coroutines/flow/StateFlow;", "navigateToReading", "getNavigateToReading", "<set-?>", "reader", "getReader", "()Lcom/smartx/rfidreader/core/reader/IRfidReader;", "saveInventoryResult", "getSaveInventoryResult", "settingsRepo", "Lcom/smartx/rfidreader/core/settings/AppSettingsRepository;", "tags", "getTags", "uiState", "getUiState", "autoConnectLastReader", "clearError", "clearTags", "connect", "rfidReader", "consumeConfigSaveResult", "disconnect", "loadConfig", "observeReader", "r", "onCleared", "onTriggerPressed", "onTriggerReleased", "saveAppSettings", "settings", "Lcom/smartx/rfidreader/core/settings/AppSettings;", "saveConfig", "config", "Lcom/smartx/rfidreader/core/reader/ReaderConfig;", "saveInventory", "setDisplayLimit", "limit", "(Ljava/lang/Integer;)V", "stopInventoryAndClear", "toggleInventory", "app_debug"})
public final class MainViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "MainViewModel";
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.rfidreader.core.settings.AppSettingsRepository settingsRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.smartx.rfidreader.ui.main.MainUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.ui.main.MainUiState> uiState = null;
    
    /**
     * Armazenamento rápido para lookup O(1) por EPC
     */
    @org.jetbrains.annotations.NotNull()
    private final java.util.LinkedHashMap<java.lang.String, com.smartx.rfidreader.core.reader.RfidTag> _tagMap = null;
    
    /**
     * Flag para atualizar a UI apenas quando necessário (throttle de 200 ms)
     */
    @kotlin.jvm.Volatile()
    private volatile boolean _tagsDirty = false;
    
    /**
     * Timestamp da última emissão do buzzer (throttle de 300 ms, apenas novas tags)
     */
    @kotlin.jvm.Volatile()
    private volatile long _lastBuzzerMs = 0L;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.smartx.rfidreader.core.reader.RfidTag>> _tags = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartx.rfidreader.core.reader.RfidTag>> tags = null;
    
    /**
     * Limite de tags exibidas na tela (null = todas). Padrão: 50
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _displayLimit = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> displayLimit = null;
    
    /**
     * Evento de buzzer: emitido quando nova tag é detectada e buzzer está ativado
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Unit> _buzzerEvent = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<kotlin.Unit> buzzerEvent = null;
    
    /**
     * Evento de navegação para a tela de leitura após conexão bem-sucedida
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Unit> _navigateToReading = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<kotlin.Unit> navigateToReading = null;
    
    /**
     * Resultado ao salvar inventário: true = sucesso, false = erro
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.Boolean> _saveInventoryResult = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.Boolean> saveInventoryResult = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.smartx.rfidreader.core.reader.IRfidReader> availableReaders = null;
    @org.jetbrains.annotations.Nullable()
    private com.smartx.rfidreader.core.reader.IRfidReader reader;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.ui.main.MainUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartx.rfidreader.core.reader.RfidTag>> getTags() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getDisplayLimit() {
        return null;
    }
    
    public final void setDisplayLimit(@org.jetbrains.annotations.Nullable()
    java.lang.Integer limit) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<kotlin.Unit> getBuzzerEvent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<kotlin.Unit> getNavigateToReading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.Boolean> getSaveInventoryResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.rfidreader.core.reader.IRfidReader> getAvailableReaders() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.smartx.rfidreader.core.reader.IRfidReader getReader() {
        return null;
    }
    
    /**
     * Tenta conectar automaticamente ao último leitor usado.
     * Aguarda o carregamento inicial das settings antes de decidir.
     * Não faz nada se já conectado, conectando, ou nenhum ID salvo.
     */
    public final void autoConnectLastReader() {
    }
    
    public final void connect(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.reader.IRfidReader rfidReader) {
    }
    
    public final void disconnect() {
    }
    
    private final void observeReader(com.smartx.rfidreader.core.reader.IRfidReader r) {
    }
    
    public final void clearError() {
    }
    
    public final void toggleInventory() {
    }
    
    public final void clearTags() {
    }
    
    public final void stopInventoryAndClear() {
    }
    
    public final void onTriggerPressed() {
    }
    
    public final void onTriggerReleased() {
    }
    
    public final void loadConfig() {
    }
    
    public final void saveConfig(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.reader.ReaderConfig config) {
    }
    
    public final void consumeConfigSaveResult() {
    }
    
    public final void saveInventory() {
    }
    
    public final void saveAppSettings(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.settings.AppSettings settings) {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}
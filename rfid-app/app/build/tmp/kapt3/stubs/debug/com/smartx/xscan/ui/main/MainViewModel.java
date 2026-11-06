package com.smartx.xscan.ui.main;

/**
 * Ponte entre a UI e o [ReaderManager] da biblioteca smartx-rfid.
 * A conexão, o inventário e as tags vivem no manager (compartilhado por todo o app);
 * aqui ficam apenas as regras de tela: configurações do app, buzzer e navegação.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u009a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010=\u001a\u00020\tJ\u0006\u0010>\u001a\u00020\tJ\u0006\u0010?\u001a\u00020\tJ\u0006\u0010@\u001a\u00020\tJ\u0018\u0010A\u001a\u00020\t2\u0006\u0010B\u001a\u00020\u00162\b\b\u0002\u0010C\u001a\u00020DJ\u0006\u0010E\u001a\u00020\tJ\u0006\u0010F\u001a\u00020\tJ\u0006\u0010G\u001a\u00020\tJ\u0006\u0010H\u001a\u00020\tJ\u0006\u0010I\u001a\u00020\tJ\u0006\u0010J\u001a\u00020\tJ\u0006\u0010K\u001a\u00020\tJ \u0010L\u001a\u00020\t2\u0006\u0010M\u001a\u00020N2\u0010\b\u0002\u0010O\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010PJ\u000e\u0010Q\u001a\u00020\t2\u0006\u0010R\u001a\u00020SJ\u000e\u0010T\u001a\u00020\t2\u0006\u0010U\u001a\u00020\u0006J\u0015\u0010V\u001a\u00020\t2\b\u0010W\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010XJ\u0006\u0010Y\u001a\u00020\tJ\u0006\u0010Z\u001a\u00020\tJ\u0006\u0010[\u001a\u00020\tR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\t0\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00150 \u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0019\u0010#\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0 \u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\"R\u000e\u0010%\u001a\u00020&X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\'\u001a\u00020(X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010)\u001a\b\u0012\u0004\u0012\u00020\t0\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001cR\u0013\u0010+\u001a\u0004\u0018\u00010\u00168F\u00a2\u0006\u0006\u001a\u0004\b,\u0010-R\u0017\u0010.\u001a\b\u0012\u0004\u0012\u00020\u000f0\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001cR\u000e\u00100\u001a\u000201X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u00102\u001a\b\u0012\u0004\u0012\u00020\u00060\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\u001cR\u0017\u00104\u001a\b\u0012\u0004\u0012\u00020\t0\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010\u001cR\u0017\u00106\u001a\b\u0012\u0004\u0012\u0002070\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010\u001cR\u001d\u00109\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002070\u00150 \u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010\"R\u0017\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00130 \u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010\"\u00a8\u0006\\"}, d2 = {"Lcom/smartx/xscan/ui/main/MainViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "TAG", "", "_buzzerEvent", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "", "_displayLimit", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_navigateToReading", "_saveInventoryResult", "", "_showBleScanDialog", "_showConnectionLog", "_uiState", "Lcom/smartx/xscan/ui/main/MainUiState;", "availableReaders", "", "Lcom/smartx/rfid/core/IRfidReader;", "getAvailableReaders", "()Ljava/util/List;", "buzzerEvent", "Lkotlinx/coroutines/flow/SharedFlow;", "getBuzzerEvent", "()Lkotlinx/coroutines/flow/SharedFlow;", "connectJob", "Lkotlinx/coroutines/Job;", "connectionLog", "Lkotlinx/coroutines/flow/StateFlow;", "getConnectionLog", "()Lkotlinx/coroutines/flow/StateFlow;", "displayLimit", "getDisplayLimit", "lastBuzzerMs", "", "manager", "Lcom/smartx/rfid/manager/ReaderManager;", "navigateToReading", "getNavigateToReading", "reader", "getReader", "()Lcom/smartx/rfid/core/IRfidReader;", "saveInventoryResult", "getSaveInventoryResult", "settingsRepo", "Lcom/smartx/xscan/core/settings/AppSettingsRepository;", "showBleScanDialog", "getShowBleScanDialog", "showConnectionLog", "getShowConnectionLog", "tagEventFlow", "Lcom/smartx/rfid/core/RfidTag;", "getTagEventFlow", "tags", "getTags", "uiState", "getUiState", "autoConnectLastReader", "cancelConnection", "clearError", "clearTags", "connect", "rfidReader", "request", "Lcom/smartx/rfid/core/ConnectionRequest;", "consumeConfigSaveResult", "disconnect", "enterRadarMode", "exitRadarMode", "loadConfig", "onTriggerPressed", "onTriggerReleased", "saveAppSettings", "settings", "Lcom/smartx/xscan/core/settings/AppSettings;", "onSaved", "Lkotlin/Function0;", "saveConfig", "config", "Lcom/smartx/rfid/core/ReaderConfig;", "saveInventory", "inventoryName", "setDisplayLimit", "limit", "(Ljava/lang/Integer;)V", "stopInventory", "stopInventoryAndClear", "toggleInventory", "app_debug"})
public final class MainViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "MainViewModel";
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.rfid.manager.ReaderManager manager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.xscan.core.settings.AppSettingsRepository settingsRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.smartx.xscan.ui.main.MainUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.smartx.xscan.ui.main.MainUiState> uiState = null;
    
    /**
     * Tags coletadas (já filtradas e deduplicadas pela biblioteca; atualizadas a cada ~200 ms).
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartx.rfid.core.RfidTag>> tags = null;
    
    /**
     * Cada leitura individual que passou pelos filtros (usado pelo radar).
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<com.smartx.rfid.core.RfidTag> tagEventFlow = null;
    
    /**
     * Log de conexão exibido no ConnectionLogDialogFragment.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> connectionLog = null;
    
    /**
     * Leitores habilitados (config.json -> readers).
     */
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.smartx.rfid.core.IRfidReader> availableReaders = null;
    
    /**
     * Limite de tags exibidas na tela (null = todas). Padrão: 50
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _displayLimit = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> displayLimit = null;
    
    /**
     * Evento de buzzer: emitido a cada tag lida (cooldown de 100 ms)
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Unit> _buzzerEvent = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<kotlin.Unit> buzzerEvent = null;
    @kotlin.jvm.Volatile()
    private volatile long lastBuzzerMs = 0L;
    
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
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.String> _showBleScanDialog = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.String> showBleScanDialog = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Unit> _showConnectionLog = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<kotlin.Unit> showConnectionLog = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job connectJob;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.smartx.xscan.ui.main.MainUiState> getUiState() {
        return null;
    }
    
    /**
     * Tags coletadas (já filtradas e deduplicadas pela biblioteca; atualizadas a cada ~200 ms).
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartx.rfid.core.RfidTag>> getTags() {
        return null;
    }
    
    /**
     * Cada leitura individual que passou pelos filtros (usado pelo radar).
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<com.smartx.rfid.core.RfidTag> getTagEventFlow() {
        return null;
    }
    
    /**
     * Log de conexão exibido no ConnectionLogDialogFragment.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> getConnectionLog() {
        return null;
    }
    
    /**
     * Leitores habilitados (config.json -> readers).
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.rfid.core.IRfidReader> getAvailableReaders() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.smartx.rfid.core.IRfidReader getReader() {
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
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.String> getShowBleScanDialog() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<kotlin.Unit> getShowConnectionLog() {
        return null;
    }
    
    /**
     * Tenta conectar automaticamente ao último leitor usado.
     * Não faz nada se já conectado/conectando, se não há ID salvo, ou se o leitor
     * exige escolha explícita do usuário (`capabilities.supportsAutoReconnect = false`).
     */
    public final void autoConnectLastReader() {
    }
    
    public final void cancelConnection() {
    }
    
    public final void connect(@org.jetbrains.annotations.NotNull()
    com.smartx.rfid.core.IRfidReader rfidReader, @org.jetbrains.annotations.NotNull()
    com.smartx.rfid.core.ConnectionRequest request) {
    }
    
    public final void disconnect() {
    }
    
    public final void clearError() {
    }
    
    public final void toggleInventory() {
    }
    
    public final void clearTags() {
    }
    
    public final void stopInventory() {
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
    com.smartx.rfid.core.ReaderConfig config) {
    }
    
    public final void consumeConfigSaveResult() {
    }
    
    /**
     * Aplica Session 0 ao entrar no modo radar (sem alterar a config salva no estado).
     * Session 0 garante que todas as tags respondam sequencialmente na mesma sessão,
     * ideal para localização/radar.
     */
    public final void enterRadarMode() {
    }
    
    /**
     * Restaura a session original configurada pelo usuário ao sair do modo radar.
     */
    public final void exitRadarMode() {
    }
    
    public final void saveInventory(@org.jetbrains.annotations.NotNull()
    java.lang.String inventoryName) {
    }
    
    public final void saveAppSettings(@org.jetbrains.annotations.NotNull()
    com.smartx.xscan.core.settings.AppSettings settings, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSaved) {
    }
}
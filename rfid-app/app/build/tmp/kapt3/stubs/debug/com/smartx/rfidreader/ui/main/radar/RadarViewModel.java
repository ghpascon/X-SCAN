package com.smartx.rfidreader.ui.main.radar;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0013J\u0014\u0010\u0019\u001a\u00020\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00130\tJ\u0006\u0010\u001c\u001a\u00020\u001aJ\b\u0010\u001d\u001a\u00020\u001aH\u0014J\u0006\u0010\u001e\u001a\u00020\u001aJ\u000e\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020!J\u0017\u0010\"\u001a\u0004\u0018\u00010\u00072\u0006\u0010#\u001a\u00020\u0013H\u0002\u00a2\u0006\u0002\u0010$J\u000e\u0010%\u001a\u00020\u001a2\u0006\u0010\u0018\u001a\u00020\u0013J\u0006\u0010&\u001a\u00020\u001aJ\u0006\u0010\'\u001a\u00020\u001aR\u0016\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\n0\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000e\u00a8\u0006("}, d2 = {"Lcom/smartx/rfidreader/ui/main/radar/RadarViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_activeBestRssi", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_targets", "", "Lcom/smartx/rfidreader/ui/main/radar/RadarTarget;", "activeBestRssi", "Lkotlinx/coroutines/flow/StateFlow;", "getActiveBestRssi", "()Lkotlinx/coroutines/flow/StateFlow;", "buzzerJob", "Lkotlinx/coroutines/Job;", "targetMap", "Ljava/util/LinkedHashMap;", "", "targets", "getTargets", "addTarget", "", "epc", "addTargets", "", "epcs", "clearTargets", "onCleared", "onScanStarted", "onSingleTagRead", "tag", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "parseRssi", "raw", "(Ljava/lang/String;)Ljava/lang/Double;", "removeTarget", "startBuzzerLoop", "stopBuzzerLoop", "app_debug"})
public final class RadarViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.smartx.rfidreader.ui.main.radar.RadarTarget>> _targets = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartx.rfidreader.ui.main.radar.RadarTarget>> targets = null;
    
    /**
     * Melhor RSSI de qualquer target lida nos últimos 500ms.
     * null = nenhuma tag ativa → buzzer deve parar.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Double> _activeBestRssi = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Double> activeBestRssi = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.LinkedHashMap<java.lang.String, com.smartx.rfidreader.ui.main.radar.RadarTarget> targetMap = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job buzzerJob;
    
    public RadarViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartx.rfidreader.ui.main.radar.RadarTarget>> getTargets() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Double> getActiveBestRssi() {
        return null;
    }
    
    /**
     * Adiciona um EPC à lista de targets. Retorna false se já existir ou for inválido.
     */
    public final boolean addTarget(@org.jetbrains.annotations.NotNull()
    java.lang.String epc) {
        return false;
    }
    
    /**
     * Adiciona múltiplos EPCs (ignora duplicatas silenciosamente).
     */
    public final void addTargets(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> epcs) {
    }
    
    /**
     * Remove um EPC da lista de targets.
     */
    public final void removeTarget(@org.jetbrains.annotations.NotNull()
    java.lang.String epc) {
    }
    
    /**
     * Remove todos os targets.
     */
    public final void clearTargets() {
    }
    
    /**
     * Parseia o campo rssi (String) do hardware de forma robusta.
     * Lida com vírgula decimal (locale pt-BR), sufixo " dBm" e espaços.
     */
    private final java.lang.Double parseRssi(java.lang.String raw) {
        return null;
    }
    
    /**
     * Recebe um evento individual do tagFlow (leitura fresca do hardware).
     * Atualiza RSSI, lastSeenMs e marca tag como detectada na sessão atual.
     */
    public final void onSingleTagRead(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.reader.RfidTag tag) {
    }
    
    /**
     * Limpa o estado de "detectada" de todos os targets.
     * Deve ser chamado quando uma nova sessão de leitura se inicia.
     */
    public final void onScanStarted() {
    }
    
    /**
     * Inicia o loop de buzzer radar. Deve ser chamado em onStart do Fragment.
     * O intervalo entre bipes diminui quanto mais forte for o sinal da melhor target visível.
     *
     * Mapeamento (linear):
     * RSSI ≥ -30 dBm → 120 ms entre bipes (muito próximo)
     * RSSI = -60 dBm → ~580 ms
     * RSSI ≤ -90 dBm → 2500 ms (muito longe — apenas confirma que está no campo)
     */
    public final void startBuzzerLoop() {
    }
    
    /**
     * Para o loop de buzzer radar. Deve ser chamado em onStop do Fragment.
     */
    public final void stopBuzzerLoop() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}
package com.smartx.rfidreader.core.reader;

/**
 * Contrato unificado que todos os leitores RFID devem implementar.
 *
 * Ao adicionar suporte a um novo leitor:
 * 1. Crie uma classe em readers/<modelo>/ implementando esta interface.
 * 2. Registre-a em ReaderRegistry.
 * Pronto!
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\b\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0017H\u00a6@\u00a2\u0006\u0002\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\f2\u0006\u0010\u001a\u001a\u00020\u001bH\u00a6@\u00a2\u0006\u0002\u0010\u001cJ\u000e\u0010\u001d\u001a\u00020\u001eH\u00a6@\u00a2\u0006\u0002\u0010\u001fJ\b\u0010 \u001a\u00020\fH&J\b\u0010!\u001a\u00020\fH&J\b\u0010\"\u001a\u00020\fH&J\u000e\u0010#\u001a\u00020\u0017H\u00a6@\u00a2\u0006\u0002\u0010\u001fJ\b\u0010$\u001a\u00020\fH&J\b\u0010%\u001a\u00020\fH&R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u00020\bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\rR\u0012\u0010\u000e\u001a\u00020\bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\nR\u0018\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006&"}, d2 = {"Lcom/smartx/rfidreader/core/reader/IRfidReader;", "", "connectionState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/smartx/rfidreader/core/reader/ReaderConnectionState;", "getConnectionState", "()Lkotlinx/coroutines/flow/StateFlow;", "displayName", "", "getDisplayName", "()Ljava/lang/String;", "isBle", "", "()Z", "readerId", "getReaderId", "tagFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "getTagFlow", "()Lkotlinx/coroutines/flow/Flow;", "applyConfig", "config", "Lcom/smartx/rfidreader/core/reader/ReaderConfig;", "(Lcom/smartx/rfidreader/core/reader/ReaderConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "connect", "context", "Landroid/content/Context;", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isInventorying", "onTriggerPressed", "onTriggerReleased", "readConfig", "startInventory", "stopInventory", "app_debug"})
public abstract interface IRfidReader {
    
    /**
     * Identificador único do modelo (ex.: "C72", "IH25")
     */
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getReaderId();
    
    /**
     * Nome legível para exibição (ex.: "Chainway C72")
     */
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getDisplayName();
    
    public abstract boolean isBle();
    
    /**
     * Estado atual da conexão como StateFlow observável
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.core.reader.ReaderConnectionState> getConnectionState();
    
    /**
     * Stream reativo de tags lidas durante o inventário
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.smartx.rfidreader.core.reader.RfidTag> getTagFlow();
    
    /**
     * Inicializa e conecta ao leitor.
     * @param context contexto Android
     * @return true se a conexão foi bem-sucedida
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object connect(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    /**
     * Desconecta e libera recursos
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object disconnect(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Inicia inventário contínuo; as tags chegam via [tagFlow]
     */
    public abstract boolean startInventory();
    
    /**
     * Para o inventário
     */
    public abstract boolean stopInventory();
    
    /**
     * Retorna true se o inventário está em execução
     */
    public abstract boolean isInventorying();
    
    /**
     * Aplica um bloco de configurações ao leitor
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object applyConfig(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.reader.ReaderConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    /**
     * Lê a configuração atual do leitor
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object readConfig(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.smartx.rfidreader.core.reader.ReaderConfig> $completion);
    
    /**
     * Callback chamado quando o gatilho físico é pressionado.
     * Retorna true se o evento foi tratado.
     */
    public abstract boolean onTriggerPressed();
    
    /**
     * Callback chamado quando o gatilho físico é liberado.
     * Retorna true se o evento foi tratado.
     */
    public abstract boolean onTriggerReleased();
    
    /**
     * Contrato unificado que todos os leitores RFID devem implementar.
     *
     * Ao adicionar suporte a um novo leitor:
     * 1. Crie uma classe em readers/<modelo>/ implementando esta interface.
     * 2. Registre-a em ReaderRegistry.
     * Pronto!
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        public static boolean isBle(@org.jetbrains.annotations.NotNull()
        com.smartx.rfidreader.core.reader.IRfidReader $this) {
            return false;
        }
    }
}
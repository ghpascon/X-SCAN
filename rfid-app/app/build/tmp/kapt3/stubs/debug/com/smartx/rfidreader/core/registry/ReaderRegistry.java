package com.smartx.rfidreader.core.registry;

/**
 * Registro central de todos os leitores RFID suportados.
 *
 * Para adicionar um novo leitor:
 *  1. Implemente [IRfidReader] para o modelo
 *  2. Adicione a instância a [availableReaders]
 *  Pronto — a UI e o restante do app detectam automaticamente.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\n\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\fR!\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\r"}, d2 = {"Lcom/smartx/rfidreader/core/registry/ReaderRegistry;", "", "()V", "availableReaders", "", "Lcom/smartx/rfidreader/core/reader/IRfidReader;", "getAvailableReaders", "()Ljava/util/List;", "availableReaders$delegate", "Lkotlin/Lazy;", "findById", "readerId", "", "app_debug"})
public final class ReaderRegistry {
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.Lazy availableReaders$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.rfidreader.core.registry.ReaderRegistry INSTANCE = null;
    
    private ReaderRegistry() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.rfidreader.core.reader.IRfidReader> getAvailableReaders() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.smartx.rfidreader.core.reader.IRfidReader findById(@org.jetbrains.annotations.NotNull()
    java.lang.String readerId) {
        return null;
    }
}
package com.smartx.xscan.ui.main.reader;

/**
 * Fluxo genérico de conexão, guiado por `IRfidReader.connectionOptions`:
 * 1. se o leitor oferece mais de uma forma de conexão, pergunta qual usar;
 * 2. se a opção exige dispositivo (ex.: Bluetooth), abre o scan BLE;
 * 3. conecta e mostra o log.
 *
 * Nenhum modelo de leitor é citado aqui — novos leitores da biblioteca funcionam sem alterações.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J(\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J(\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ\u001e\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n\u00a8\u0006\u0011"}, d2 = {"Lcom/smartx/xscan/ui/main/reader/ReaderConnectFlow;", "", "()V", "connect", "", "fragment", "Landroidx/fragment/app/Fragment;", "reader", "Lcom/smartx/rfid/core/IRfidReader;", "viewModel", "Lcom/smartx/xscan/ui/main/MainViewModel;", "request", "Lcom/smartx/rfid/core/ConnectionRequest;", "connectWith", "option", "Lcom/smartx/rfid/core/ConnectionOption;", "start", "app_debug"})
public final class ReaderConnectFlow {
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.xscan.ui.main.reader.ReaderConnectFlow INSTANCE = null;
    
    private ReaderConnectFlow() {
        super();
    }
    
    public final void start(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.Fragment fragment, @org.jetbrains.annotations.NotNull()
    com.smartx.rfid.core.IRfidReader reader, @org.jetbrains.annotations.NotNull()
    com.smartx.xscan.ui.main.MainViewModel viewModel) {
    }
    
    public final void connectWith(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.Fragment fragment, @org.jetbrains.annotations.NotNull()
    com.smartx.rfid.core.IRfidReader reader, @org.jetbrains.annotations.NotNull()
    com.smartx.xscan.ui.main.MainViewModel viewModel, @org.jetbrains.annotations.Nullable()
    com.smartx.rfid.core.ConnectionOption option) {
    }
    
    private final void connect(androidx.fragment.app.Fragment fragment, com.smartx.rfid.core.IRfidReader reader, com.smartx.xscan.ui.main.MainViewModel viewModel, com.smartx.rfid.core.ConnectionRequest request) {
    }
}
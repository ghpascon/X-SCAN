package com.smartx.rfidreader.ui.base;

/**
 * Activity base que fornece automaticamente:
 * - header_app (nome do leitor + status de conexão) em TODAS as telas
 * - footer_app (device ID) em TODAS as telas
 *
 * O header é atualizado automaticamente via [ActiveReaderState] — não é
 * necessário chamar nada nas subclasses. Qualquer nova Activity que estenda
 * BaseActivity exibirá o estado real do leitor sem código adicional.
 *
 * Como usar ao criar uma nova Activity:
 * 1. Estenda `BaseActivity<SuaActivityBinding>()`
 * 2. Implemente `inflateBinding` retornando `SuaActivityBinding.inflate(inflater)`
 * 3. Implemente `onActivityReady` com o código que antes ficava em `onCreate`
 * 4. Use `binding` para acessar as views do seu layout
 * 5. Use `headerBinding.headerLogo` etc. para acesso direto ao header se necessário
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\u0015\u0010\u0011\u001a\u00028\u00002\u0006\u0010\u0012\u001a\u00020\u0013H$\u00a2\u0006\u0002\u0010\u0014J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\u0012\u0010\u0017\u001a\u00020\u00162\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014J\u0012\u0010\u001a\u001a\u00020\u00162\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0004J\"\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001d2\b\b\u0001\u0010\u001f\u001a\u00020 H\u0004R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u00028\u0000X\u0084.\u00a2\u0006\u0010\n\u0002\u0010\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0014\u0010\r\u001a\u00020\u000e8DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006!"}, d2 = {"Lcom/smartx/rfidreader/ui/base/BaseActivity;", "VB", "Landroidx/viewbinding/ViewBinding;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "_baseBinding", "Lcom/smartx/rfidreader/databinding/ActivityBaseBinding;", "binding", "getBinding", "()Landroidx/viewbinding/ViewBinding;", "setBinding", "(Landroidx/viewbinding/ViewBinding;)V", "Landroidx/viewbinding/ViewBinding;", "headerBinding", "Lcom/smartx/rfidreader/databinding/HeaderAppBinding;", "getHeaderBinding", "()Lcom/smartx/rfidreader/databinding/HeaderAppBinding;", "inflateBinding", "inflater", "Landroid/view/LayoutInflater;", "(Landroid/view/LayoutInflater;)Landroidx/viewbinding/ViewBinding;", "observeReaderState", "", "onActivityReady", "savedInstanceState", "Landroid/os/Bundle;", "onCreate", "updateHeader", "readerName", "", "statusText", "statusDotRes", "", "app_debug"})
public abstract class BaseActivity<VB extends androidx.viewbinding.ViewBinding> extends androidx.appcompat.app.AppCompatActivity {
    private com.smartx.rfidreader.databinding.ActivityBaseBinding _baseBinding;
    
    /**
     * Binding do conteúdo específico da tela — disponível em onActivityReady
     */
    protected VB binding;
    
    public BaseActivity() {
        super();
    }
    
    /**
     * Binding do conteúdo específico da tela — disponível em onActivityReady
     */
    @org.jetbrains.annotations.NotNull()
    protected final VB getBinding() {
        return null;
    }
    
    /**
     * Binding do conteúdo específico da tela — disponível em onActivityReady
     */
    protected final void setBinding(@org.jetbrains.annotations.NotNull()
    VB p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final com.smartx.rfidreader.databinding.HeaderAppBinding getHeaderBinding() {
        return null;
    }
    
    @java.lang.Override()
    protected final void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void observeReaderState() {
    }
    
    /**
     * Retorna o ViewBinding do layout específico desta tela.
     * Ex: `override fun inflateBinding(inflater: LayoutInflater) = ActivitySyncBinding.inflate(inflater)`
     */
    @org.jetbrains.annotations.NotNull()
    protected abstract VB inflateBinding(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater);
    
    /**
     * Substitui onCreate — o binding já está disponível quando este método é chamado.
     */
    protected void onActivityReady(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * Sobrescreve o header manualmente para casos especiais (ex: forçar texto fixo).
     * Na maioria dos casos não é necessário — o header atualiza sozinho via ActiveReaderState.
     */
    protected final void updateHeader(@org.jetbrains.annotations.NotNull()
    java.lang.String readerName, @org.jetbrains.annotations.NotNull()
    java.lang.String statusText, @androidx.annotation.DrawableRes()
    int statusDotRes) {
    }
}
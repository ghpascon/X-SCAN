package com.smartx.rfidreader.core.webhook;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006J\u0006\u0010\u0012\u001a\u00020\u0010J\u000e\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\bR\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\f\u00a8\u0006\u0015"}, d2 = {"Lcom/smartx/rfidreader/core/webhook/WebhookStatusStore;", "", "()V", "_history", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/smartx/rfidreader/core/webhook/WebhookSendStatus;", "_sending", "", "history", "Lkotlinx/coroutines/flow/StateFlow;", "getHistory", "()Lkotlinx/coroutines/flow/StateFlow;", "sending", "getSending", "add", "", "status", "clear", "setSending", "value", "app_debug"})
public final class WebhookStatusStore {
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _sending = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> sending = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.smartx.rfidreader.core.webhook.WebhookSendStatus>> _history = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartx.rfidreader.core.webhook.WebhookSendStatus>> history = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.rfidreader.core.webhook.WebhookStatusStore INSTANCE = null;
    
    private WebhookStatusStore() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getSending() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.smartx.rfidreader.core.webhook.WebhookSendStatus>> getHistory() {
        return null;
    }
    
    public final void setSending(boolean value) {
    }
    
    public final void add(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.webhook.WebhookSendStatus status) {
    }
    
    public final void clear() {
    }
}
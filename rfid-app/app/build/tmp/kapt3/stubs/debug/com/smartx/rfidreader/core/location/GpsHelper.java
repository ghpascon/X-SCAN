package com.smartx.rfidreader.core.location;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\t\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nH\u0082@\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nH\u0086@\u00a2\u0006\u0002\u0010\fJ\b\u0010\u000e\u001a\u00020\u000fH\u0002J\u001c\u0010\u0010\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nH\u0082@\u00a2\u0006\u0002\u0010\fR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/smartx/rfidreader/core/location/GpsHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "TAG", "", "fusedClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "getLastKnownLocation", "Lkotlin/Pair;", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLocation", "hasPermission", "", "requestSingleUpdate", "app_debug"})
public final class GpsHelper {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "GpsHelper";
    @org.jetbrains.annotations.NotNull()
    private final com.google.android.gms.location.FusedLocationProviderClient fusedClient = null;
    
    public GpsHelper(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final boolean hasPermission() {
        return false;
    }
    
    /**
     * Tenta obter a última localização conhecida.
     * Em seguida, se nula ou antiga, solicita uma atualização rápida.
     * Timeout total de 4 segundos.
     * Retorna null se permissão negada ou falhar.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLocation(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Double, java.lang.Double>> $completion) {
        return null;
    }
    
    private final java.lang.Object getLastKnownLocation(kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Double, java.lang.Double>> $completion) {
        return null;
    }
    
    private final java.lang.Object requestSingleUpdate(kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Double, java.lang.Double>> $completion) {
        return null;
    }
}
# Regras ProGuard para o app RFID Reader
# Manter classes dos SDKs de hardware
-keep class com.rscja.** { *; }
-keep class com.honeywell.** { *; }
-keep class com.atid.** { *; }

# Manter classes do app
-keep class com.smartx.rfidreader.** { *; }

# Coroutines
-keepclassmembers class kotlinx.coroutines.internal.MainDispatcherFactory { *; }

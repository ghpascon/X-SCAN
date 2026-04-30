# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#urovo标准SDK混淆规则
-dontwarn com.lib.**
-keep class com.lib.**{*;}
-dontwarn android.device.**
-keep class android.device.**{*;}
-dontwarn android.content.**
-keep class android.content.**{*;}
-dontwarn android.os.**
-keep class android.os.**{*;}
#urovoRFIDSDK混淆规则
-dontwarn com.ubx.usdk.**
-keep class com.ubx.usdk.**{*;}
-dontwarn  com.iflytek.**
-keep class  com.iflytek.**{*;}
-dontwarn com.rfid.**
-keep class com.rfid.**{*;}
-dontwarn com.android.hw.**
-keep class com.android.hw.**{*;}
-dontwarn com.urovo.**
-keep class com.urovo.**{*;}


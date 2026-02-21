# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in SDK_ROOT/tools/proguard/proguard-android.txt
# Keep Room and data classes
-keep class com.yourapp.budgetmanager.data.local.entity.** { *; }
-keep class com.yourapp.budgetmanager.domain.model.** { *; }

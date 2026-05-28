# Add project specific ProGuard rules here.
# You can control the set of applied rules through the proguardFiles
# in the build.gradle.kts file.

# SQLCipher rules to prevent cryptographic code obfuscation
-keep class net.zetetic.database.sqlcipher.** { *; }
-keep class net.zetetic.database.** { *; }
-dontwarn net.zetetic.database.sqlcipher.**

# Room database rules
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.limits.**

# Keep models intact for reflection
-keepclassmembers class * {
    @androidx.room.PrimaryKey *;
    @androidx.room.ColumnInfo *;
    @androidx.room.Entity *;
}

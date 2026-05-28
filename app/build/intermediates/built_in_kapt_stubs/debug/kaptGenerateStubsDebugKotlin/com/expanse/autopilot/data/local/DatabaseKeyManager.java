package com.expanse.autopilot.data.local;

@kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\u0006\u001a\u00020\u0007J\b\u0010\b\u001a\u00020\u0007H\u0002J\b\u0010\t\u001a\u00020\nH\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0007H\u0002J\u0018\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0007H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/expanse/autopilot/data/local/DatabaseKeyManager;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "getOrCreatePassphrase", "", "generateRandomKey", "getKeystoreSecretKey", "Ljavax/crypto/SecretKey;", "encryptAndSaveKey", "", "key", "decryptKey", "encryptedKey", "iv", "Companion", "app"})
public final class DatabaseKeyManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ALIAS = "ExpanseSqlcipherKeyAlias";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "ExpansePrefsSecure";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DB_KEY_PREF = "encrypted_db_key";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DB_IV_PREF = "encrypted_db_iv";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEYSTORE_PROVIDER = "AndroidKeyStore";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String AES_MODE = "AES/GCM/NoPadding";
    @org.jetbrains.annotations.NotNull()
    public static final com.expanse.autopilot.data.local.DatabaseKeyManager.Companion Companion = null;
    
    public DatabaseKeyManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final byte[] getOrCreatePassphrase() {
        return null;
    }
    
    private final byte[] generateRandomKey() {
        return null;
    }
    
    private final javax.crypto.SecretKey getKeystoreSecretKey() {
        return null;
    }
    
    private final void encryptAndSaveKey(byte[] key) {
    }
    
    private final byte[] decryptKey(byte[] encryptedKey, byte[] iv) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/expanse/autopilot/data/local/DatabaseKeyManager$Companion;", "", "<init>", "()V", "KEY_ALIAS", "", "PREFS_NAME", "DB_KEY_PREF", "DB_IV_PREF", "KEYSTORE_PROVIDER", "AES_MODE", "app"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
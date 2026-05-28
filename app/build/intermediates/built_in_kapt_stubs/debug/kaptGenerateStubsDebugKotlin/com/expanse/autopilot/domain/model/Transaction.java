package com.expanse.autopilot.domain.model;

@kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0019\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BA\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\u0003\u0012\u0006\u0010\r\u001a\u00020\u000e\u00a2\u0006\u0004\b\u000f\u0010\u0010J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0007H\u00c6\u0003J\t\u0010 \u001a\u00020\tH\u00c6\u0003J\t\u0010!\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\u000eH\u00c6\u0003JO\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u000eH\u00c6\u0001J\u0014\u0010%\u001a\u00020\u000e2\b\u0010&\u001a\u0004\u0018\u00010\u0001H\u00d6\u0083\u0004J\n\u0010\'\u001a\u00020(H\u00d6\u0081\u0004J\n\u0010)\u001a\u00020\u000bH\u00d6\u0081\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0012R\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u001c\u00a8\u0006*"}, d2 = {"Lcom/expanse/autopilot/domain/model/Transaction;", "", "id", "", "amount", "", "type", "Lcom/expanse/autopilot/domain/model/TransactionType;", "category", "Lcom/expanse/autopilot/domain/model/BudgetCategory;", "description", "", "timestamp", "isAutoScraped", "", "<init>", "(JDLcom/expanse/autopilot/domain/model/TransactionType;Lcom/expanse/autopilot/domain/model/BudgetCategory;Ljava/lang/String;JZ)V", "getId", "()J", "getAmount", "()D", "getType", "()Lcom/expanse/autopilot/domain/model/TransactionType;", "getCategory", "()Lcom/expanse/autopilot/domain/model/BudgetCategory;", "getDescription", "()Ljava/lang/String;", "getTimestamp", "()Z", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app"})
public final class Transaction {
    private final long id = 0L;
    private final double amount = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final com.expanse.autopilot.domain.model.TransactionType type = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expanse.autopilot.domain.model.BudgetCategory category = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String description = null;
    private final long timestamp = 0L;
    private final boolean isAutoScraped = false;
    
    public Transaction(long id, double amount, @org.jetbrains.annotations.NotNull()
    com.expanse.autopilot.domain.model.TransactionType type, @org.jetbrains.annotations.NotNull()
    com.expanse.autopilot.domain.model.BudgetCategory category, @org.jetbrains.annotations.NotNull()
    java.lang.String description, long timestamp, boolean isAutoScraped) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final double getAmount() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expanse.autopilot.domain.model.TransactionType getType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expanse.autopilot.domain.model.BudgetCategory getCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDescription() {
        return null;
    }
    
    public final long getTimestamp() {
        return 0L;
    }
    
    public final boolean isAutoScraped() {
        return false;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expanse.autopilot.domain.model.TransactionType component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expanse.autopilot.domain.model.BudgetCategory component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    public final long component6() {
        return 0L;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expanse.autopilot.domain.model.Transaction copy(long id, double amount, @org.jetbrains.annotations.NotNull()
    com.expanse.autopilot.domain.model.TransactionType type, @org.jetbrains.annotations.NotNull()
    com.expanse.autopilot.domain.model.BudgetCategory category, @org.jetbrains.annotations.NotNull()
    java.lang.String description, long timestamp, boolean isAutoScraped) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}
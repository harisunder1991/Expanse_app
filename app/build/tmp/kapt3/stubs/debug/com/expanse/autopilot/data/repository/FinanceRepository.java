package com.expanse.autopilot.data.repository;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0012\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00140\u0013J\u0012\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00140\u0013J\u0012\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\u00140\u0013J6\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020#H\u0086@\u00a2\u0006\u0002\u0010$J&\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u001d2\u0006\u0010)\u001a\u00020*H\u0086@\u00a2\u0006\u0002\u0010+J\u000e\u0010,\u001a\u00020\u001bH\u0086@\u00a2\u0006\u0002\u0010-R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2 = {"Lcom/expanse/autopilot/data/repository/FinanceRepository;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "db", "Lcom/expanse/autopilot/data/local/AppDatabase;", "transactionDao", "Lcom/expanse/autopilot/data/local/dao/TransactionDao;", "budgetDao", "Lcom/expanse/autopilot/data/local/dao/BudgetDao;", "savingsGoalDao", "Lcom/expanse/autopilot/data/local/dao/SavingsGoalDao;", "budgetEngine", "Lcom/expanse/autopilot/domain/engine/BudgetEngine;", "sweepEngine", "Lcom/expanse/autopilot/domain/engine/SweepEngine;", "getAllTransactions", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/expanse/autopilot/data/local/entity/TransactionEntity;", "getAllBudgets", "Lcom/expanse/autopilot/data/local/entity/BudgetCategoryEntity;", "getActiveGoals", "Lcom/expanse/autopilot/data/local/entity/SavingsGoalEntity;", "addTransaction", "", "amount", "", "type", "", "category", "description", "isAutoScraped", "", "(DLjava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createSavingsGoal", "", "name", "target", "durationMonths", "", "(Ljava/lang/String;DILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearAllData", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class FinanceRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.expanse.autopilot.data.local.AppDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expanse.autopilot.data.local.dao.TransactionDao transactionDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expanse.autopilot.data.local.dao.BudgetDao budgetDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expanse.autopilot.data.local.dao.SavingsGoalDao savingsGoalDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expanse.autopilot.domain.engine.BudgetEngine budgetEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final com.expanse.autopilot.domain.engine.SweepEngine sweepEngine = null;
    
    public FinanceRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.expanse.autopilot.data.local.entity.TransactionEntity>> getAllTransactions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.expanse.autopilot.data.local.entity.BudgetCategoryEntity>> getAllBudgets() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.expanse.autopilot.data.local.entity.SavingsGoalEntity>> getActiveGoals() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addTransaction(double amount, @org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.String category, @org.jetbrains.annotations.NotNull()
    java.lang.String description, boolean isAutoScraped, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createSavingsGoal(@org.jetbrains.annotations.NotNull()
    java.lang.String name, double target, int durationMonths, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clearAllData(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}
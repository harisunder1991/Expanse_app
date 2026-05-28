package com.expanse.autopilot.data.local.dao;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u0006\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\rJ\u001c\u0010\u000e\u001a\u00020\u000b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u0010J\u001e\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001e\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u000e\u0010\u0016\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u000e\u0010\u0018\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0017\u00a8\u0006\u0019\u00c0\u0006\u0003"}, d2 = {"Lcom/expanse/autopilot/data/local/dao/BudgetDao;", "", "getAllBudgetsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/expanse/autopilot/data/local/entity/BudgetCategoryEntity;", "getBudgetById", "categoryId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertBudget", "", "budget", "(Lcom/expanse/autopilot/data/local/entity/BudgetCategoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertBudgets", "budgets", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "incrementSpent", "amount", "", "(Ljava/lang/String;DLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "incrementLimit", "resetSpending", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllBudgets", "app_debug"})
@androidx.room.Dao()
public abstract interface BudgetDao {
    
    @androidx.room.Query(value = "SELECT * FROM budget_categories")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.expanse.autopilot.data.local.entity.BudgetCategoryEntity>> getAllBudgetsFlow();
    
    @androidx.room.Query(value = "SELECT * FROM budget_categories WHERE categoryId = :categoryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBudgetById(@org.jetbrains.annotations.NotNull()
    java.lang.String categoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.expanse.autopilot.data.local.entity.BudgetCategoryEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertBudget(@org.jetbrains.annotations.NotNull()
    com.expanse.autopilot.data.local.entity.BudgetCategoryEntity budget, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertBudgets(@org.jetbrains.annotations.NotNull()
    java.util.List<com.expanse.autopilot.data.local.entity.BudgetCategoryEntity> budgets, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE budget_categories SET currentSpent = currentSpent + :amount WHERE categoryId = :categoryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object incrementSpent(@org.jetbrains.annotations.NotNull()
    java.lang.String categoryId, double amount, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE budget_categories SET allocatedLimit = allocatedLimit + :amount WHERE categoryId = :categoryId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object incrementLimit(@org.jetbrains.annotations.NotNull()
    java.lang.String categoryId, double amount, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE budget_categories SET currentSpent = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object resetSpending(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM budget_categories")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllBudgets(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}
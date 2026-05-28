package com.expanse.autopilot.ui.dashboard;

@kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0011\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BK\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n\u00a2\u0006\u0004\b\f\u0010\rJ\u000f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H\u00c6\u0003J\u000f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\nH\u00c6\u0003J\t\u0010\u0017\u001a\u00020\nH\u00c6\u0003JM\u0010\u0018\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\nH\u00c6\u0001J\u0014\u0010\u0019\u001a\u00020\n2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001H\u00d6\u0083\u0004J\n\u0010\u001b\u001a\u00020\u001cH\u00d6\u0081\u0004J\n\u0010\u001d\u001a\u00020\u001eH\u00d6\u0081\u0004R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0012R\u0011\u0010\u000b\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0012\u00a8\u0006\u001f"}, d2 = {"Lcom/expanse/autopilot/ui/dashboard/DashboardState;", "", "transactions", "", "Lcom/expanse/autopilot/data/local/entity/TransactionEntity;", "budgets", "Lcom/expanse/autopilot/data/local/entity/BudgetCategoryEntity;", "activeGoals", "Lcom/expanse/autopilot/data/local/entity/SavingsGoalEntity;", "isQuickEntryOpen", "", "isAddingGoalOpen", "<init>", "(Ljava/util/List;Ljava/util/List;Ljava/util/List;ZZ)V", "getTransactions", "()Ljava/util/List;", "getBudgets", "getActiveGoals", "()Z", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "", "app"})
public final class DashboardState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expanse.autopilot.data.local.entity.TransactionEntity> transactions = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expanse.autopilot.data.local.entity.BudgetCategoryEntity> budgets = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.expanse.autopilot.data.local.entity.SavingsGoalEntity> activeGoals = null;
    private final boolean isQuickEntryOpen = false;
    private final boolean isAddingGoalOpen = false;
    
    public DashboardState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.expanse.autopilot.data.local.entity.TransactionEntity> transactions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expanse.autopilot.data.local.entity.BudgetCategoryEntity> budgets, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expanse.autopilot.data.local.entity.SavingsGoalEntity> activeGoals, boolean isQuickEntryOpen, boolean isAddingGoalOpen) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expanse.autopilot.data.local.entity.TransactionEntity> getTransactions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expanse.autopilot.data.local.entity.BudgetCategoryEntity> getBudgets() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expanse.autopilot.data.local.entity.SavingsGoalEntity> getActiveGoals() {
        return null;
    }
    
    public final boolean isQuickEntryOpen() {
        return false;
    }
    
    public final boolean isAddingGoalOpen() {
        return false;
    }
    
    public DashboardState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expanse.autopilot.data.local.entity.TransactionEntity> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expanse.autopilot.data.local.entity.BudgetCategoryEntity> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.expanse.autopilot.data.local.entity.SavingsGoalEntity> component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.expanse.autopilot.ui.dashboard.DashboardState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.expanse.autopilot.data.local.entity.TransactionEntity> transactions, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expanse.autopilot.data.local.entity.BudgetCategoryEntity> budgets, @org.jetbrains.annotations.NotNull()
    java.util.List<com.expanse.autopilot.data.local.entity.SavingsGoalEntity> activeGoals, boolean isQuickEntryOpen, boolean isAddingGoalOpen) {
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
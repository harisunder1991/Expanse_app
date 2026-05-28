package com.expanse.autopilot.data.local.dao;

@kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u0014\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u0018\u0010\u0007\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\rJ\u001e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u001e\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\f\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\r\u00a8\u0006\u0018\u00c0\u0006\u0003"}, d2 = {"Lcom/expanse/autopilot/data/local/dao/SavingsGoalDao;", "", "getAllGoalsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/expanse/autopilot/data/local/entity/SavingsGoalEntity;", "getActiveGoalsFlow", "getGoalById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertGoal", "goal", "(Lcom/expanse/autopilot/data/local/entity/SavingsGoalEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "incrementSavings", "", "amount", "", "(JDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCompletion", "completed", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteGoal", "app"})
@androidx.room.Dao()
public abstract interface SavingsGoalDao {
    
    @androidx.room.Query(value = "SELECT * FROM savings_goals")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.expanse.autopilot.data.local.entity.SavingsGoalEntity>> getAllGoalsFlow();
    
    @androidx.room.Query(value = "SELECT * FROM savings_goals WHERE isCompleted = 0 ORDER BY targetDate ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.expanse.autopilot.data.local.entity.SavingsGoalEntity>> getActiveGoalsFlow();
    
    @androidx.room.Query(value = "SELECT * FROM savings_goals WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getGoalById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.expanse.autopilot.data.local.entity.SavingsGoalEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertGoal(@org.jetbrains.annotations.NotNull()
    com.expanse.autopilot.data.local.entity.SavingsGoalEntity goal, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "UPDATE savings_goals SET currentAmount = currentAmount + :amount WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object incrementSavings(long id, double amount, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE savings_goals SET isCompleted = :completed WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateCompletion(long id, boolean completed, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteGoal(@org.jetbrains.annotations.NotNull()
    com.expanse.autopilot.data.local.entity.SavingsGoalEntity goal, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}
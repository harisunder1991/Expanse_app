package com.expanse.autopilot.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.expanse.autopilot.data.local.entity.TransactionEntity
import com.expanse.autopilot.ui.components.GlassyCard
import com.expanse.autopilot.ui.components.QuickExpenseDialog
import com.expanse.autopilot.ui.components.SafeToSpendCard
import com.expanse.autopilot.ui.components.SavingsProgressList
import com.expanse.autopilot.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

enum class DashboardTab {
    TRANSACTIONS, STATISTICS, AUTOPILOT, SETTINGS
}

data class CategoryStat(
    val categoryName: String,
    val amount: Double,
    val percentage: Double
)

data class MonthlyBudget(
    val fixedLimit: Double,
    val fixedSpent: Double,
    val fixedRemaining: Double,
    val flexibleLimit: Double,
    val flexibleSpent: Double,
    val flexibleRemaining: Double,
    val savingsLimit: Double,
    val savingsSpent: Double,
    val savingsRemaining: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var currentTab by remember { mutableStateOf(DashboardTab.TRANSACTIONS) }
    
    // Store selected month (default to current month)
    var selectedMonth by remember { mutableStateOf(Calendar.getInstance()) }

    var isAddingGoalOpen by remember { mutableStateOf(false) }
    var newGoalName by remember { mutableStateOf("") }
    var newGoalTarget by remember { mutableStateOf("") }

    // Transaction delete confirmation state
    var transactionToDelete by remember { mutableStateOf<TransactionEntity?>(null) }

    // Filter transactions for the selected month
    val filteredTransactions = remember(state.transactions, selectedMonth) {
        state.transactions.filter { tx ->
            val txCal = Calendar.getInstance().apply { timeInMillis = tx.timestamp }
            txCal.get(Calendar.MONTH) == selectedMonth.get(Calendar.MONTH) &&
            txCal.get(Calendar.YEAR) == selectedMonth.get(Calendar.YEAR)
        }
    }

    // Dynamic Monthly Budget Calculation
    val monthlyBudget = remember(filteredTransactions) {
        val totalIncome = filteredTransactions.filter { it.type == "CREDIT" }.sumOf { it.amount }
        val fixedLimit = totalIncome * 0.50
        val flexibleLimit = totalIncome * 0.30
        val savingsLimit = totalIncome * 0.20

        val fixedSpent = filteredTransactions.filter { it.type == "DEBIT" && it.category == "FIXED" }.sumOf { it.amount }
        val flexibleSpent = filteredTransactions.filter { it.type == "DEBIT" && it.category == "FLEXIBLE" }.sumOf { it.amount }
        val savingsSpent = filteredTransactions.filter { it.type == "DEBIT" && it.category == "SAVINGS" }.sumOf { it.amount }

        MonthlyBudget(
            fixedLimit = fixedLimit,
            fixedSpent = fixedSpent,
            fixedRemaining = fixedLimit - fixedSpent,
            flexibleLimit = flexibleLimit,
            flexibleSpent = flexibleSpent,
            flexibleRemaining = flexibleLimit - flexibleSpent,
            savingsLimit = savingsLimit,
            savingsSpent = savingsSpent,
            savingsRemaining = savingsLimit - savingsSpent
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Expanse Auto-Pilot",
                            color = TextWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Secure Key",
                                tint = EmeraldNeon,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Secure Local-Only SQLCipher",
                                color = EmeraldNeon,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkBg,
                modifier = Modifier.border(0.5.dp, Color(0x15FFFFFF), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    selected = currentTab == DashboardTab.TRANSACTIONS,
                    onClick = { currentTab = DashboardTab.TRANSACTIONS },
                    icon = { Icon(Icons.Default.List, contentDescription = "Ledger") },
                    label = { Text("Trans", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PurpleNeon,
                        selectedTextColor = PurpleNeon,
                        unselectedIconColor = TextGrey,
                        unselectedTextColor = TextGrey,
                        indicatorColor = GlassSurface
                    )
                )
                NavigationBarItem(
                    selected = currentTab == DashboardTab.STATISTICS,
                    onClick = { currentTab = DashboardTab.STATISTICS },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Stats") },
                    label = { Text("Stats", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PurpleNeon,
                        selectedTextColor = PurpleNeon,
                        unselectedIconColor = TextGrey,
                        unselectedTextColor = TextGrey,
                        indicatorColor = GlassSurface
                    )
                )
                NavigationBarItem(
                    selected = currentTab == DashboardTab.AUTOPILOT,
                    onClick = { currentTab = DashboardTab.AUTOPILOT },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Auto-Pilot") },
                    label = { Text("Auto-Pilot", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PurpleNeon,
                        selectedTextColor = PurpleNeon,
                        unselectedIconColor = TextGrey,
                        unselectedTextColor = TextGrey,
                        indicatorColor = GlassSurface
                    )
                )
                NavigationBarItem(
                    selected = currentTab == DashboardTab.SETTINGS,
                    onClick = { currentTab = DashboardTab.SETTINGS },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PurpleNeon,
                        selectedTextColor = PurpleNeon,
                        unselectedIconColor = TextGrey,
                        unselectedTextColor = TextGrey,
                        indicatorColor = GlassSurface
                    )
                )
            }
        },
        floatingActionButton = {
            if (currentTab != DashboardTab.SETTINGS) {
                FloatingActionButton(
                    onClick = { viewModel.openQuickEntry() },
                    containerColor = PurpleNeon,
                    contentColor = TextWhite,
                    shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Transaction")
                }
            }
        },
        containerColor = DarkBg,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DarkBg, Color(0xFF070A14))
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Month selector navigation at the top of Ledger, Stats and Auto-Pilot
                if (currentTab != DashboardTab.SETTINGS) {
                    MonthSelector(
                        selectedMonth = selectedMonth,
                        onMonthChanged = { selectedMonth = it }
                    )
                }

                when (currentTab) {
                    DashboardTab.TRANSACTIONS -> LedgerTab(
                        transactions = filteredTransactions,
                        onDeleteRequest = { transactionToDelete = it },
                        onAddManual = { viewModel.openQuickEntry() }
                    )
                    DashboardTab.STATISTICS -> StatsTab(
                        transactions = filteredTransactions
                    )
                    DashboardTab.AUTOPILOT -> AutoPilotTab(
                        monthlyBudget = monthlyBudget,
                        activeGoals = state.activeGoals,
                        onGoalClick = { isAddingGoalOpen = true }
                    )
                    DashboardTab.SETTINGS -> SettingsTab(
                        onReset = { viewModel.resetDataForPrivacy() }
                    )
                }
            }

            // Quick Transaction Entry Dialog Overlay
            if (state.isQuickEntryOpen) {
                QuickExpenseDialog(
                    onDismiss = { viewModel.closeQuickEntry() },
                    onSave = { amount, type, category, desc, subCat, acc, timestamp ->
                        viewModel.addManualTransaction(amount, type, category, desc, subCat, acc, timestamp)
                    }
                )
            }

            // Delete Confirmation Alert Dialog
            transactionToDelete?.let { tx ->
                DeleteConfirmationDialog(
                    onDismiss = { transactionToDelete = null },
                    onConfirm = {
                        viewModel.deleteTransaction(tx)
                        transactionToDelete = null
                    }
                )
            }

            // Add Savings Goal Dialog Overlay
            if (isAddingGoalOpen) {
                Dialog(onDismissRequest = { isAddingGoalOpen = false }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp))
                            .background(DarkBg)
                            .padding(24.dp)
                    ) {
                        Column {
                            Text(
                                text = "Create Savings Goal",
                                color = TextWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            OutlinedTextField(
                                value = newGoalName,
                                onValueChange = { newGoalName = it },
                                label = { Text("Goal Name (e.g. Dream Laptop)", color = TextWhite.copy(alpha = 0.6f)) },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite,
                                    focusedBorderColor = PurpleNeon,
                                    unfocusedBorderColor = Color(0x40FFFFFF)
                                ),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                            )

                            OutlinedTextField(
                                value = newGoalTarget,
                                onValueChange = { newGoalTarget = it },
                                label = { Text("Target Amount (₹)", color = TextWhite.copy(alpha = 0.6f)) },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite,
                                    focusedBorderColor = PurpleNeon,
                                    unfocusedBorderColor = Color(0x40FFFFFF)
                                ),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { isAddingGoalOpen = false }) {
                                    Text("Cancel", color = TextWhite.copy(alpha = 0.6f))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Button(
                                    onClick = {
                                        val amt = newGoalTarget.toDoubleOrNull() ?: 0.0
                                        if (newGoalName.isNotBlank() && amt > 0) {
                                            viewModel.createGoal(newGoalName, amt, 12)
                                            isAddingGoalOpen = false
                                            newGoalName = ""
                                            newGoalTarget = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PurpleNeon)
                                ) {
                                    Text("Create", color = TextWhite)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthSelector(
    selectedMonth: Calendar,
    onMonthChanged: (Calendar) -> Unit
) {
    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(GlassSurface)
            .border(0.5.dp, Color(0x15FFFFFF), RoundedCornerShape(16.dp))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            val newCal = Calendar.getInstance().apply {
                timeInMillis = selectedMonth.timeInMillis
                add(Calendar.MONTH, -1)
            }
            onMonthChanged(newCal)
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month", tint = TextWhite)
        }

        Text(
            text = monthFormat.format(selectedMonth.time).uppercase(),
            color = TextWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp
        )

        IconButton(onClick = {
            val newCal = Calendar.getInstance().apply {
                timeInMillis = selectedMonth.timeInMillis
                add(Calendar.MONTH, 1)
            }
            onMonthChanged(newCal)
        }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Month", tint = TextWhite)
        }
    }
}

@Composable
fun LedgerTab(
    transactions: List<TransactionEntity>,
    onDeleteRequest: (TransactionEntity) -> Unit,
    onAddManual: () -> Unit
) {
    // Group transactions by day format: "dd MMM yyyy (EEE)"
    val grouped = remember(transactions) {
        val sdf = SimpleDateFormat("dd MMM yyyy (EEE)", Locale.getDefault())
        transactions.groupBy { sdf.format(Date(it.timestamp)) }
    }

    if (transactions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "No Transactions Found",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    text = "No entries match the selected month. Add manual entries or receive bank alerts to populate the ledger.",
                    color = TextGrey,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 20.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Button(
                    onClick = onAddManual,
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleNeon)
                ) {
                    Text("Add Transaction", color = TextWhite)
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            grouped.forEach { (dateHeader, txsInGroup) ->
                item {
                    val groupIncome = txsInGroup.filter { it.type == "CREDIT" }.sumOf { it.amount }
                    val groupExpense = txsInGroup.filter { it.type == "DEBIT" }.sumOf { it.amount }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x05FFFFFF))
                            .border(0.5.dp, Color(0x10FFFFFF), RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        // Header for each day
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dateHeader.uppercase(),
                                color = TextGrey,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.2.sp
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (groupIncome > 0) {
                                    Text(
                                        text = "+₹${String.format("%.0f", groupIncome)}",
                                        color = EmeraldNeon,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                if (groupExpense > 0) {
                                    Text(
                                        text = "-₹${String.format("%.0f", groupExpense)}",
                                        color = PurpleNeon,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Render each item
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            txsInGroup.forEach { tx ->
                                LedgerRowItem(transaction = tx, onDelete = { onDeleteRequest(tx) })
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun LedgerRowItem(
    transaction: TransactionEntity,
    onDelete: () -> Unit
) {
    val isCredit = transaction.type == "CREDIT"
    val colorAccent = if (isCredit) EmeraldNeon else PurpleNeon

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GlassSurface)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colorAccent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        transaction.isAutoScraped -> Icons.Default.MailOutline
                        transaction.subCategory == "Food" -> Icons.Default.ShoppingCart
                        transaction.subCategory == "Travel" -> Icons.Default.PlayArrow
                        transaction.subCategory == "Bills" -> Icons.Default.Star
                        else -> Icons.Default.Edit
                    },
                    contentDescription = "Icon",
                    tint = colorAccent,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = transaction.description,
                        color = TextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x15FFFFFF))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = transaction.account,
                            color = TextLightGrey,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Text(
                    text = "${transaction.subCategory} • ${if (transaction.isAutoScraped) "Auto" else "Manual"}",
                    color = TextGrey,
                    fontSize = 10.sp
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${if (isCredit) "+" else "-"}₹${String.format("%.1f", transaction.amount)}",
                color = colorAccent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete entry",
                    tint = Color.Red.copy(alpha = 0.6f),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun StatsTab(
    transactions: List<TransactionEntity>
) {
    val totalIncome = transactions.filter { it.type == "CREDIT" }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.type == "DEBIT" }.sumOf { it.amount }

    val categoryStats = remember(transactions) {
        val expenses = transactions.filter { it.type == "DEBIT" }
        val sum = expenses.sumOf { it.amount }
        expenses.groupBy { it.subCategory }
            .map { (cat, txList) ->
                val amt = txList.sumOf { it.amount }
                val pct = if (sum > 0) (amt / sum) * 100 else 0.0
                CategoryStat(cat, amt, pct)
            }.sortedByDescending { it.amount }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Summary Card
        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TOTAL EXPENDITURE SUMMARY",
                        color = TextGrey,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "₹${String.format("%.2f", totalExpense)}",
                        color = PurpleNeon,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Total Income", color = TextGrey, fontSize = 11.sp)
                            Text(
                                text = "₹${String.format("%.1f", totalIncome)}",
                                color = EmeraldNeon,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Net Cashflow", color = TextGrey, fontSize = 11.sp)
                            val flow = totalIncome - totalExpense
                            Text(
                                text = "${if (flow >= 0) "+" else ""}₹${String.format("%.1f", flow)}",
                                color = if (flow >= 0) EmeraldNeon else Color.Red,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Category Breakdown Header
        item {
            Text(
                text = "EXPENDITURE BY CATEGORY",
                color = TextGrey,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (categoryStats.isEmpty()) {
            item {
                GlassyCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "No category data available for this selection.",
                        color = TextGrey,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(categoryStats) { stat ->
                GlassyCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stat.categoryName,
                                color = TextWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "₹${String.format("%.1f", stat.amount)} (${String.format("%.1f", stat.percentage)}%)",
                                color = PurpleNeon,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0x10FFFFFF))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth((stat.percentage / 100).toFloat())
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(PurpleNeon)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun AutoPilotTab(
    monthlyBudget: MonthlyBudget,
    activeGoals: List<SavingsGoalEntity>,
    onGoalClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            SafeToSpendCard(
                remaining = monthlyBudget.flexibleRemaining,
                limit = monthlyBudget.flexibleLimit,
                spent = monthlyBudget.flexibleSpent
            )
        }

        item {
            Button(
                onClick = onGoalClick,
                colors = ButtonDefaults.buttonColors(containerColor = GlassSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(0.5.dp, Color(0x15FFFFFF), RoundedCornerShape(12.dp))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "New Goal",
                        tint = AmberNeon,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create New Savings Goal", color = TextWhite, fontSize = 13.sp)
                }
            }
        }

        item {
            SavingsProgressList(goals = activeGoals)
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SettingsTab(
    onReset: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "SECURE ENCRYPTION STATUS",
                        color = EmeraldNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Your finance records are fully protected by military-grade AES-256 hardware-level SQLCipher. The decryption keys reside safely inside your Android KeyStore hardware backing, completely isolated from any external applications or net connections.",
                        color = TextLightGrey,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "PHYSICAL DATABASE BACKUPS",
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "To backup your secure database, you can physically copy your local Room encrypted file located at:\n/data/data/com.expanse.autopilot/databases/expanse_secure.db\n\nNo developer backend or cloud service ever sees or transmits this file.",
                        color = TextLightGrey,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "PRIVACY COMMANDS & RESET",
                        color = Color.Red,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Warning: Resetting your data is irreversible. All manual and auto-scraped transactions will be destroyed, and SQLCipher keys will be re-generated.",
                        color = TextGrey,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = onReset,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Reset All Local Data", color = TextWhite)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Transaction?",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Text(
                text = "Are you sure you want to permanently delete this transaction? This will automatically reverse all budget allocations.",
                color = TextGrey,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Delete", color = TextWhite)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextWhite.copy(alpha = 0.6f))
            }
        },
        containerColor = DarkBg,
        shape = RoundedCornerShape(20.dp)
    )
}

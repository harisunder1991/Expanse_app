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
import androidx.compose.ui.graphics.vector.ImageVector
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

enum class Period {
    DAILY, WEEKLY, MONTHLY, YEARLY
}

data class CategoryStat(
    val categoryName: String,
    val amount: Double,
    val percentage: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var currentTab by remember { mutableStateOf(DashboardTab.TRANSACTIONS) }
    var currentPeriod by remember { mutableStateOf(Period.DAILY) }

    var isAddingGoalOpen by remember { mutableStateOf(false) }
    var newGoalName by remember { mutableStateOf("") }
    var newGoalTarget by remember { mutableStateOf("") }

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
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Stats") }, // standard playarrow as custom wedge representation
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
                // Render Period Filter Toggle (Daily / Weekly / Monthly / Yearly) for Trans & Stats Tab
                if (currentTab == DashboardTab.TRANSACTIONS || currentTab == DashboardTab.STATISTICS) {
                    PeriodFilterBar(
                        selectedPeriod = currentPeriod,
                        onPeriodSelected = { currentPeriod = it }
                    )
                }

                when (currentTab) {
                    DashboardTab.TRANSACTIONS -> LedgerTab(
                        transactions = state.transactions,
                        period = currentPeriod,
                        onDelete = { viewModel.deleteTransaction(it) },
                        onAddManual = { viewModel.openQuickEntry() }
                    )
                    DashboardTab.STATISTICS -> StatsTab(
                        transactions = state.transactions,
                        period = currentPeriod
                    )
                    DashboardTab.AUTOPILOT -> AutoPilotTab(
                        state = state,
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
                    onSave = { amount, type, category, desc, subCat, acc ->
                        viewModel.addManualTransaction(amount, type, category, desc, subCat, acc)
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
fun PeriodFilterBar(
    selectedPeriod: Period,
    onPeriodSelected: (Period) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(GlassSurface)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Period.values().forEach { period ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (period == selectedPeriod) PurpleNeon else Color.Transparent)
                    .clickable { onPeriodSelected(period) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = period.name.replaceFirstChar { it.uppercase() },
                    color = TextWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LedgerTab(
    transactions: List<TransactionEntity>,
    period: Period,
    onDelete: (TransactionEntity) -> Unit,
    onAddManual: () -> Unit
) {
    // 1. Filter and group transactions based on selected period
    val grouped = remember(transactions, period) {
        val sdf = SimpleDateFormat(
            when (period) {
                Period.DAILY -> "dd MMM yyyy (EEE)"
                Period.WEEKLY -> "'Week' w, yyyy"
                Period.MONTHLY -> "MMMM yyyy"
                Period.YEARLY -> "yyyy"
            }, Locale.getDefault()
        )
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
                    text = "Encrypted Ledger Empty",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Any manually entered transactions or bank UPI SMS triggers will automatically show here.",
                    color = TextGrey,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Button(
                    onClick = onAddManual,
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleNeon)
                ) {
                    Text("Add Entry Now", color = TextWhite)
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
                    val groupExpense = txsInGroup.filter { it.type == "DEBIT" || it.type == "SWEEP" }.sumOf { it.amount }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x05FFFFFF))
                            .border(0.5.dp, Color(0x10FFFFFF), RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        // Date header row with daily sub-totals
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

                        // Transaction List Items for this date
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            txsInGroup.forEach { tx ->
                                LedgerRowItem(transaction = tx, onDelete = { onDelete(tx) })
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
    val isSweep = transaction.type == "SWEEP"
    val colorAccent = when {
        isSweep -> AmberNeon
        isCredit -> EmeraldNeon
        else -> PurpleNeon
    }

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
                        transaction.subCategory == "Food" -> Icons.Default.ShoppingCart // cart representative for food/shopping
                        transaction.subCategory == "Travel" -> Icons.Default.PlayArrow // directional icon
                        transaction.subCategory == "Bills" -> Icons.Default.Star
                        else -> Icons.Default.Edit
                    },
                    contentDescription = "Category Icon",
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
                    // Account badge
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
    transactions: List<TransactionEntity>,
    period: Period
) {
    // 1. Filter by time period
    val filteredTxs = remember(transactions, period) {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = now

        transactions.filter {
            val txCal = Calendar.getInstance()
            txCal.timeInMillis = it.timestamp
            when (period) {
                Period.DAILY -> calendar.get(Calendar.DAY_OF_YEAR) == txCal.get(Calendar.DAY_OF_YEAR) && calendar.get(Calendar.YEAR) == txCal.get(Calendar.YEAR)
                Period.WEEKLY -> calendar.get(Calendar.WEEK_OF_YEAR) == txCal.get(Calendar.WEEK_OF_YEAR) && calendar.get(Calendar.YEAR) == txCal.get(Calendar.YEAR)
                Period.MONTHLY -> calendar.get(Calendar.MONTH) == txCal.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == txCal.get(Calendar.YEAR)
                Period.YEARLY -> calendar.get(Calendar.YEAR) == txCal.get(Calendar.YEAR)
            }
        }
    }

    val totalIncome = filteredTxs.filter { it.type == "CREDIT" }.sumOf { it.amount }
    val totalExpense = filteredTxs.filter { it.type == "DEBIT" || it.type == "SWEEP" }.sumOf { it.amount }

    val categoryStats = remember(filteredTxs) {
        val expenses = filteredTxs.filter { it.type == "DEBIT" || it.type == "SWEEP" }
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
    state: DashboardState,
    onGoalClick: () -> Unit
) {
    val flexibleBudget = state.budgets.find { it.categoryId == "FLEXIBLE" }
    val remainingFlexible = flexibleBudget?.remaining ?: 0.0
    val totalFlexibleLimit = flexibleBudget?.allocatedLimit ?: 0.0
    val flexibleSpent = flexibleBudget?.currentSpent ?: 0.0

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
                remaining = remainingFlexible,
                limit = totalFlexibleLimit,
                spent = flexibleSpent
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
            SavingsProgressList(goals = state.activeGoals)
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

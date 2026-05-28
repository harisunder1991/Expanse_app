package com.expanse.autopilot.ui.dashboard

import androidx.compose.foundation.background
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    
    // Calculate Safe-To-Spend values
    val flexibleBudget = state.budgets.find { it.categoryId == "FLEXIBLE" }
    val remainingFlexible = flexibleBudget?.remaining ?: 0.0
    val totalFlexibleLimit = flexibleBudget?.allocatedLimit ?: 0.0
    val flexibleSpent = flexibleBudget?.currentSpent ?: 0.0

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
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Secure Key",
                                tint = EmeraldNeon,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Local-Only SQLCipher Encrypted",
                                color = EmeraldNeon,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.resetDataForPrivacy() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset local data",
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openQuickEntry() },
                containerColor = PurpleNeon,
                contentColor = TextWhite,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Transaction")
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                // Safe-To-Spend Radial Card
                item {
                    SafeToSpendCard(
                        remaining = remainingFlexible,
                        limit = totalFlexibleLimit,
                        spent = flexibleSpent
                    )
                }

                // Quick buttons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { isAddingGoalOpen = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GlassSurface),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Goal",
                                    tint = AmberNeon,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("New Goal", color = TextWhite, fontSize = 13.sp)
                            }
                        }

                        Button(
                            onClick = { viewModel.openQuickEntry() },
                            colors = ButtonDefaults.buttonColors(containerColor = GlassSurface),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Manual Entry",
                                    tint = BlueNeon,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Manual Entry", color = TextWhite, fontSize = 13.sp)
                            }
                        }
                    }
                }

                // Goals List
                item {
                    SavingsProgressList(goals = state.activeGoals)
                }

                // Recent Transactions List
                item {
                    Text(
                        text = "RECENT AUTO-PILOT RUNS",
                        color = TextGrey,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                if (state.transactions.isEmpty()) {
                    item {
                        GlassyCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Your encrypted ledger is empty. Financial SMS triggers or manual entries will automatically show here.",
                                color = TextGrey,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(state.transactions) { transaction ->
                        TransactionRow(transaction = transaction)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp)) // Avoid floating button overlap
                }
            }

            // Manual Entry Transaction Dialog Overlay
            if (state.isQuickEntryOpen) {
                QuickExpenseDialog(
                    onDismiss = { viewModel.closeQuickEntry() },
                    onSave = { amount, type, category, desc ->
                        viewModel.addManualTransaction(amount, type, category, desc)
                    }
                )
            }

            // Manual Savings Goal Dialog Overlay
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
                                label = { Text("Goal Name (e.g. Emergency Fund)", color = TextWhite.copy(alpha = 0.6f)) },
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
fun TransactionRow(transaction: TransactionEntity) {
    val isCredit = transaction.type == "CREDIT"
    val isSweep = transaction.type == "SWEEP"
    val colorAccent = when {
        isSweep -> AmberNeon
        isCredit -> EmeraldNeon
        else -> PurpleNeon
    }

    GlassyCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Source Icon Indicating scrap type
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colorAccent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (transaction.isAutoScraped) Icons.Default.MailOutline else Icons.Default.Edit,
                        contentDescription = "Source",
                        tint = colorAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = transaction.description,
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${transaction.category} • ${if (transaction.isAutoScraped) "Auto-Parsed" else "Manual"}",
                        color = TextGrey,
                        fontSize = 11.sp
                    )
                }
            }

            Text(
                text = "${if (isCredit) "+" else "-"}₹${String.format("%.2f", transaction.amount)}",
                color = colorAccent,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

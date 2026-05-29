package com.expanse.autopilot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.expanse.autopilot.ui.theme.DarkBg
import com.expanse.autopilot.ui.theme.EmeraldNeon
import com.expanse.autopilot.ui.theme.PurpleNeon
import com.expanse.autopilot.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickExpenseDialog(
    onDismiss: () -> Unit,
    onSave: (amount: Double, type: String, category: String, description: String, subCategory: String, account: String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isDebit by remember { mutableStateOf(true) } // DEBIT vs CREDIT
    var selectedCategory by remember { mutableStateOf("FLEXIBLE") } // "FIXED", "FLEXIBLE", "SAVINGS"
    var selectedSubCategory by remember { mutableStateOf("General") } // "Food", "Shopping", "Travel", "Bills", "Entertainment", "General"
    var selectedAccount by remember { mutableStateOf("Secure Bank") } // "Cash", "Secure Bank", "Card"

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(DarkBg)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Manual Entry",
                    color = TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Type Toggle Selection
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { isDebit = true; selectedCategory = "FLEXIBLE"; selectedSubCategory = "General" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDebit) PurpleNeon else Color(0x20FFFFFF)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Debit (Expense)", color = TextWhite, fontSize = 12.sp)
                    }
                    Button(
                        onClick = { isDebit = false; selectedCategory = "FIXED"; selectedSubCategory = "Salary" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isDebit) EmeraldNeon else Color(0x20FFFFFF)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Credit (Income)", color = TextWhite, fontSize = 12.sp)
                    }
                }

                // Envelope selection for Debits
                if (isDebit) {
                    Text(
                        text = "Envelope Category (50/30/20 Rule)",
                        color = TextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("FIXED", "FLEXIBLE", "SAVINGS").forEach { cat ->
                            Button(
                                onClick = { selectedCategory = cat },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedCategory == cat) PurpleNeon.copy(alpha = 0.8f) else Color(0x10FFFFFF)
                                ),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 4.dp)
                            ) {
                                Text(text = cat, fontSize = 10.sp, color = TextWhite)
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Allocated auto-split 50% Fixed, 30% Flex, 20% Savings.",
                        color = EmeraldNeon,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                // Sub-Category Selection (Only relevant for expenses)
                if (isDebit) {
                    Text(
                        text = "Expense Sub-category",
                        color = TextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        maxItemsInEachRow = 3
                    ) {
                        listOf("Food", "Shopping", "Travel", "Bills", "Entertainment", "General").forEach { sub ->
                            FilterChip(
                                selected = selectedSubCategory == sub,
                                onClick = { selectedSubCategory = sub },
                                label = { Text(text = sub, fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PurpleNeon,
                                    selectedLabelColor = TextWhite,
                                    containerColor = Color(0x10FFFFFF),
                                    labelColor = TextWhite
                                )
                            )
                        }
                    }
                }

                // Account Selection
                Text(
                    text = "Account used",
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Cash", "Secure Bank", "Card").forEach { acc ->
                        Button(
                            onClick = { selectedAccount = acc },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (acc == selectedAccount) PurpleNeon.copy(alpha = 0.8f) else Color(0x10FFFFFF)
                            ),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            Text(text = acc, fontSize = 10.sp, color = TextWhite)
                        }
                    }
                }

                // Amount Input
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (₹)", color = TextWhite.copy(alpha = 0.6f)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = PurpleNeon,
                        unfocusedBorderColor = Color(0x40FFFFFF)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Merchant / Description", color = TextWhite.copy(alpha = 0.6f)) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = PurpleNeon,
                        unfocusedBorderColor = Color(0x40FFFFFF)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel", color = TextWhite.copy(alpha = 0.6f))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            val amt = amount.toDoubleOrNull() ?: 0.0
                            if (amt > 0 && description.isNotBlank()) {
                                onSave(
                                    amt,
                                    if (isDebit) "DEBIT" else "CREDIT",
                                    if (isDebit) selectedCategory else "FIXED",
                                    description,
                                    if (isDebit) selectedSubCategory else "Salary",
                                    selectedAccount
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleNeon)
                    ) {
                        Text(text = "Save Entry", color = TextWhite)
                    }
                }
            }
        }
    }
}

package com.expanse.autopilot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expanse.autopilot.data.local.entity.SavingsGoalEntity
import com.expanse.autopilot.ui.theme.AmberNeon
import com.expanse.autopilot.ui.theme.BlueNeon
import com.expanse.autopilot.ui.theme.TextGrey
import com.expanse.autopilot.ui.theme.TextWhite

@Composable
fun SavingsProgressList(
    goals: List<SavingsGoalEntity>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "SAVINGS MILESTONES & SWEEPS",
            color = TextGrey,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        if (goals.isEmpty()) {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "No active savings goals. Add a new goal to trigger automatic sweeps!",
                    color = TextGrey,
                    fontSize = 14.sp
                )
            }
        } else {
            goals.forEach { goal ->
                GlassyCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = goal.goalName,
                                color = TextWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "₹${String.format("%.0f", goal.currentAmount)} / ₹${String.format("%.0f", goal.targetAmount)}",
                                color = AmberNeon,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        // Custom Gradient Progress Bar
                        val progress = if (goal.targetAmount > 0) (goal.currentAmount / goal.targetAmount).coerceIn(0.0, 1.0) else 0.0
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0x20FFFFFF))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(progress.toFloat())
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(BlueNeon, AmberNeon)
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

package com.expanse.autopilot.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expanse.autopilot.ui.theme.BlueNeon
import com.expanse.autopilot.ui.theme.EmeraldNeon
import com.expanse.autopilot.ui.theme.TextGrey
import com.expanse.autopilot.ui.theme.TextWhite

@Composable
fun SafeToSpendCard(
    remaining: Double,
    limit: Double,
    spent: Double,
    modifier: Modifier = Modifier
) {
    GlassyCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SAFE-TO-SPEND FLEXIBLE BUDGET",
                color = TextGrey,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "₹${String.format("%.2f", remaining)}",
                color = if (remaining > 0) EmeraldNeon else Color.Red,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Total Allocated", color = TextGrey, fontSize = 12.sp)
                    Text(
                        text = "₹${String.format("%.2f", limit)}",
                        color = TextWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Spent + Sweeps", color = TextGrey, fontSize = 12.sp)
                    Text(
                        text = "₹${String.format("%.2f", spent)}",
                        color = BlueNeon,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

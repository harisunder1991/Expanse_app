package com.expanse.autopilot.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.expanse.autopilot.data.repository.FinanceRepository
import com.expanse.autopilot.domain.model.TransactionType
import com.expanse.autopilot.domain.parser.SmsParserImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsBroadcastReceiver : BroadcastReceiver() {
    private val parser = SmsParserImpl()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val repo = FinanceRepository(context.applicationContext)
            val pendingResult = goAsync()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    for (msg in messages) {
                        val body = msg.messageBody ?: continue
                        val parsed = parser.parse(body)
                        if (parsed != null) {
                            // By default, auto credit goes to FIXED split, debits go to FLEXIBLE budget
                            val category = if (parsed.type == TransactionType.CREDIT) "FIXED" else "FLEXIBLE"
                            
                            repo.addTransaction(
                                amount = parsed.amount,
                                type = parsed.type.name,
                                category = category,
                                description = parsed.merchant,
                                isAutoScraped = true,
                                subCategory = parsed.subCategory,
                                account = "Secure Bank"
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}

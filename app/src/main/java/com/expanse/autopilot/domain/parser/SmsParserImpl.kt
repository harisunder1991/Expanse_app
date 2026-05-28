package com.expanse.autopilot.domain.parser

import com.expanse.autopilot.domain.model.TransactionType
import java.util.regex.Pattern

class SmsParserImpl : SmsParser {

    private val amountRegex = Pattern.compile(
        "(?i)(?:Rs\\.?|INR|₹)\\s*([0-9,]+(?:\\.[0-9]{2})?)"
    )

    private val debitKeywords = listOf(
        "debited", "withdrawn", "spent", "txn of", "paid", "payment of", "sent to", "transferred"
    )

    private val creditKeywords = listOf(
        "credited", "deposited", "salary", "added to", "received"
    )

    private val merchantKeywords = listOf(
        "zomato", "swiggy", "paytm", "phonepe", "amazon", "flipkart", "uber", "ola", "netflix", 
        "spotify", "starbucks", "hdfc", "sbi", "icici", "axis", "gpay", "bms", "bookmyshow"
    )

    override fun parse(smsBody: String): ParsedSms? {
        if (smsBody.isBlank()) return null

        val cleanSms = smsBody.replace("\n", " ").trim()
        val matcher = amountRegex.matcher(cleanSms)
        if (!matcher.find()) return null

        val amountString = matcher.group(1) ?: return null
        val amount = amountString.replace(",", "").toDoubleOrNull() ?: return null

        // Determine transaction type
        val isDebit = debitKeywords.any { cleanSms.contains(it, ignoreCase = true) }
        val isCredit = creditKeywords.any { cleanSms.contains(it, ignoreCase = true) }

        if (!isDebit && !isCredit) return null // Could not conclusively determine transaction type

        val type = if (isCredit && !isDebit) TransactionType.CREDIT else TransactionType.DEBIT

        // Detect Merchant or Origin
        var merchant = "Unknown Merchant"
        
        // Strategy A: Match common predefined merchants
        val matchedMerchant = merchantKeywords.firstOrNull { cleanSms.contains(it, ignoreCase = true) }
        if (matchedMerchant != null) {
            merchant = matchedMerchant.replaceFirstChar { it.uppercase() }
        } else {
            // Strategy B: Try extracting after common connector words "at", "to", "spent on"
            val patternAt = Pattern.compile("(?i)(?:at|to|spent on|on)\\s+([A-Za-z0-9\\s]{3,15})")
            val matchAt = patternAt.matcher(cleanSms)
            if (matchAt.find()) {
                val extracted = matchAt.group(1)?.trim()
                if (!extracted.isNullOrBlank()) {
                    merchant = extracted.split(" ")[0].replaceFirstChar { it.uppercase() }
                }
            }
        }

        return ParsedSms(
            amount = amount,
            type = type,
            merchant = merchant
        )
    }
}

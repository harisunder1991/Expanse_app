package com.expanse.autopilot.domain.parser

import com.expanse.autopilot.domain.model.TransactionType
import java.util.regex.Pattern

class SmsParserImpl : SmsParser {

    // Supports: Rs 200, Rs. 150, INR 12000, ₹100, ₹ 50.00, Rs.1,500.00 etc.
    private val amountRegex = Pattern.compile(
        "(?i)(?:Rs\\.?|INR|₹)\\s*([0-9,]+(?:\\.[0-9]+)?)"
    )

    private val debitKeywords = listOf(
        "debited", "withdrawn", "spent", "txn of", "paid to", "paid", "payment of", "sent to", "transferred", "transfer to", "debit"
    )

    private val creditKeywords = listOf(
        "credited", "deposited", "salary", "added to", "received from", "received", "credit"
    )

    // Predefined merchant to sub-category mapping for automated tracking
    private val categoryMap = mapOf(
        "Zomato" to "Food",
        "Swiggy" to "Food",
        "Uber" to "Travel",
        "Ola" to "Travel",
        "Netflix" to "Entertainment",
        "Spotify" to "Entertainment",
        "Amazon" to "Shopping",
        "Flipkart" to "Shopping",
        "Myntra" to "Shopping",
        "Paytm" to "Bills",
        "PhonePe" to "Bills",
        "GPay" to "Bills"
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

        if (!isDebit && !isCredit) return null // Could not determine type

        val type = if (isCredit && !isDebit) TransactionType.CREDIT else TransactionType.DEBIT

        // Detect Merchant or Origin from UPI formats
        var merchant = "Unknown Merchant"
        
        // Extract VPA from UPI (e.g. zomato@paytm, 9876543210@ybl, amit@okaxis)
        val vpaPattern = Pattern.compile("([a-zA-Z0-9.\\-_]+@[a-zA-Z]{3,})")
        val vpaMatcher = vpaPattern.matcher(cleanSms)
        
        if (vpaMatcher.find()) {
            val fullVpa = vpaMatcher.group(1) ?: ""
            val namePart = fullVpa.split("@")[0]
            
            // Clean up name part from common prefixes/suffixes
            merchant = namePart
                .replace("(?i)gpay-".toRegex(), "")
                .replace("(?i)upi-".toRegex(), "")
                .replace("(?i)paytm-".toRegex(), "")
                .replace("(?i)-pay".toRegex(), "")
                .replace("(?i)-upi".toRegex(), "")
                .replace("(?i)phonepe-".toRegex(), "")
                .split(".")[0] // e.g. swiggy.pay -> swiggy
                .replaceFirstChar { it.uppercase() }
        } else {
            // Preposition search (e.g. at Zomato, to Rajesh, spent at local shop)
            val patternAt = Pattern.compile("(?i)(?:at|to|spent on|on|sent to|paid to|transfer to)\\s+([A-Za-z0-9\\s]{3,20})")
            val matchAt = patternAt.matcher(cleanSms)
            if (matchAt.find()) {
                val extracted = matchAt.group(1)?.trim()
                if (!extracted.isNullOrBlank()) {
                    val words = extracted.split(" ")
                    val candidate = words[0]
                    // Ignore numbers and date stamps
                    if (!candidate.contains("ref", ignoreCase = true) && 
                        !candidate.contains("refno", ignoreCase = true) && 
                        !candidate.matches("[0-9./-]+".toRegex())) {
                        merchant = candidate.replaceFirstChar { it.uppercase() }
                    }
                }
            }
        }

        // Clean up common merchant names
        val lowerMerchant = merchant.lowercase()
        val finalMerchant = when {
            lowerMerchant.contains("zomato") -> "Zomato"
            lowerMerchant.contains("swiggy") -> "Swiggy"
            lowerMerchant.contains("uber") -> "Uber"
            lowerMerchant.contains("ola") -> "Ola"
            lowerMerchant.contains("amazon") -> "Amazon"
            lowerMerchant.contains("flipkart") -> "Flipkart"
            lowerMerchant.contains("myntra") -> "Myntra"
            lowerMerchant.contains("netflix") -> "Netflix"
            lowerMerchant.contains("spotify") -> "Spotify"
            lowerMerchant.contains("paytm") -> "Paytm"
            lowerMerchant.contains("phonepe") -> "PhonePe"
            lowerMerchant.contains("gpay") -> "GPay"
            else -> merchant
        }

        val subCategory = categoryMap[finalMerchant] ?: "General"

        return ParsedSms(
            amount = amount,
            type = type,
            merchant = finalMerchant,
            subCategory = subCategory
        )
    }
}

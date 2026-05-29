package com.expanse.autopilot.domain.parser

import com.expanse.autopilot.domain.model.TransactionType

data class ParsedSms(
    val amount: Double,
    val type: TransactionType,
    val merchant: String,
    val subCategory: String = "General"
)

interface SmsParser {
    fun parse(smsBody: String): ParsedSms?
}

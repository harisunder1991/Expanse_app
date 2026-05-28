package com.expanse.autopilot

import com.expanse.autopilot.domain.engine.BudgetEngine
import com.expanse.autopilot.domain.engine.SweepEngine
import com.expanse.autopilot.domain.model.TransactionType
import com.expanse.autopilot.domain.parser.SmsParserImpl
import org.junit.Assert.*
import org.junit.Test

class FinanceEngineTest {

    private val parser = SmsParserImpl()
    private val budgetEngine = BudgetEngine()
    private val sweepEngine = SweepEngine()

    @Test
    fun testSmsParser_Debit() {
        val sms = "Alert: Rs 120.00 debited from a/c ...1234 spent at Zomato. Info: UPI-ZOMATO-PAY-@paytm"
        val parsed = parser.parse(sms)
        
        assertNotNull(parsed)
        assertEquals(120.0, parsed!!.amount, 0.001)
        assertEquals(TransactionType.DEBIT, parsed.type)
        assertEquals("Zomato", parsed.merchant)
    }

    @Test
    fun testSmsParser_Credit() {
        val sms = "Dear customer, Rs 45,000.00 has been credited to your account ...7890. Info: SALARY."
        val parsed = parser.parse(sms)

        assertNotNull(parsed)
        assertEquals(45000.0, parsed!!.amount, 0.001)
        assertEquals(TransactionType.CREDIT, parsed.type)
        assertEquals("Salary", parsed.merchant)
    }

    @Test
    fun testBudgetEngine_Split() {
        val income = 10000.0
        val split = budgetEngine.calculateEnvelopeSplit(income)

        assertEquals(5000.0, split.fixedAmount, 0.001)
        assertEquals(3000.0, split.flexibleAmount, 0.001)
        assertEquals(2000.0, split.savingsAmount, 0.001)
    }

    @Test
    fun testSweepEngine_Calculations() {
        // Test standard round-up values
        assertEquals(30.0, sweepEngine.calculateSweepAmount(120.0), 0.001)
        assertEquals(0.0, sweepEngine.calculateSweepAmount(150.0), 0.001)
        assertEquals(49.0, sweepEngine.calculateSweepAmount(151.0), 0.001)
        assertEquals(45.0, sweepEngine.calculateSweepAmount(5.0), 0.001)
        assertEquals(0.0, sweepEngine.calculateSweepAmount(0.0), 0.001)
    }
}

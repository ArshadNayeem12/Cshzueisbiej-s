package com.example.core.finance

import kotlin.math.pow

data class CurrencyRate(
    val code: String,
    val name: String,
    val symbol: String,
    val rateToUsd: Double // 1 USD = rateToUsd units
)

data class CompoundInterestResult(
    val futureValue: Double,
    val totalPrincipal: Double,
    val totalInterest: Double,
    val yearlyBreakdown: List<YearlyGrowth>
)

data class YearlyGrowth(
    val year: Int,
    val balance: Double,
    val totalDeposited: Double,
    val interestEarned: Double
)

data class LoanResult(
    val monthlyPayment: Double,
    val totalPayment: Double,
    val totalInterest: Double
)

data class SplitBillResult(
    val tipAmount: Double,
    val totalWithTip: Double,
    val perPerson: Double
)

object FxEngine {

    val supportedCurrencies = listOf(
        CurrencyRate("USD", "US Dollar", "$", 1.00),
        CurrencyRate("EUR", "Euro", "€", 0.92),
        CurrencyRate("GBP", "British Pound", "£", 0.79),
        CurrencyRate("JPY", "Japanese Yen", "¥", 154.20),
        CurrencyRate("CHF", "Swiss Franc", "CHF", 0.89),
        CurrencyRate("CAD", "Canadian Dollar", "CA$", 1.37),
        CurrencyRate("AUD", "Australian Dollar", "A$", 1.52),
        CurrencyRate("CNY", "Chinese Yuan", "¥", 7.24),
        CurrencyRate("SGD", "Singapore Dollar", "S$", 1.34),
        CurrencyRate("INR", "Indian Rupee", "₹", 83.50),
        CurrencyRate("AED", "UAE Dirham", "AED", 3.67),
        CurrencyRate("BTC", "Bitcoin", "₿", 0.000015),
        CurrencyRate("ETH", "Ethereum", "Ξ", 0.00038)
    )

    fun convertCurrency(
        amount: Double,
        fromCode: String,
        toCode: String,
        spreadPercent: Double = 0.1
    ): Double {
        val fromRate = supportedCurrencies.find { it.code == fromCode }?.rateToUsd ?: 1.0
        val toRate = supportedCurrencies.find { it.code == toCode }?.rateToUsd ?: 1.0
        val usdAmount = amount / fromRate
        val rawConverted = usdAmount * toRate
        val spreadFactor = 1.0 - (spreadPercent / 100.0)
        return rawConverted * spreadFactor
    }

    fun calculateCompoundInterest(
        principal: Double,
        annualRatePercent: Double,
        years: Int,
        monthlyDeposit: Double = 0.0,
        compoundingPerYear: Int = 12
    ): CompoundInterestResult {
        val r = (annualRatePercent / 100.0) / compoundingPerYear
        val n = compoundingPerYear
        val yearlyList = mutableListOf<YearlyGrowth>()

        var currentBalance = principal
        var currentDeposited = principal

        for (year in 1..years) {
            for (month in 1..12) {
                currentBalance += monthlyDeposit
                currentDeposited += monthlyDeposit
                currentBalance *= (1.0 + (annualRatePercent / 100.0) / 12.0)
            }
            yearlyList.add(
                YearlyGrowth(
                    year = year,
                    balance = currentBalance,
                    totalDeposited = currentDeposited,
                    interestEarned = (currentBalance - currentDeposited).coerceAtLeast(0.0)
                )
            )
        }

        return CompoundInterestResult(
            futureValue = currentBalance,
            totalPrincipal = currentDeposited,
            totalInterest = (currentBalance - currentDeposited).coerceAtLeast(0.0),
            yearlyBreakdown = yearlyList
        )
    }

    fun calculateLoan(
        principal: Double,
        annualRatePercent: Double,
        years: Int
    ): LoanResult {
        if (principal <= 0 || years <= 0) {
            return LoanResult(0.0, 0.0, 0.0)
        }
        val monthlyRate = (annualRatePercent / 100.0) / 12.0
        val numMonths = years * 12
        if (monthlyRate == 0.0) {
            val monthly = principal / numMonths
            return LoanResult(monthly, principal, 0.0)
        }
        val factor = (1.0 + monthlyRate).pow(numMonths.toDouble())
        val monthlyPayment = principal * (monthlyRate * factor) / (factor - 1.0)
        val totalPayment = monthlyPayment * numMonths
        val totalInterest = totalPayment - principal

        return LoanResult(
            monthlyPayment = monthlyPayment,
            totalPayment = totalPayment,
            totalInterest = totalInterest.coerceAtLeast(0.0)
        )
    }

    fun calculateSplitBill(
        amount: Double,
        tipPercent: Double,
        guests: Int
    ): SplitBillResult {
        val people = guests.coerceAtLeast(1)
        val tip = amount * (tipPercent / 100.0)
        val total = amount + tip
        val perPerson = total / people
        return SplitBillResult(
            tipAmount = tip,
            totalWithTip = total,
            perPerson = perPerson
        )
    }
}

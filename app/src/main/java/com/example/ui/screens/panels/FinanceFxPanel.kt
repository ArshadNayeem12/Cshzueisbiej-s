package com.example.ui.screens.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.finance.CompoundInterestResult
import com.example.core.finance.FxEngine
import com.example.core.math.ExpressionEvaluator
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.*

enum class FinanceTab {
    CURRENCY,
    COMPOUND_INTEREST,
    LOAN_MORTGAGE,
    SPLIT_BILL
}

@Composable
fun FinanceFxPanel(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(FinanceTab.CURRENCY) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Tab Selector Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FinanceTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) LuxuryGold else ObsidianElevated)
                        .clickable { selectedTab = tab }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (tab) {
                            FinanceTab.CURRENCY -> "Forex FX"
                            FinanceTab.COMPOUND_INTEREST -> "Compound"
                            FinanceTab.LOAN_MORTGAGE -> "Loan"
                            FinanceTab.SPLIT_BILL -> "Split"
                        },
                        color = if (isSelected) OledBlack else TitaniumLight,
                        fontSize = 11.sp,
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        GlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("finance_fx_card"),
            shape = RoundedCornerShape(20.dp),
            accentGlowColor = LuxuryGold.copy(alpha = 0.25f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (selectedTab) {
                    FinanceTab.CURRENCY -> CurrencyConverterView()
                    FinanceTab.COMPOUND_INTEREST -> CompoundInterestView()
                    FinanceTab.LOAN_MORTGAGE -> LoanMortgageView()
                    FinanceTab.SPLIT_BILL -> SplitBillView()
                }
            }
        }
    }
}

@Composable
private fun CurrencyConverterView() {
    var amountStr by remember { mutableStateOf("1000") }
    var fromCode by remember { mutableStateOf("USD") }
    var toCode by remember { mutableStateOf("EUR") }

    val amount = amountStr.toDoubleOrNull() ?: 1000.0
    val converted = FxEngine.convertCurrency(amount, fromCode, toCode)

    Text("Forex Live Currency Engine", color = LuxuryGold, fontSize = 14.sp, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)

    OutlinedTextField(
        value = amountStr,
        onValueChange = { amountStr = it },
        label = { Text("Amount ($fromCode)", fontSize = 10.sp) },
        modifier = Modifier.fillMaxWidth(),
        colors = customFieldColors()
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        CurrencySelector(
            label = "From",
            selected = fromCode,
            onSelect = { fromCode = it },
            modifier = Modifier.weight(1f)
        )
        CurrencySelector(
            label = "To",
            selected = toCode,
            onSelect = { toCode = it },
            modifier = Modifier.weight(1f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianBase)
            .border(1.dp, LuxuryGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column {
            Text("CONVERTED VALUE (0.1% SPREAD)", color = BrushedTitanium, fontSize = 10.sp, fontFamily = JetBrainsMonoFontFamily)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${ExpressionEvaluator.formatDisplay(converted)} $toCode",
                color = LuxuryGold,
                fontSize = 22.sp,
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CurrencySelector(
    label: String,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TitaniumLight)
        ) {
            Text("$label: $selected", fontFamily = JetBrainsMonoFontFamily, fontSize = 12.sp)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(ObsidianElevated)
        ) {
            FxEngine.supportedCurrencies.forEach { c ->
                DropdownMenuItem(
                    text = { Text("${c.code} (${c.name})", color = TitaniumLight, fontSize = 12.sp) },
                    onClick = {
                        onSelect(c.code)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun CompoundInterestView() {
    var principalStr by remember { mutableStateOf("10000") }
    var rateStr by remember { mutableStateOf("8.5") }
    var yearsStr by remember { mutableStateOf("10") }
    var monthlyStr by remember { mutableStateOf("250") }

    val p = principalStr.toDoubleOrNull() ?: 10000.0
    val r = rateStr.toDoubleOrNull() ?: 8.5
    val y = (yearsStr.toIntOrNull() ?: 10).coerceIn(1, 40)
    val pmt = monthlyStr.toDoubleOrNull() ?: 250.0

    val res = FxEngine.calculateCompoundInterest(p, r, y, pmt)

    Text("Compound Interest & Wealth Growth", color = LuxuryGold, fontSize = 14.sp, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = principalStr, onValueChange = { principalStr = it }, label = { Text("Initial Principal ($)", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = rateStr, onValueChange = { rateStr = it }, label = { Text("Annual Rate (%)", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = yearsStr, onValueChange = { yearsStr = it }, label = { Text("Years (t)", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = monthlyStr, onValueChange = { monthlyStr = it }, label = { Text("Monthly Deposit ($)", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianBase)
            .border(1.dp, LuxuryGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column {
            Text("FUTURE PORTFOLIO BALANCE", color = BrushedTitanium, fontSize = 10.sp, fontFamily = JetBrainsMonoFontFamily)
            Text("$${ExpressionEvaluator.formatDisplay(res.futureValue)}", color = LuxuryGold, fontSize = 22.sp, fontFamily = JetBrainsMonoFontFamily, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Deposits: $${ExpressionEvaluator.formatDisplay(res.totalPrincipal)}", color = TitaniumLight, fontSize = 11.sp, fontFamily = SpaceGroteskFontFamily)
                Text("Interest: +$${ExpressionEvaluator.formatDisplay(res.totalInterest)}", color = EmeraldGreen, fontSize = 11.sp, fontFamily = SpaceGroteskFontFamily)
            }
        }
    }
}

@Composable
private fun LoanMortgageView() {
    var loanStr by remember { mutableStateOf("350000") }
    var rateStr by remember { mutableStateOf("6.2") }
    var yearsStr by remember { mutableStateOf("30") }

    val l = loanStr.toDoubleOrNull() ?: 350000.0
    val r = rateStr.toDoubleOrNull() ?: 6.2
    val y = (yearsStr.toIntOrNull() ?: 30).coerceIn(1, 50)
    val res = FxEngine.calculateLoan(l, r, y)

    Text("Loan & Mortgage Amortization", color = LaserCyan, fontSize = 14.sp, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = loanStr, onValueChange = { loanStr = it }, label = { Text("Loan Amount ($)", fontSize = 10.sp) }, modifier = Modifier.weight(1.5f), colors = customFieldColors())
        OutlinedTextField(value = rateStr, onValueChange = { rateStr = it }, label = { Text("Rate (%)", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = yearsStr, onValueChange = { yearsStr = it }, label = { Text("Years", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianBase)
            .border(1.dp, LaserCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column {
            Text("ESTIMATED MONTHLY PAYMENT", color = BrushedTitanium, fontSize = 10.sp, fontFamily = JetBrainsMonoFontFamily)
            Text("$${ExpressionEvaluator.formatDisplay(res.monthlyPayment)} / mo", color = LaserCyan, fontSize = 22.sp, fontFamily = JetBrainsMonoFontFamily, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Total Repayment: $${ExpressionEvaluator.formatDisplay(res.totalPayment)} (Interest: $${ExpressionEvaluator.formatDisplay(res.totalInterest)})", color = TitaniumLight, fontSize = 11.sp, fontFamily = SpaceGroteskFontFamily)
        }
    }
}

@Composable
private fun SplitBillView() {
    var billStr by remember { mutableStateOf("128.50") }
    var tipStr by remember { mutableStateOf("18") }
    var guestsStr by remember { mutableStateOf("4") }

    val b = billStr.toDoubleOrNull() ?: 128.50
    val t = tipStr.toDoubleOrNull() ?: 18.0
    val g = guestsStr.toIntOrNull() ?: 4
    val res = FxEngine.calculateSplitBill(b, t, g)

    Text("Split-Bill & Gratuity Calculator", color = WarmCopper, fontSize = 14.sp, fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold)

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = billStr, onValueChange = { billStr = it }, label = { Text("Bill Total ($)", fontSize = 10.sp) }, modifier = Modifier.weight(1.2f), colors = customFieldColors())
        OutlinedTextField(value = tipStr, onValueChange = { tipStr = it }, label = { Text("Tip (%)", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
        OutlinedTextField(value = guestsStr, onValueChange = { guestsStr = it }, label = { Text("Guests", fontSize = 10.sp) }, modifier = Modifier.weight(1f), colors = customFieldColors())
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianBase)
            .border(1.dp, WarmCopper.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column {
            Text("PER PERSON SHARE", color = BrushedTitanium, fontSize = 10.sp, fontFamily = JetBrainsMonoFontFamily)
            Text("$${ExpressionEvaluator.formatDisplay(res.perPerson)}", color = WarmCopper, fontSize = 22.sp, fontFamily = JetBrainsMonoFontFamily, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Total with Tip: $${ExpressionEvaluator.formatDisplay(res.totalWithTip)} (Tip: $${ExpressionEvaluator.formatDisplay(res.tipAmount)})", color = TitaniumLight, fontSize = 11.sp, fontFamily = SpaceGroteskFontFamily)
        }
    }
}

@Composable
private fun customFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TitaniumLight,
    unfocusedTextColor = TitaniumLight,
    focusedBorderColor = LuxuryGold,
    unfocusedBorderColor = GlassBorderHairline,
    focusedContainerColor = ObsidianBase,
    unfocusedContainerColor = ObsidianBase,
    focusedLabelColor = LuxuryGold,
    unfocusedLabelColor = BrushedTitanium
)

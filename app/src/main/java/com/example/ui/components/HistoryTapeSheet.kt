package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.core.math.CasSolution
import com.example.data.HistoryTapeItem
import com.example.ui.theme.*

@Composable
fun HistoryTapeSheet(
    historyItems: List<HistoryTapeItem>,
    onDismiss: () -> Unit,
    onReuse: (String) -> Unit,
    onViewProof: (CasSolution) -> Unit,
    onClearHistory: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.88f)
                    .testTag("history_tape_sheet"),
                shape = RoundedCornerShape(24.dp),
                accentGlowColor = BrushedTitanium
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CALCULATION HISTORY TAPE",
                                color = LuxuryGold,
                                fontSize = 11.sp,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Tape Log (${historyItems.size})",
                                color = TitaniumLight,
                                fontSize = 18.sp,
                                fontFamily = SpaceGroteskFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (historyItems.isNotEmpty()) {
                                IconButton(
                                    onClick = onClearHistory,
                                    modifier = Modifier.testTag("btn_clear_history")
                                ) {
                                    Icon(Icons.Default.DeleteSweep, "Clear History", tint = CrimsonRed)
                                }
                            }
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.testTag("btn_close_history")
                            ) {
                                Icon(Icons.Default.Close, "Close", tint = TitaniumLight)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (historyItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Infinite tape is currently empty.\nPast expressions and roots appear here.",
                                color = BrushedTitanium,
                                fontSize = 13.sp,
                                fontFamily = SpaceGroteskFontFamily,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(historyItems.reversed()) { item ->
                                HistoryRowView(
                                    item = item,
                                    onReuse = onReuse,
                                    onViewProof = onViewProof
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryRowView(
    item: HistoryTapeItem,
    onReuse: (String) -> Unit,
    onViewProof: (CasSolution) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ObsidianBase)
            .clickable { onReuse(item.result) }
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.mode.uppercase(),
                    color = BrushedTitanium,
                    fontSize = 10.sp,
                    fontFamily = JetBrainsMonoFontFamily
                )
                if (item.casSolution != null) {
                    IconButton(
                        onClick = { onViewProof(item.casSolution) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Psychology,
                            "View Proof",
                            tint = LuxuryGold,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.expression,
                color = BrushedTitanium,
                fontSize = 13.sp,
                fontFamily = JetBrainsMonoFontFamily
            )

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "= ${item.result}",
                    color = LuxuryGold,
                    fontSize = 18.sp,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Tap to reuse",
                    color = LaserCyan.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    fontFamily = SpaceGroteskFontFamily
                )
            }
        }
    }
}

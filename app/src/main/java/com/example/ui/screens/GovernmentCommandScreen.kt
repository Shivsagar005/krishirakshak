package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockAndDemoRepository
import com.example.model.AppLanguage
import com.example.model.RiskLevel
import com.example.ui.theme.*

@Composable
fun GovernmentCommandScreen(
    onNavigateToGis: () -> Unit
) {
    val hotspots by MockAndDemoRepository.hotspots.collectAsState()
    val language by MockAndDemoRepository.currentLanguage.collectAsState()

    var reportExported by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Government Command Header
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3B82F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = "Govt",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "National Crop Health Command Center",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Directorate of Plant Protection, Quarantine & Storage",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = "Agro-Intelligence Network · Live Telemetry",
                        fontSize = 10.sp,
                        color = Color(0xFF38BDF8)
                    )
                }
            }
        }

        // Top KPI Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GovtKpiCard("Farmers Monitored", "148,290", "+12% MoM", AgriGreenPrimary, Modifier.weight(1f))
            GovtKpiCard("Active Hotspots", "${hotspots.size} Clusters", "2 Critical", RiskCritical, Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GovtKpiCard("Scans Today", "3,842", "89% Accuracy", Color(0xFF2563EB), Modifier.weight(1f))
            GovtKpiCard("Verification Rate", "87.4%", "ICAR Certified", HarvestAmberDark, Modifier.weight(1f))
        }

        // Action banner to open GIS Map
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = AgriGreenDark,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable { onNavigateToGis() }
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Map",
                        tint = HarvestGold,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Interactive GIS Disease Hotspot Map",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Inspect regional infection buffers and drone telemetry",
                            fontSize = 10.sp,
                            color = TextSecondaryDark
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Go",
                    tint = HarvestGold,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Regional Outbreak Threat Matrix
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Regional Agricultural Threat Matrix",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                hotspots.forEach { hotspot ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${hotspot.regionDistrict} (${hotspot.state})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${hotspot.crop} · ${hotspot.threat}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when (hotspot.riskLevel) {
                                RiskLevel.CRITICAL -> RiskCritical.copy(alpha = 0.15f)
                                RiskLevel.HIGH -> RiskHigh.copy(alpha = 0.15f)
                                else -> RiskModerate.copy(alpha = 0.15f)
                            }
                        ) {
                            Text(
                                text = "${hotspot.caseCount} cases (${hotspot.riskLevel.name})",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (hotspot.riskLevel) {
                                    RiskLevel.CRITICAL -> RiskCritical
                                    RiskLevel.HIGH -> RiskHigh
                                    else -> RiskModerate
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                }
            }
        }

        // Policy & Operational Directives
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Priority Government Directives & Advisory",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                DirectiveRow(
                    title = "Release Subsidized Bio-Defense Kits",
                    desc = "Authorize KVKs in Barabanki and Nashik to dispense Trichoderma viride & sticky traps.",
                    isCritical = true
                )
                DirectiveRow(
                    title = "Broadcast Agro-Met Early Warning",
                    desc = "Push automated SMS alerts to 45,000 farmers in rainfall belt regarding Alternaria blight prevention.",
                    isCritical = false
                )
                DirectiveRow(
                    title = "Strict Pesticide Dosage Regulation",
                    desc = "Enforce ban on unauthorized synthetic pyrethroid usage in cotton zones to prevent whitefly resurgence.",
                    isCritical = false
                )
            }
        }

        // Export Report Button
        Button(
            onClick = { reportExported = true },
            colors = ButtonDefaults.buttonColors(containerColor = if (reportExported) AgriGreenMedium else Color(0xFF1E293B)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(
                imageVector = if (reportExported) Icons.Default.Check else Icons.Default.Download,
                contentDescription = "Export",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (reportExported) "✓ National Crop Health Report Exported (PDF & CSV)" else "Export National Agricultural Health Dossier (PDF)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun GovtKpiCard(
    title: String,
    value: String,
    subText: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subText, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = AgriGreenPrimary)
        }
    }
}

@Composable
private fun DirectiveRow(title: String, desc: String, isCritical: Boolean) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isCritical) RiskHigh.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isCritical) Icons.Default.PriorityHigh else Icons.Default.CheckCircle,
                    contentDescription = "Priority",
                    tint = if (isCritical) RiskHigh else AgriGreenPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

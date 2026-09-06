package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockAndDemoRepository
import com.example.model.AppLanguage
import com.example.model.HotspotCluster
import com.example.model.RiskLevel
import com.example.ui.theme.*

@Composable
fun GisHotspotMapScreen() {
    val hotspots by MockAndDemoRepository.hotspots.collectAsState()
    val language by MockAndDemoRepository.currentLanguage.collectAsState()

    var selectedFilter by remember { mutableStateOf("All") }
    var selectedHotspot by remember { mutableStateOf(hotspots.firstOrNull()) }
    var alertBroadcastSent by remember { mutableStateOf(false) }

    val filteredHotspots = remember(hotspots, selectedFilter) {
        if (selectedFilter == "All") hotspots
        else hotspots.filter { it.crop.contains(selectedFilter, ignoreCase = true) || it.threat.contains(selectedFilter, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (language == AppLanguage.HINDI) "जीआईएस रोग व कीट हॉटस्पॉट मानचित्र" else "GIS Outbreak Hotspot Network",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (language == AppLanguage.HINDI) "राष्ट्रीय कृषि निगरानी एवं क्लस्टर विश्लेषण" else "Regional disease clustering & containment intelligence",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = AgriGreenContainer
            ) {
                Text(
                    text = "${hotspots.size} Active Clusters",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenDark,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Filter Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("All", "Tomato", "Rice", "Cotton", "Wheat").forEach { crop ->
                val isSelected = selectedFilter == crop
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) AgriGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { selectedFilter = crop }
                ) {
                    Text(
                        text = crop,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // 2D Interactive GIS Map Canvas Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2417)),
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .border(2.dp, AgriGreenPrimary, RoundedCornerShape(20.dp))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Map Background Grid & State Boundaries Drawing
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Grid lines
                    for (i in 1..6) {
                        drawLine(
                            color = Color(0xFF1B3D28),
                            start = Offset(0f, (h / 7) * i),
                            end = Offset(w, (h / 7) * i),
                            strokeWidth = 1f
                        )
                    }
                    for (i in 1..8) {
                        drawLine(
                            color = Color(0xFF1B3D28),
                            start = Offset((w / 9) * i, 0f),
                            end = Offset((w / 9) * i, h),
                            strokeWidth = 1f
                        )
                    }

                    // Stylized Indo-Gangetic & Central India agro-climatic corridor
                    val corridorPath = Path().apply {
                        moveTo(w * 0.15f, h * 0.25f) // Punjab / Haryana
                        cubicTo(w * 0.4f, h * 0.35f, w * 0.6f, h * 0.4f, w * 0.85f, h * 0.45f) // UP / Bihar
                        lineTo(w * 0.7f, h * 0.75f) // MP
                        lineTo(w * 0.3f, h * 0.8f) // Maharashtra
                        close()
                    }
                    drawPath(
                        corridorPath,
                        color = Color(0xFF143621),
                        style = Stroke(width = 2f)
                    )
                }

                // Interactive Hotspot Pins on Map
                Box(modifier = Modifier.fillMaxSize()) {
                    filteredHotspots.forEachIndexed { index, hotspot ->
                        // Determine coordinate placement on 2D map
                        val xFraction = when (hotspot.regionDistrict) {
                            "Barabanki" -> 0.58f
                            "Sitapur" -> 0.48f
                            "Raebareli" -> 0.55f
                            "Ludhiana" -> 0.22f
                            "Nashik" -> 0.32f
                            else -> 0.5f
                        }
                        val yFraction = when (hotspot.regionDistrict) {
                            "Barabanki" -> 0.42f
                            "Sitapur" -> 0.32f
                            "Raebareli" -> 0.54f
                            "Ludhiana" -> 0.22f
                            "Nashik" -> 0.72f
                            else -> 0.5f
                        }

                        val pinColor = when (hotspot.riskLevel) {
                            RiskLevel.CRITICAL -> RiskCritical
                            RiskLevel.HIGH -> RiskHigh
                            RiskLevel.MODERATE -> RiskModerate
                            else -> RiskSafe
                        }

                        val isSelected = selectedHotspot?.id == hotspot.id

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = (xFraction * 280).dp,
                                    top = (yFraction * 220).dp
                                )
                        ) {
                            // Pulsating / Selected Outbreak Ring
                            Box(
                                modifier = Modifier
                                    .size(if (isSelected) 36.dp else 24.dp)
                                    .clip(CircleShape)
                                    .background(pinColor.copy(alpha = if (isSelected) 0.35f else 0.2f))
                                    .clickable { selectedHotspot = hotspot },
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(if (isSelected) 16.dp else 12.dp)
                                        .clip(CircleShape)
                                        .background(pinColor)
                                )
                            }
                        }
                    }
                }

                // Map Legend Overlay
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LegendChip("Critical", RiskCritical)
                        LegendChip("High", RiskHigh)
                        LegendChip("Moderate", RiskModerate)
                    }
                }

                // Instructions pill
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Tap pin to inspect",
                        fontSize = 9.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // Hotspot Inspection Detail Card
        selectedHotspot?.let { hotspot ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "District",
                                    tint = AgriGreenPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${hotspot.regionDistrict} District, ${hotspot.state}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "Threat: ${hotspot.threat}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RiskHigh
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (hotspot.riskLevel) {
                                RiskLevel.CRITICAL -> RiskCritical.copy(alpha = 0.15f)
                                RiskLevel.HIGH -> RiskHigh.copy(alpha = 0.15f)
                                else -> RiskModerate.copy(alpha = 0.15f)
                            }
                        ) {
                            Text(
                                text = hotspot.riskLevel.name,
                                color = when (hotspot.riskLevel) {
                                    RiskLevel.CRITICAL -> RiskCritical
                                    RiskLevel.HIGH -> RiskHigh
                                    else -> RiskModerate
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Cluster Statistics Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatPill("Crop Monitored", hotspot.crop, AgriGreenPrimary)
                        StatPill("Reported Cases", "${hotspot.caseCount}", RiskHigh)
                        StatPill("Expert Verified", "${hotspot.verifiedRatioPct}%", AgriGreenPrimary)
                        StatPill("Cluster Trend", hotspot.outbreakTrend, HarvestAmberDark)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Operational Action Directive
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = "Directive",
                                    tint = AgriGreenDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.HINDI) "अनुशंसित प्रशासनिक कार्रवाई:" else "Suggested Government Operational Directive:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriGreenDark
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hotspot.operationalAction,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Broadcast Early Warning Alert Action
                    Button(
                        onClick = { alertBroadcastSent = true },
                        enabled = !alertBroadcastSent,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (alertBroadcastSent) AgriGreenMedium else HarvestAmberDark
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (alertBroadcastSent) Icons.Default.Check else Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (alertBroadcastSent)
                                (if (language == AppLanguage.HINDI) "चेतावनी संदेश सभी पंजीकृत किसानों को प्रसारित!" else "Early Warning SMS & App Alert Broadcasted to ${hotspot.regionDistrict} Farmers!")
                            else
                                (if (language == AppLanguage.HINDI) "हॉटस्पॉट क्षेत्र में किसानों को प्रारंभिक चेतावनी भेजें" else "Broadcast Early Warning to ${hotspot.regionDistrict} Farmers"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendChip(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 9.sp, color = Color.White)
    }
}

@Composable
private fun StatPill(title: String, value: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.08f),
        modifier = Modifier.padding(2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Text(text = title, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

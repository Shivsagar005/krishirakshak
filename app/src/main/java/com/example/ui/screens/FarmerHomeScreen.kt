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
import com.example.model.*
import com.example.ui.components.CropAnatomyViewer
import com.example.ui.components.RiskGaugeCard
import com.example.ui.components.WeatherBanner
import com.example.ui.theme.*

@Composable
fun FarmerHomeScreen(
    onNavigateToScan: () -> Unit,
    onNavigateToChat: (String?) -> Unit,
    onNavigateToGis: () -> Unit,
    onNavigateToIot: () -> Unit
) {
    val language by MockAndDemoRepository.currentLanguage.collectAsState()
    val crops by MockAndDemoRepository.crops.collectAsState()
    val alerts by MockAndDemoRepository.alerts.collectAsState()
    val scans by MockAndDemoRepository.scans.collectAsState()
    val iot by MockAndDemoRepository.iotData.collectAsState()

    var showAddCropDialog by remember { mutableStateOf(false) }
    var newCropName by remember { mutableStateOf("") }
    var newCropVariety by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Farmer Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (language == AppLanguage.HINDI) "नमस्ते, रमेश कुमार जी 🙏" else "Namaste, Ramesh Kumar ji 🙏",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (language == AppLanguage.HINDI)
                        "सूर्या एग्रो फार्म (3.5 एकड़) · लखनऊ, उत्तर प्रदेश"
                    else
                        "Surya Agro Farm (3.5 Acres) · Lucknow, Uttar Pradesh",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                shape = CircleShape,
                color = AgriGreenContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Agriculture,
                        contentDescription = "Farm",
                        tint = AgriGreenDark,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Live Microclimate Banner
        WeatherBanner(
            onDetectGps = { /* GPS detected */ }
        )

        // Primary 2D Circular Crop Risk Gauge (Multi-factor: score 78 / 100)
        RiskGaugeCard(
            riskScore = 78,
            cropName = "Tomato (Flourishing)",
            onOpenScanner = onNavigateToScan
        )

        // Dominant Primary 2D Scan CTA Button
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = AgriGreenPrimary,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { onNavigateToScan() }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Scan",
                            tint = HarvestGold,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.HINDI) "फसल पत्ती स्कैन करें" else "Scan Crop Foliage",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = if (language == AppLanguage.HINDI) "त्वरित रोग पहचान एवं एकीकृत सलाह" else "Instant Vision AI diagnosis + IPM protocol",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Go",
                    tint = HarvestGold,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // 2D Plant Anatomical Risk Model & Organ Viewer
        CropAnatomyViewer(
            onScanCropPart = { _ -> onNavigateToScan() }
        )

        // Active Early Warning Card
        alerts.firstOrNull()?.let { alert ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RiskHigh.copy(alpha = 0.08f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, RiskHigh.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Alert",
                                tint = RiskHigh,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) alert.titleHindi else alert.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = RiskHigh
                            )
                        }
                        Text(text = alert.timestamp, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) alert.messageHindi else alert.message,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = AgriGreenPrimary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                onNavigateToChat("How should I prepare for the forecasted ${alert.title}?")
                            }
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "सुझाव व रोकथाम देखें" else "View Advisory Action",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // My Crops Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.HINDI) "मेरी फसलें (Active Crops)" else "Monitored Crops",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(onClick = { showAddCropDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "फसल जोड़ें" else "Add Crop",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            crops.forEach { crop ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${crop.cropName} (${crop.localHindiName})",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = AgriGreenContainer
                                ) {
                                    Text(
                                        text = crop.stage.labelEn,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriGreenDark,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Variety: ${crop.variety} · Sown: ${crop.sowingDate}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (crop.riskScore >= 70) RiskHigh.copy(alpha = 0.15f) else AgriGreenContainer
                        ) {
                            Text(
                                text = "Risk: ${crop.riskScore}/100",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (crop.riskScore >= 70) RiskHigh else AgriGreenDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // Quick Telemetry & Hotspot Radar Preview
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // IoT Trap Mini Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToIot() }
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.PestControl, contentDescription = "Pest", tint = RiskHigh, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Smart Trap", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${iot.whiteflyTrapCount} flies", fontSize = 16.sp, fontWeight = FontWeight.Black, color = RiskHigh)
                    Text(text = "Threshold 15 (Alert)", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // GIS Hotspot Mini Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToGis() }
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "GIS", tint = AgriGreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Nearby Cluster", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Barabanki", fontSize = 14.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = "18 km away (Critical)", fontSize = 9.sp, color = RiskHigh)
                }
            }
        }

        // Recent Scans List
        Text(
            text = if (language == AppLanguage.HINDI) "हालिया स्कैन इतिहास" else "Recent Diagnostic Records",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        scans.take(2).forEach { scan ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${scan.cropName} - ${scan.detectedIssue}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when (scan.status) {
                                ScanStatus.EXPERT_VERIFIED -> AgriGreenContainer
                                ScanStatus.PENDING_EXPERT -> HarvestGoldLight
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ) {
                            Text(
                                text = scan.status.labelEn,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (scan.status) {
                                    ScanStatus.EXPERT_VERIFIED -> AgriGreenDark
                                    ScanStatus.PENDING_EXPERT -> HarvestAmberDark
                                    else -> MaterialTheme.colorScheme.onSurface
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Scanned: ${scan.dateFormatted} · Risk: ${scan.riskScore}/100",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    // Add Crop Dialog
    if (showAddCropDialog) {
        AlertDialog(
            onDismissRequest = { showAddCropDialog = false },
            title = { Text("Add Crop to Monitoring") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCropName,
                        onValueChange = { newCropName = it },
                        label = { Text("Crop Name (e.g. Potato, Mustard)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCropVariety,
                        onValueChange = { newCropVariety = it },
                        label = { Text("Variety (e.g. Kufri Jyoti)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCropName.isNotBlank()) {
                            MockAndDemoRepository.addCrop(
                                farmId = "farm-1",
                                cropName = newCropName,
                                hindiName = newCropName,
                                variety = newCropVariety.ifBlank { "Standard High-Yield" },
                                stage = CropStage.VEGETATIVE
                            )
                            showAddCropDialog = false
                            newCropName = ""
                            newCropVariety = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text("Add Crop")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCropDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

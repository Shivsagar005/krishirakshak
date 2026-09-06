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
import com.example.model.ScanResult
import com.example.model.ScanStatus
import com.example.ui.theme.*

@Composable
fun ExpertReviewScreen() {
    val scans by MockAndDemoRepository.scans.collectAsState()
    val language by MockAndDemoRepository.currentLanguage.collectAsState()

    var selectedTab by remember { mutableStateOf("Pending") }
    val pendingScans = scans.filter { it.status == ScanStatus.PENDING_EXPERT }
    val verifiedScans = scans.filter { it.status == ScanStatus.EXPERT_VERIFIED }

    var activeScanToReview by remember { mutableStateOf<ScanResult?>(pendingScans.firstOrNull()) }
    var expertNotesInput by remember { mutableStateOf("Concentric zonate ring pattern confirms Alternaria solani. Advised foliar bio-protective spray and basal leaf defoliation.") }
    var actionSuccessMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Expert Header Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D253A)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E88E5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Science,
                        contentDescription = "Expert",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Dr. Priya Sharma (ICAR-IARI)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Principal Plant Pathologist · Central Review Desk",
                        fontSize = 11.sp,
                        color = Color(0xFF90CAF9)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Lucknow & Indo-Gangetic Agro-Climatic Zone",
                        fontSize = 10.sp,
                        color = Color(0xFFB0BEC5)
                    )
                }
            }
        }

        // Tab Selector Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TabPill(
                title = "Pending Queue (${pendingScans.size})",
                isSelected = selectedTab == "Pending",
                onClick = { selectedTab = "Pending" },
                modifier = Modifier.weight(1f)
            )
            TabPill(
                title = "Verified Archive (${verifiedScans.size})",
                isSelected = selectedTab == "Verified",
                onClick = { selectedTab = "Verified" },
                modifier = Modifier.weight(1f)
            )
        }

        // Review Queue Card List
        if (selectedTab == "Pending") {
            if (pendingScans.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Clear",
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Queue Cleared!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "All farmer scans in this regional cluster have been validated.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                pendingScans.forEach { scan ->
                    CaseReviewCard(
                        scan = scan,
                        isSelected = activeScanToReview?.id == scan.id,
                        onSelect = { activeScanToReview = scan }
                    )
                }
            }
        } else {
            verifiedScans.forEach { scan ->
                CaseReviewCard(
                    scan = scan,
                    isSelected = false,
                    onSelect = {}
                )
            }
        }

        // Action / Verification Workspace Drawer
        activeScanToReview?.let { scan ->
            if (scan.status == ScanStatus.PENDING_EXPERT) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, HarvestGold, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Reviewing Case #${scan.id.takeLast(5)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = HarvestGoldLight
                            ) {
                                Text(
                                    text = "AI Match: ${(scan.confidence * 100).toInt()}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HarvestAmberDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Farmer: Ramesh Kumar · ${scan.farmName} (${scan.locationName})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Crop: ${scan.cropName} · Candidate: ${scan.detectedIssue} (${scan.scientificName})",
                            fontSize = 12.sp,
                            color = AgriGreenPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Clinical notes input
                        OutlinedTextField(
                            value = expertNotesInput,
                            onValueChange = { expertNotesInput = it },
                            label = { Text("Expert Clinical & Extension Advisory Notes") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Expert Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Approve Button
                            Button(
                                onClick = {
                                    MockAndDemoRepository.expertValidateScan(
                                        scanId = scan.id,
                                        approved = true,
                                        notes = expertNotesInput
                                    )
                                    actionSuccessMessage = "Case #${scan.id.takeLast(5)} verified and certified by Dr. Priya Sharma!"
                                    activeScanToReview = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Approve",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Approve Diagnosis", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            // Request Retake Button
                            OutlinedButton(
                                onClick = {
                                    MockAndDemoRepository.expertValidateScan(
                                        scanId = scan.id,
                                        approved = false,
                                        notes = "Image blurry. Please take a clear close-up of lower leaf undersides."
                                    )
                                    actionSuccessMessage = "Requested farmer to retake photo with clearer focus."
                                    activeScanToReview = null
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Retake",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Request Retake", fontSize = 11.sp)
                            }
                        }

                        // Order KVK Inspection button
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                actionSuccessMessage = "Mobile KVK plant pathology squad scheduled for ${scan.farmName}."
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Agriculture,
                                contentDescription = "Inspection",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Schedule On-Site KVK Squad Inspection", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Notification Message Toast
        actionSuccessMessage?.let { msg ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = AgriGreenContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = AgriGreenDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = msg,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenDark
                    )
                }
            }
        }
    }
}

@Composable
private fun TabPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) AgriGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CaseReviewCard(
    scan: ScanResult,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) HarvestGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onSelect() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${scan.cropName}: ${scan.detectedIssue}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${scan.farmName} · ${scan.dateFormatted}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (scan.status) {
                        ScanStatus.EXPERT_VERIFIED -> AgriGreenContainer
                        ScanStatus.PENDING_EXPERT -> HarvestGoldLight
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = scan.status.labelEn,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (scan.status) {
                            ScanStatus.EXPERT_VERIFIED -> AgriGreenDark
                            ScanStatus.PENDING_EXPERT -> HarvestAmberDark
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Symptoms: ${scan.symptoms.firstOrNull() ?: "Visual necrosis"}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            scan.expertNotes?.let { notes ->
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = AgriGreenContainer.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Expert Note: $notes",
                        fontSize = 10.sp,
                        color = AgriGreenDark,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }
}

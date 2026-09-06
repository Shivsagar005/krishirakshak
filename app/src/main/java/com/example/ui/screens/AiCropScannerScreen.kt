package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockAndDemoRepository
import com.example.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AiCropScannerScreen(
    onNavigateToChatWithContext: (String) -> Unit,
    onNavigateToGis: () -> Unit,
    onNavigateToExpertQueue: () -> Unit
) {
    val language by MockAndDemoRepository.currentLanguage.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var selectedSample by remember { mutableStateOf("tomato_blight") }
    var isAnalyzing by remember { mutableStateOf(false) }
    var analysisStep by remember { mutableIntStateOf(0) }
    var activeResult by remember { mutableStateOf<ScanResult?>(null) }
    var submittedToExpert by remember { mutableStateOf(false) }

    // Scan line animation
    val infiniteTransition = rememberInfiniteTransition(label = "scanLine")
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanY"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (language == AppLanguage.HINDI) "एआई फसल स्वास्थ्य स्कैनर" else "AI Crop Health Scanner",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (language == AppLanguage.HINDI)
                        "पत्ती का रोग पहचाने एवं जोखिम भविष्यवाणी प्राप्त करें"
                    else
                        "Symptom detection + multi-factor outbreak risk assessment",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = AgriGreenContainer
            ) {
                Text(
                    text = "Vision 3.5 AI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenDark,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // 2D Camera Viewfinder Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1A13)),
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .border(2.dp, AgriGreenPrimary, RoundedCornerShape(20.dp))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Viewfinder Reticle & Scan Laser
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Corner brackets
                    val bracketLen = 36f
                    val stroke = 5f
                    val pad = 30f

                    // Top Left
                    drawLine(Color(0xFF81C784), Offset(pad, pad), Offset(pad + bracketLen, pad), stroke)
                    drawLine(Color(0xFF81C784), Offset(pad, pad), Offset(pad, pad + bracketLen), stroke)

                    // Top Right
                    drawLine(Color(0xFF81C784), Offset(w - pad, pad), Offset(w - pad - bracketLen, pad), stroke)
                    drawLine(Color(0xFF81C784), Offset(w - pad, pad), Offset(w - pad, pad + bracketLen), stroke)

                    // Bottom Left
                    drawLine(Color(0xFF81C784), Offset(pad, h - pad), Offset(pad + bracketLen, h - pad), stroke)
                    drawLine(Color(0xFF81C784), Offset(pad, h - pad), Offset(pad, h - pad - bracketLen), stroke)

                    // Bottom Right
                    drawLine(Color(0xFF81C784), Offset(w - pad, h - pad), Offset(w - pad - bracketLen, h - pad), stroke)
                    drawLine(Color(0xFF81C784), Offset(w - pad, h - pad), Offset(w - pad, h - pad - bracketLen), stroke)

                    // Animated laser line when analyzing
                    if (isAnalyzing) {
                        val currentY = h * scanLineY
                        drawLine(
                            color = Color(0xFF00E676),
                            start = Offset(pad, currentY),
                            end = Offset(w - pad, currentY),
                            strokeWidth = 4f
                        )
                    }
                }

                // Center leaf graphic / status
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(
                        imageVector = when (selectedSample) {
                            "tomato_blight" -> Icons.Default.FilterVintage
                            "tomato_curl" -> Icons.Default.Coronavirus
                            "cotton_pest" -> Icons.Default.PestControl
                            else -> Icons.Default.Grass
                        },
                        contentDescription = "Specimen",
                        tint = when (selectedSample) {
                            "tomato_blight" -> Color(0xFFFFB74D)
                            "tomato_curl" -> Color(0xFFFF8A65)
                            "cotton_pest" -> Color(0xFFEF5350)
                            else -> Color(0xFF81C784)
                        },
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = when (selectedSample) {
                            "tomato_blight" -> "Specimen: Solanum lycopersicum (Tomato Leaf)"
                            "tomato_curl" -> "Specimen: Tomato Shoot with Leaf Curl"
                            "cotton_pest" -> "Specimen: Cotton Leaf with Whitefly Colony"
                            else -> "Specimen: Oryza sativa (Healthy Rice)"
                        },
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isAnalyzing) "Analysing multi-spectral symptom vectors..." else "Keep affected leaf inside frame · Good natural light",
                        color = TextSecondaryDark,
                        fontSize = 10.sp
                    )
                }

                // Image Quality Pre-Check Overlay Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QualityIndicator("Lighting", true)
                        QualityIndicator("Focus", true)
                        QualityIndicator("Framing", true)
                    }
                }
            }
        }

        // Sample Leaf Picker Row (for instant testing)
        Column {
            Text(
                text = if (language == AppLanguage.HINDI) "परीक्षण हेतु पत्ती नमूना चुनें:" else "Select Crop Specimen to Scan:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SampleChip(
                    label = "🍅 Early Blight",
                    isSelected = selectedSample == "tomato_blight",
                    onClick = {
                        selectedSample = "tomato_blight"
                        activeResult = null
                        submittedToExpert = false
                    },
                    modifier = Modifier.weight(1f)
                )
                SampleChip(
                    label = "🍃 Leaf Curl",
                    isSelected = selectedSample == "tomato_curl",
                    onClick = {
                        selectedSample = "tomato_curl"
                        activeResult = null
                        submittedToExpert = false
                    },
                    modifier = Modifier.weight(1f)
                )
                SampleChip(
                    label = "🐛 Whitefly",
                    isSelected = selectedSample == "cotton_pest",
                    onClick = {
                        selectedSample = "cotton_pest"
                        activeResult = null
                        submittedToExpert = false
                    },
                    modifier = Modifier.weight(1f)
                )
                SampleChip(
                    label = "🌾 Healthy",
                    isSelected = selectedSample == "rice_healthy",
                    onClick = {
                        selectedSample = "rice_healthy"
                        activeResult = null
                        submittedToExpert = false
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Action Trigger Button: "Run AI Crop Scan"
        Button(
            onClick = {
                if (!isAnalyzing) {
                    isAnalyzing = true
                    activeResult = null
                    submittedToExpert = false
                    coroutineScope.launch {
                        // Truthful 6-step progress pipeline simulation
                        for (step in 1..6) {
                            analysisStep = step
                            delay(400)
                        }
                        isAnalyzing = false
                        activeResult = when (selectedSample) {
                            "tomato_blight" -> MockAndDemoRepository.submitScan(
                                cropName = "Tomato",
                                cropHindiName = "टमाटर",
                                detectedIssue = "Early Blight",
                                issueHindi = "अगेती झुलसा रोग (अर्ली ब्लाइट)",
                                category = ScanCategory.DISEASE,
                                confidence = 0.89f,
                                severity = SeverityLevel.MODERATE,
                                symptoms = listOf(
                                    "Dark brown concentric rings ('target' spots) on lower foliage",
                                    "Yellow chlorotic halo around lesions",
                                    "Basal leaf senescence"
                                ),
                                sampleKey = "tomato_early_blight"
                            )
                            "tomato_curl" -> MockAndDemoRepository.submitScan(
                                cropName = "Tomato",
                                cropHindiName = "टमाटर",
                                detectedIssue = "Tomato Leaf Curl Virus (ToLCV)",
                                issueHindi = "पर्ण कुंचन विषाणु रोग",
                                category = ScanCategory.DISEASE,
                                confidence = 0.92f,
                                severity = SeverityLevel.SEVERE,
                                symptoms = listOf(
                                    "Upward curling and crinkling of young leaves",
                                    "Stunted plant growth and bushy appearance",
                                    "Interveinal chlorosis and reduced flowering"
                                ),
                                sampleKey = "tomato_curl"
                            )
                            "cotton_pest" -> MockAndDemoRepository.submitScan(
                                cropName = "Cotton",
                                cropHindiName = "कपास",
                                detectedIssue = "Whitefly Infestation",
                                issueHindi = "सफेद मक्खी प्रकोप",
                                category = ScanCategory.PEST,
                                confidence = 0.86f,
                                severity = SeverityLevel.SEVERE,
                                symptoms = listOf(
                                    "Clusters of sucking nymphs on leaf underside",
                                    "Honeydew secretion promoting sooty mold",
                                    "Curling and leaf drop"
                                ),
                                sampleKey = "cotton_pest"
                            )
                            else -> MockAndDemoRepository.submitScan(
                                cropName = "Rice",
                                cropHindiName = "धान",
                                detectedIssue = "Healthy Foliage",
                                issueHindi = "स्वस्थ फसल (कोई रोग नहीं)",
                                category = ScanCategory.HEALTHY,
                                confidence = 0.95f,
                                severity = SeverityLevel.MILD,
                                symptoms = listOf("Vigorous green blades", "No blast or sheath blight", "Uniform tillering"),
                                sampleKey = "rice_healthy"
                            )
                        }
                    }
                }
            },
            enabled = !isAnalyzing,
            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (isAnalyzing) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = when (analysisStep) {
                        1 -> "1. Checking image quality & focus..."
                        2 -> "2. Identifying crop genus & species..."
                        3 -> "3. Detecting visible necrosis & symptoms..."
                        4 -> "4. Matching pathogen signatures..."
                        5 -> "5. Computing weather & stage risk..."
                        else -> "6. Formulating IPM advisory..."
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
            } else {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Scan",
                    tint = HarvestGold,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "फसल स्कैन व जोखिम विश्लेषण करें" else "Scan Specimen & Predict Risk",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }

        // Result Card View
        activeResult?.let { scan ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, if (scan.riskScore >= 70) RiskHigh else AgriGreenPrimary, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Result Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = if (language == AppLanguage.HINDI) "निदान परिणाम (Diagnosis)" else "Diagnostic Assessment",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) scan.issueHindi else scan.detectedIssue,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = scan.scientificName,
                                fontSize = 12.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (scan.riskLevel) {
                                RiskLevel.CRITICAL -> RiskCritical.copy(alpha = 0.15f)
                                RiskLevel.HIGH -> RiskHigh.copy(alpha = 0.15f)
                                RiskLevel.MODERATE -> RiskModerate.copy(alpha = 0.15f)
                                else -> RiskSafe.copy(alpha = 0.15f)
                            }
                        ) {
                            Text(
                                text = if (language == AppLanguage.HINDI) scan.riskLevel.labelHi else scan.riskLevel.labelEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = when (scan.riskLevel) {
                                    RiskLevel.CRITICAL -> RiskCritical
                                    RiskLevel.HIGH -> RiskHigh
                                    RiskLevel.MODERATE -> RiskModerate
                                    else -> RiskSafe
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Key Metric Badges (Confidence, Severity, Risk Score)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ResultMetricBox(
                            title = if (language == AppLanguage.HINDI) "एआई विश्वास" else "AI Confidence",
                            value = "${(scan.confidence * 100).toInt()}%",
                            color = AgriGreenPrimary
                        )
                        ResultMetricBox(
                            title = if (language == AppLanguage.HINDI) "गंभीरता" else "Severity",
                            value = scan.severity.name,
                            color = if (scan.severity == SeverityLevel.SEVERE || scan.severity == SeverityLevel.CRITICAL) RiskCritical else RiskModerate
                        )
                        ResultMetricBox(
                            title = if (language == AppLanguage.HINDI) "कुल जोखिम" else "Risk Index",
                            value = "${scan.riskScore}/100",
                            color = if (scan.riskScore >= 70) RiskHigh else AgriGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contributing Risk Signals Breakdown (Explainable AI)
                    Text(
                        text = if (language == AppLanguage.HINDI) "जोखिम का मुख्य कारण (Explainable Factors):" else "Risk Contributors (Environmental & Biological):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        scan.contributingFactors.forEach { factor ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (language == AppLanguage.HINDI) factor.factorNameHindi else factor.factorName,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = factor.valueText,
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = "+${factor.contributionPct}%",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 11.sp,
                                        color = if (factor.isWarning) RiskHigh else AgriGreenPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // IPM Advisory Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AgriGreenContainer.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Eco,
                                    contentDescription = "IPM",
                                    tint = AgriGreenDark,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.HINDI) "एकीकृत कीट प्रबंधन (IPM) सलाह" else "Actionable IPM Advisory",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriGreenDark
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• Immediate: ${scan.ipmAdvisory.immediateAction}",
                                fontSize = 11.sp,
                                color = AgriGreenDark,
                                lineHeight = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Biological: ${scan.ipmAdvisory.biologicalControl}",
                                fontSize = 11.sp,
                                color = AgriGreenDark,
                                lineHeight = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Caution: ${scan.ipmAdvisory.chemicalCaution}",
                                fontSize = 10.sp,
                                color = Color(0xFF7A3E00),
                                lineHeight = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Next Steps Button Group
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Ask Krishi AI button
                        OutlinedButton(
                            onClick = {
                                onNavigateToChatWithContext("I scanned my ${scan.cropName} and detected ${scan.detectedIssue} (Risk: ${scan.riskScore}/100). What organic treatments should I apply?")
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "Chat",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "कृषि AI से पूछें" else "Ask Krishi AI",
                                fontSize = 11.sp
                            )
                        }

                        // Submit to Expert Button
                        Button(
                            onClick = {
                                submittedToExpert = true
                            },
                            enabled = !submittedToExpert,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (submittedToExpert) Color.Gray else HarvestAmberDark
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (submittedToExpert) Icons.Default.Check else Icons.Default.VerifiedUser,
                                contentDescription = "Expert",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (submittedToExpert)
                                    (if (language == AppLanguage.HINDI) "समीक्षा हेतु प्रेषित" else "Sent to Expert")
                                else
                                    (if (language == AppLanguage.HINDI) "विशेषज्ञ सत्यापन" else "Send to Expert"),
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }

                    if (submittedToExpert) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE8F5E9),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (language == AppLanguage.HINDI)
                                    "✓ मामला डॉ. प्रिया शर्मा (ICAR विस्तार अधिकारी) के समीक्षा कक्ष में भेज दिया गया है।"
                                else
                                    "✓ Dispatched to Dr. Priya Sharma (ICAR-IARI Extension Desk) for validation.",
                                fontSize = 11.sp,
                                color = AgriGreenPrimary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QualityIndicator(label: String, isGood: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(if (isGood) Color(0xFF00E676) else Color(0xFFFF5252))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SampleChip(
    label: String,
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
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ResultMetricBox(
    title: String,
    value: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.1f),
        modifier = Modifier.padding(2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(text = title, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}

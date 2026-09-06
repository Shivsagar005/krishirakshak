package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockAndDemoRepository
import com.example.model.AppLanguage
import com.example.model.RiskLevel
import com.example.ui.theme.*

@Composable
fun RiskGaugeCard(
    riskScore: Int = 78,
    cropName: String = "Tomato (Flourishing)",
    onOpenScanner: () -> Unit
) {
    val language by MockAndDemoRepository.currentLanguage.collectAsState()
    var showExplanationDialog by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = riskScore / 100f,
        animationSpec = tween(durationMillis = 1000),
        label = "riskProgress"
    )

    val riskColor = when {
        riskScore >= 76 -> RiskCritical
        riskScore >= 51 -> RiskHigh
        riskScore >= 26 -> RiskModerate
        else -> RiskSafe
    }

    val riskLevelText = when {
        riskScore >= 76 -> if (language == AppLanguage.HINDI) "अति गंभीर जोखिम" else "CRITICAL OUTBREAK RISK"
        riskScore >= 51 -> if (language == AppLanguage.HINDI) "उच्च रोग जोखिम" else "HIGH INFECTION RISK"
        riskScore >= 26 -> if (language == AppLanguage.HINDI) "मध्यम जोखिम" else "MODERATE ATTENTION"
        else -> if (language == AppLanguage.HINDI) "सुरक्षित / कम जोखिम" else "LOW RISK (PROTECTED)"
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(riskColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "बहु-कारकीय फसल जोखिम सूचकांक" else "Multi-Factor Crop Risk Engine",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = riskColor.copy(alpha = 0.12f),
                    modifier = Modifier.clickable { showExplanationDialog = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Why",
                            tint = riskColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "यह स्कोर क्यों?" else "Why this score?",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = riskColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Gauge + Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 2D Circular Gauge Canvas
                Box(
                    modifier = Modifier.size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(90.dp)) {
                        val strokeWidth = 10.dp.toPx()
                        val arcSize = size.minDimension - strokeWidth
                        val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                        // Background arc
                        drawArc(
                            color = Color.LightGray.copy(alpha = 0.25f),
                            startAngle = 135f,
                            sweepAngle = 270f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(arcSize, arcSize),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        // Active animated arc
                        drawArc(
                            color = riskColor,
                            startAngle = 135f,
                            sweepAngle = 270f * animatedProgress,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(arcSize, arcSize),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$riskScore",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "/ 100",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Risk Status Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = riskLevelText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = riskColor,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "उच्च आर्द्रता (84%) व वर्षा से कवक बीजाणु प्रसार की संभावना प्रबल है।"
                        else
                            "84% humidity + 6h leaf wetness significantly elevates fungal sporulation in tomato crops.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = AgriGreenContainer
                        ) {
                            Text(
                                text = "Weather: 84% RH",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriGreenDark,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = HarvestGoldLight
                        ) {
                            Text(
                                text = "Stage: Flowering",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = HarvestAmberDark,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Preventive Call-to-action
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AgriGreenDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onOpenScanner() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Scan",
                            tint = HarvestGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "निचली पत्तियों की तुरंत जांच करें" else "Scan Foliage for Early Symptoms",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Go",
                        tint = HarvestGold,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }

    // "Why this score?" Explanation Dialog
    if (showExplanationDialog) {
        AlertDialog(
            onDismissRequest = { showExplanationDialog = false },
            confirmButton = {
                TextButton(onClick = { showExplanationDialog = false }) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "समझ गया" else "Understood",
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenPrimary
                    )
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Analysis",
                        tint = AgriGreenPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "जोखिम कारक विश्लेषण" else "Multi-Factor Risk Breakdown",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "यह स्कोर केवल फोटो पर नहीं, बल्कि मौसम, फसल अवस्था और क्षेत्रीय डेटा के संयोजन से तैयार किया गया है:"
                        else
                            "KrishiRakshak AI computes risk across 4 biological and meteorological layers:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    FactorRow("Relative Humidity (84%)", "Relative humidity exceeding 75% triggers spore germination", "+30% Weight", RiskHigh)
                    FactorRow("Leaf Wetness (6 hrs)", "Continuous wet foliage enables pathogen penetration", "+25% Weight", RiskHigh)
                    FactorRow("Crop Stage (Flowering)", "Physiological stress makes plants more susceptible", "+25% Weight", RiskModerate)
                    FactorRow("Nearby Hotspot (18 km)", "Active Early Blight cluster verified in Barabanki district", "+20% Weight", RiskModerate)
                }
            }
        )
    }
}

@Composable
private fun FactorRow(title: String, desc: String, weight: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(text = desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = color.copy(alpha = 0.15f)
            ) {
                Text(
                    text = weight,
                    color = color,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

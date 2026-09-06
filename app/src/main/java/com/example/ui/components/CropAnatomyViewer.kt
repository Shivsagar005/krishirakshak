package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.theme.*

enum class PlantAnatomyPart(val titleEn: String, val titleHi: String, val vulnerability: String) {
    LOWER_LEAF("Lower Canopy Leaves", "निचली पत्तियां (प्रारंभिक झुलसा)", "High Fungal Spore Inoculum Zone"),
    UPPER_LEAF("Young Canopy Shoots", "शीर्ष कोपलें (पर्ण कुंचन)", "Sucking Pest & Viral Vector Target"),
    FLOWER_CLUSTER("Flowering Clusters", "फूल व परागण गुच्छे", "Critical Yield Period & Blossom Drop"),
    STEM_BASE("Crown & Collar Region", "तना व जड़ का संधि स्थल", "Damping-off & Sclerotium Wilt Risk")
}

@Composable
fun CropAnatomyViewer(
    onScanCropPart: (String) -> Unit
) {
    val language by MockAndDemoRepository.currentLanguage.collectAsState()
    var selectedPart by remember { mutableStateOf(PlantAnatomyPart.LOWER_LEAF) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Park,
                        contentDescription = "2D Model",
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "2D फसल शारीरिक संरचना मॉडल" else "2D Plant Anatomical Risk Model",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Surface(
                    color = HarvestGoldLight,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Tomato Specimen",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HarvestAmberDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = if (language == AppLanguage.HINDI)
                    "पौधे के भाग पर टैप करें और रोग की सुभेद्यता देखें:"
                else
                    "Tap on plant organs to inspect localized infection vulnerability:",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 6.dp)
            )

            // Interactive 2D Structure Canvas + Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 2D Illustrated Plant Canvas
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(AgriGreenContainer.copy(alpha = 0.4f))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Draw Soil Ground Line
                        drawLine(
                            color = Color(0xFF795548),
                            start = Offset(10f, h - 25f),
                            end = Offset(w - 10f, h - 25f),
                            strokeWidth = 4f
                        )

                        // Draw Roots
                        val rootPath = Path().apply {
                            moveTo(w / 2, h - 25f)
                            lineTo(w / 2 - 20f, h - 5f)
                            moveTo(w / 2, h - 25f)
                            lineTo(w / 2, h - 2f)
                            moveTo(w / 2, h - 25f)
                            lineTo(w / 2 + 20f, h - 6f)
                        }
                        drawPath(rootPath, color = Color(0xFF8D6E63), style = Stroke(width = 3f))

                        // Draw Main Stem
                        val stemPath = Path().apply {
                            moveTo(w / 2, h - 25f)
                            cubicTo(w / 2 - 5f, h * 0.6f, w / 2 + 5f, h * 0.3f, w / 2, 25f)
                        }
                        drawPath(stemPath, color = Color(0xFF2E7D32), style = Stroke(width = 6f))

                        // Draw Lower Leaves (Target zone for early blight)
                        val lowerLeafColor = if (selectedPart == PlantAnatomyPart.LOWER_LEAF) Color(0xFFE65100) else Color(0xFF388E3C)
                        drawCircle(
                            color = lowerLeafColor,
                            radius = 16f,
                            center = Offset(w * 0.25f, h * 0.65f)
                        )
                        drawLine(
                            color = Color(0xFF2E7D32),
                            start = Offset(w / 2, h * 0.65f),
                            end = Offset(w * 0.25f, h * 0.65f),
                            strokeWidth = 3f
                        )

                        // Draw Upper Leaves
                        val upperLeafColor = if (selectedPart == PlantAnatomyPart.UPPER_LEAF) Color(0xFFE65100) else Color(0xFF4CAF50)
                        drawCircle(
                            color = upperLeafColor,
                            radius = 14f,
                            center = Offset(w * 0.75f, h * 0.4f)
                        )
                        drawLine(
                            color = Color(0xFF2E7D32),
                            start = Offset(w / 2, h * 0.4f),
                            end = Offset(w * 0.75f, h * 0.4f),
                            strokeWidth = 3f
                        )

                        // Draw Flower Cluster
                        val flowerColor = if (selectedPart == PlantAnatomyPart.FLOWER_CLUSTER) Color(0xFFE65100) else Color(0xFFFFD54F)
                        drawCircle(
                            color = flowerColor,
                            radius = 10f,
                            center = Offset(w / 2, 22f)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // 2D Organ Selectable Buttons
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PlantAnatomyPart.values().forEach { part ->
                        val isSelected = selectedPart == part
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) AgriGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedPart = part }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (language == AppLanguage.HINDI) part.titleHi else part.titleEn,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = HarvestGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Detailed Diagnostic Inspection card for selected part
            AnimatedVisibility(visible = true) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WarningAmber,
                                contentDescription = "Risk",
                                tint = RiskHigh,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedPart.vulnerability,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RiskHigh
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (selectedPart) {
                                PlantAnatomyPart.LOWER_LEAF ->
                                    "Spores splash from soil during rain or surface irrigation. Watch for dark circular rings with concentric ridges (Alternaria early blight signature)."
                                PlantAnatomyPart.UPPER_LEAF ->
                                    "Preferred feeding site for Bemisia tabaci whiteflies. Early detection prevents transmission of Tomato Leaf Curl Virus (ToLCV)."
                                PlantAnatomyPart.FLOWER_CLUSTER ->
                                    "High moisture during flowering triggers blossom end rot and Botrytis grey mold. Maintain proper calcium availability."
                                PlantAnatomyPart.STEM_BASE ->
                                    "Check for water-soaked collar rot lesions near the soil line caused by Sclerotium rolfsii or Rhizoctonia."
                            },
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { onScanCropPart(selectedPart.name) },
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Scan",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "इस भाग को स्कैन करें" else "Scan This Plant Part",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

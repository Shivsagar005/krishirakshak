package com.example.ui.components

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
import androidx.compose.ui.window.Dialog
import com.example.data.MockAndDemoRepository
import com.example.model.UserRole
import com.example.ui.theme.*

data class DemoStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val targetAction: String
)

@Composable
fun JudgeDemoWalkthroughDialog(
    onDismiss: () -> Unit,
    onNavigateTab: (Int) -> Unit
) {
    var currentStepIndex by remember { mutableIntStateOf(0) }

    val demoSteps = remember {
        listOf(
            DemoStep(
                1,
                "Examine Farmer Microclimate",
                "Start as Farmer Ramesh Kumar in Lucknow, UP. Notice ambient weather: 31°C, 84% humidity, and elevated leaf wetness creating a fungal infection window.",
                "Dashboard"
            ),
            DemoStep(
                2,
                "Multi-Factor 2D Crop Risk Gauge",
                "Observe the animated circular gauge displaying Score: 78 / 100 (HIGH RISK) computed across biological and meteorological layers.",
                "Dashboard"
            ),
            DemoStep(
                3,
                "Explainable AI Breakdown",
                "Tap 'Why this score?' on the gauge to inspect the exact weights: 30% relative humidity, 25% leaf wetness, 25% flowering vulnerability, 20% regional cluster proximity.",
                "Dashboard"
            ),
            DemoStep(
                4,
                "2D Plant Anatomical Model",
                "Interact with the 2D anatomical plant model. Tap on 'Lower Canopy Leaves' to understand Alternaria spore splash and symptoms.",
                "Dashboard"
            ),
            DemoStep(
                5,
                "Vision AI Scanner Viewfinder",
                "Navigate to 'Scan' tab. Observe the real-time viewfinder with laser line, corner brackets, and lighting/focus quality pre-checks.",
                "Scan Tab"
            ),
            DemoStep(
                6,
                "Run 6-Step Diagnostic Pipeline",
                "Select 'Early Blight' sample leaf and tap 'Scan Specimen'. Watch the 6-stage truthful pipeline (quality -> genus -> symptoms -> pathogen -> weather risk -> IPM).",
                "Scan Tab"
            ),
            DemoStep(
                7,
                "Integrated Pest Management (IPM)",
                "Review the structured advisory: immediate sanitation, Trichoderma bio-fungicide, and caution against unwarranted chemical applications.",
                "Scan Tab"
            ),
            DemoStep(
                8,
                "Human-in-the-Loop Expert Dispatch",
                "Tap 'Send to Expert' to dispatch the scan case to the ICAR Central Plant Pathology Desk for secondary validation.",
                "Scan Tab"
            ),
            DemoStep(
                9,
                "ICAR Expert Clinical Console",
                "Switch role to '🔬 Expert' in the top bar to assume Dr. Priya Sharma's role. Inspect the pending queue.",
                "Expert Review"
            ),
            DemoStep(
                10,
                "Expert Validation & Clinical Certification",
                "Review the farmer's symptoms and tap 'Approve Diagnosis' with clinical advisory notes. Notice immediate live verification sync.",
                "Expert Review"
            ),
            DemoStep(
                11,
                "Government Command Center",
                "Switch role to '🏛️ Govt' in the top bar. Review national KPIs (148,290 farmers, 5 active outbreak clusters, 87.4% verified ratio).",
                "Govt Center"
            ),
            DemoStep(
                12,
                "Interactive GIS Outbreak Hotspot Network",
                "Open the GIS Map. Inspect the Barabanki critical cluster (142 cases, +31% trend) and broadcast early warning alerts.",
                "GIS Map"
            ),
            DemoStep(
                13,
                "IoT Optical Smart Traps",
                "Navigate to 'IoT Traps' tab. Inspect the optical whitefly trap (23 flies / trap crossing the ETL 15 limit) and rhizosphere sensors.",
                "IoT Traps"
            ),
            DemoStep(
                14,
                "Conversational Krishi AI Assistant",
                "Navigate to 'Krishi AI' tab. Ask questions in English or Hindi (e.g. 'How to protect crops from high humidity?'). Powered by Gemini 3.5 AI.",
                "Krishi AI"
            ),
            DemoStep(
                15,
                "Complete Closed-Loop Protection",
                "From leaf detection to farmer advisory, expert certification, regional GIS containment, and government directives: an end-to-end mission-ready system.",
                "Complete"
            )
        )
    }

    val currentStep = demoSteps[currentStepIndex]

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, AgriGreenPrimary, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = HarvestGold,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "Trophy",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SIH Judge Walkthrough",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = AgriGreenContainer
                    ) {
                        Text(
                            text = "Step ${currentStep.stepNumber} / 15",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Indicator
                LinearProgressIndicator(
                    progress = (currentStepIndex + 1) / 15f,
                    color = AgriGreenPrimary,
                    trackColor = Color.LightGray.copy(alpha = 0.3f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Step Title & Body
                Text(
                    text = currentStep.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentStep.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Navigation Shortcut button for this step
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            when (currentStep.stepNumber) {
                                1, 2, 3, 4 -> {
                                    MockAndDemoRepository.setRole(UserRole.FARMER)
                                    onNavigateTab(0)
                                }
                                5, 6, 7, 8 -> {
                                    MockAndDemoRepository.setRole(UserRole.FARMER)
                                    onNavigateTab(1)
                                }
                                9, 10 -> {
                                    MockAndDemoRepository.setRole(UserRole.EXPERT)
                                }
                                11 -> {
                                    MockAndDemoRepository.setRole(UserRole.GOVERNMENT)
                                }
                                12 -> {
                                    onNavigateTab(2)
                                }
                                13 -> {
                                    onNavigateTab(3)
                                }
                                14 -> {
                                    onNavigateTab(4)
                                }
                                else -> {}
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = "Action",
                                tint = AgriGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Go to: ${currentStep.targetAction}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriGreenPrimary
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Go",
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Stepper Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            if (currentStepIndex > 0) currentStepIndex--
                        },
                        enabled = currentStepIndex > 0
                    ) {
                        Text("Previous")
                    }

                    if (currentStepIndex < demoSteps.size - 1) {
                        Button(
                            onClick = {
                                currentStepIndex++
                                // Automatically sync screen if desired
                                when (currentStepIndex + 1) {
                                    5 -> onNavigateTab(1)
                                    9 -> MockAndDemoRepository.setRole(UserRole.EXPERT)
                                    11 -> MockAndDemoRepository.setRole(UserRole.GOVERNMENT)
                                    12 -> onNavigateTab(2)
                                    13 -> onNavigateTab(3)
                                    14 -> onNavigateTab(4)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Next Step")
                        }
                    } else {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Finish Walkthrough")
                        }
                    }
                }
            }
        }
    }
}

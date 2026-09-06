package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockAndDemoRepository
import com.example.model.AppLanguage
import com.example.model.UserRole
import com.example.ui.components.JudgeDemoWalkthroughDialog
import com.example.ui.components.KrishiTopBar
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                KrishiRakshakApp()
            }
        }
    }
}

@Composable
fun KrishiRakshakApp() {
    val currentRole by MockAndDemoRepository.currentRole.collectAsState()
    val currentLanguage by MockAndDemoRepository.currentLanguage.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var chatInitialPrompt by remember { mutableStateOf<String?>(null) }
    var showDemoWalkthrough by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            KrishiTopBar(
                onOpenDemoGuide = { showDemoWalkthrough = true }
            )
        },
        bottomBar = {
            if (currentRole == UserRole.FARMER) {
                NavigationBar(
                    containerColor = AgriGreenDark,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = if (selectedTab == 0) HarvestGold else Color.White.copy(alpha = 0.7f)
                            )
                        },
                        label = {
                            Text(
                                text = if (currentLanguage == AppLanguage.HINDI) "होम" else "Home",
                                fontSize = 10.sp,
                                color = if (selectedTab == 0) HarvestGold else Color.White.copy(alpha = 0.7f),
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = AgriGreenMedium
                        ),
                        modifier = Modifier.testTag("nav_home")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Scan",
                                tint = if (selectedTab == 1) HarvestGold else Color.White.copy(alpha = 0.7f)
                            )
                        },
                        label = {
                            Text(
                                text = if (currentLanguage == AppLanguage.HINDI) "स्कैन" else "Scan",
                                fontSize = 10.sp,
                                color = if (selectedTab == 1) HarvestGold else Color.White.copy(alpha = 0.7f),
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = AgriGreenMedium
                        ),
                        modifier = Modifier.testTag("nav_scan")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = "GIS Map",
                                tint = if (selectedTab == 2) HarvestGold else Color.White.copy(alpha = 0.7f)
                            )
                        },
                        label = {
                            Text(
                                text = if (currentLanguage == AppLanguage.HINDI) "मानचित्र" else "GIS Map",
                                fontSize = 10.sp,
                                color = if (selectedTab == 2) HarvestGold else Color.White.copy(alpha = 0.7f),
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = AgriGreenMedium
                        ),
                        modifier = Modifier.testTag("nav_gis")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.PestControl,
                                contentDescription = "IoT Traps",
                                tint = if (selectedTab == 3) HarvestGold else Color.White.copy(alpha = 0.7f)
                            )
                        },
                        label = {
                            Text(
                                text = if (currentLanguage == AppLanguage.HINDI) "ट्रैप्स" else "IoT Traps",
                                fontSize = 10.sp,
                                color = if (selectedTab == 3) HarvestGold else Color.White.copy(alpha = 0.7f),
                                fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = AgriGreenMedium
                        ),
                        modifier = Modifier.testTag("nav_iot")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = "Krishi AI",
                                tint = if (selectedTab == 4) HarvestGold else Color.White.copy(alpha = 0.7f)
                            )
                        },
                        label = {
                            Text(
                                text = if (currentLanguage == AppLanguage.HINDI) "कृषि AI" else "Krishi AI",
                                fontSize = 10.sp,
                                color = if (selectedTab == 4) HarvestGold else Color.White.copy(alpha = 0.7f),
                                fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = AgriGreenMedium
                        ),
                        modifier = Modifier.testTag("nav_ai")
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentRole == UserRole.FARMER && selectedTab != 1 && selectedTab != 4) {
                // 2D Model / AI Assistant Quick Action Button
                ExtendedFloatingActionButton(
                    onClick = { selectedTab = 1 },
                    containerColor = AgriGreenPrimary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                    modifier = Modifier.testTag("fab_2d_scan")
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Scan 2D Model",
                        tint = HarvestGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentLanguage == AppLanguage.HINDI) "2D मॉडल स्कैन" else "Scan 2D Crop",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRole) {
                UserRole.FARMER -> {
                    when (selectedTab) {
                        0 -> FarmerHomeScreen(
                            onNavigateToScan = { selectedTab = 1 },
                            onNavigateToChat = { prompt ->
                                chatInitialPrompt = prompt
                                selectedTab = 4
                            },
                            onNavigateToGis = { selectedTab = 2 },
                            onNavigateToIot = { selectedTab = 3 }
                        )
                        1 -> AiCropScannerScreen(
                            onNavigateToChatWithContext = { prompt ->
                                chatInitialPrompt = prompt
                                selectedTab = 4
                            },
                            onNavigateToGis = { selectedTab = 2 },
                            onNavigateToExpertQueue = {
                                MockAndDemoRepository.setRole(UserRole.EXPERT)
                            }
                        )
                        2 -> GisHotspotMapScreen()
                        3 -> IotPestScreen()
                        4 -> KrishiAiChatScreen(
                            initialPrompt = chatInitialPrompt
                        )
                    }
                }
                UserRole.EXPERT -> {
                    ExpertReviewScreen()
                }
                UserRole.GOVERNMENT -> {
                    GovernmentCommandScreen(
                        onNavigateToGis = {
                            MockAndDemoRepository.setRole(UserRole.FARMER)
                            selectedTab = 2
                        }
                    )
                }
            }

            // Role Status Banner when in Expert or Government mode
            if (currentRole != UserRole.FARMER) {
                Surface(
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                    color = when (currentRole) {
                        UserRole.EXPERT -> Color(0xFF0D47A1)
                        else -> Color(0xFF7C2D12)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (currentRole) {
                                UserRole.EXPERT -> "🔬 Active Mode: ICAR Expert Diagnostic Panel"
                                else -> "🏛️ Active Mode: National Government Command GIS"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        TextButton(
                            onClick = { MockAndDemoRepository.setRole(UserRole.FARMER) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Text("Switch to Farmer", color = HarvestGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // SIH Judge 15-Step Walkthrough Dialog
    if (showDemoWalkthrough) {
        JudgeDemoWalkthroughDialog(
            onDismiss = { showDemoWalkthrough = false },
            onNavigateTab = { tabIndex ->
                selectedTab = tabIndex
                showDemoWalkthrough = false
            }
        )
    }
}

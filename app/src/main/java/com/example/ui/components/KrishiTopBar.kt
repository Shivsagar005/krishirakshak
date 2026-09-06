package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockAndDemoRepository
import com.example.model.AppLanguage
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun KrishiTopBar(
    onOpenDemoGuide: () -> Unit
) {
    val currentRole by MockAndDemoRepository.currentRole.collectAsState()
    val currentLanguage by MockAndDemoRepository.currentLanguage.collectAsState()
    var showRoleMenu by remember { mutableStateOf(false) }

    Surface(
        color = AgriGreenDark,
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // App Emblem and Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AgriGreenMedium),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Shield",
                            tint = HarvestGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "KRISHIRAKSHAK",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "AI",
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                color = HarvestGold
                            )
                        }
                        Text(
                            text = if (currentLanguage == AppLanguage.HINDI) "फसल स्वास्थ्य प्रारंभिक चेतावनी तंत्र" else "Crop Health Early Warning Network",
                            fontSize = 10.sp,
                            color = TextSecondaryDark,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Action controls: Language, Role & Demo Tour
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Language Switcher Button
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF1E3A28),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                MockAndDemoRepository.setLanguage(
                                    if (currentLanguage == AppLanguage.ENGLISH) AppLanguage.HINDI else AppLanguage.ENGLISH
                                )
                            }
                    ) {
                        Text(
                            text = if (currentLanguage == AppLanguage.ENGLISH) "हिन्दी" else "ENG",
                            color = HarvestGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }

                    // Role Selector Chip / Dropdown
                    Box {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (currentRole) {
                                UserRole.FARMER -> Color(0xFF1B5E20)
                                UserRole.EXPERT -> Color(0xFF0D47A1)
                                UserRole.GOVERNMENT -> Color(0xFF7C2D12)
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { showRoleMenu = true }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = when (currentRole) {
                                        UserRole.FARMER -> "👨‍🌾 Farmer"
                                        UserRole.EXPERT -> "🔬 Expert"
                                        UserRole.GOVERNMENT -> "🏛️ Govt"
                                    },
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Switch Role",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showRoleMenu,
                            onDismissRequest = { showRoleMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("👨‍🌾 Farmer Dashboard (किसान)") },
                                onClick = {
                                    MockAndDemoRepository.setRole(UserRole.FARMER)
                                    showRoleMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🔬 Agri Expert Review (कृषि विशेषज्ञ)") },
                                onClick = {
                                    MockAndDemoRepository.setRole(UserRole.EXPERT)
                                    showRoleMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🏛️ Government Command GIS (सरकार)") },
                                onClick = {
                                    MockAndDemoRepository.setRole(UserRole.GOVERNMENT)
                                    showRoleMenu = false
                                }
                            )
                        }
                    }

                    // Judge / Demo Guide Button
                    Surface(
                        shape = CircleShape,
                        color = HarvestGold,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .clickable { onOpenDemoGuide() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "SIH Demo Walkthrough",
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

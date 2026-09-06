package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.ai.KrishiAiService
import com.example.data.MockAndDemoRepository
import com.example.model.AppLanguage
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun KrishiAiChatScreen(
    initialPrompt: String? = null
) {
    val messages by MockAndDemoRepository.chatMessages.collectAsState()
    val language by MockAndDemoRepository.currentLanguage.collectAsState()
    val farms by MockAndDemoRepository.farms.collectAsState()
    val currentFarm = farms.firstOrNull()

    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Handle initial prompt if passed
    LaunchedEffect(initialPrompt) {
        if (!initialPrompt.isNullOrBlank()) {
            inputText = initialPrompt
        }
    }

    // Scroll to bottom when messages update
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val quickQuestions = if (language == AppLanguage.HINDI) listOf(
        "मेरे टमाटर के पत्तों पर भूरे धब्बे क्यों हैं?",
        "बारिश के बाद फंगल रोग से कैसे बचें?",
        "सफेद मक्खी के लिए कौन सा जैविक कीटनाशक प्रयोग करें?",
        "मेरी फसल का वर्तमान जोखिम क्या है?"
    ) else listOf(
        "Why do my tomato leaves have brown rings?",
        "How to prevent fungal disease after rain?",
        "What organic bio-control to use against whitefly?",
        "What is the current infection risk for my crop?"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Context Awareness Badge at top
        Surface(
            color = AgriGreenContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(AgriGreenPrimary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Farm: ${currentFarm?.name ?: "Surya Agro"} · Tomato · 31°C, 84% RH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenDark
                    )
                }
                Text(
                    text = "Gemini 3.5 AI",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AgriGreenDark
                )
            }
        }

        // Chat Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(msg)
            }

            if (isThinking) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.border(1.dp, AgriGreenAccent.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = AgriGreenPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.HINDI) "कृषि विशेषज्ञ एआई सोच रहा है..." else "Krishi AI is synthesizing IPM advice...",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Prompt Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            quickQuestions.take(2).forEach { q ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            inputText = q
                        }
                ) {
                    Text(
                        text = q,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Input Box Row
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = if (language == AppLanguage.HINDI) "फसल या रोग के बारे में पूछें..." else "Ask about crop health, blight, IPM...",
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        val query = inputText.trim()
                        if (query.isNotBlank() && !isThinking) {
                            MockAndDemoRepository.addChatMessage(query, isFromAi = false)
                            inputText = ""
                            isThinking = true
                            coroutineScope.launch {
                                val reply = KrishiAiService.askKrishiAssistant(
                                    userQuery = query,
                                    cropContext = "Tomato (Flourishing Stage in Lucknow)",
                                    weatherContext = "31°C, 84% humidity, high fungal risk",
                                    isHindi = language == AppLanguage.HINDI
                                )
                                isThinking = false
                                MockAndDemoRepository.addChatMessage(reply, isFromAi = true)
                            }
                        }
                    },
                    containerColor = AgriGreenPrimary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(msg: com.example.model.ChatMessage) {
    val isAi = msg.isFromAi

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isAi) Arrangement.Start else Arrangement.End
    ) {
        if (isAi) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(AgriGreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "AI",
                    tint = HarvestGold,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isAi) 4.dp else 16.dp,
                bottomEnd = if (isAi) 16.dp else 4.dp
            ),
            color = if (isAi) MaterialTheme.colorScheme.surface else AgriGreenPrimary,
            tonalElevation = if (isAi) 2.dp else 0.dp,
            modifier = Modifier
                .widthIn(max = 300.dp)
                .border(
                    width = if (isAi) 1.dp else 0.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (isAi) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Krishi AI Assistant",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary
                        )
                        Text(
                            text = "ICAR + Gemini",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = msg.text,
                    fontSize = 13.sp,
                    color = if (isAi) MaterialTheme.colorScheme.onSurface else Color.White,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = msg.timestamp,
                    fontSize = 9.sp,
                    color = if (isAi) MaterialTheme.colorScheme.onSurfaceVariant else Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

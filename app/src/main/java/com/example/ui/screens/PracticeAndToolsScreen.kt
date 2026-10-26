package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FormulaItem
import com.example.data.model.MistakeItem
import com.example.data.model.MockTestResult
import com.example.data.model.QuestionItem
import com.example.data.model.QuestionType
import com.example.data.repository.FormulaData
import com.example.data.repository.QuestionBankData
import com.example.ui.theme.*
import kotlinx.coroutines.delay

enum class PracticeSubTab(val title: String) {
    MOCK_TEST("Mock Test"),
    QUESTION_BANK("Question Bank"),
    FORMULAS("Formulas & Science"),
    POMODORO_MISTAKES("Productivity")
}

@Composable
fun PracticeAndToolsScreen(
    mistakes: List<MistakeItem>,
    mockHistory: List<MockTestResult>,
    onAddMistake: (String, String, String, String) -> Unit,
    onToggleMistake: (String) -> Unit,
    onRecordMockResult: (MockTestResult) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSubTab by remember { mutableStateOf(PracticeSubTab.MOCK_TEST) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SubhxBackground)
            .testTag("practice_tools_screen")
    ) {
        // Sub-tabs row
        TabRow(
            selectedTabIndex = currentSubTab.ordinal,
            containerColor = SubhxSurface,
            contentColor = SubhxSecondary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[currentSubTab.ordinal]),
                    color = SubhxSecondary,
                    height = 3.dp
                )
            }
        ) {
            PracticeSubTab.values().forEach { tab ->
                val isSelected = currentSubTab == tab
                Tab(
                    selected = isSelected,
                    onClick = { currentSubTab = tab },
                    text = {
                        Text(
                            text = tab.title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) SubhxSecondary else SubhxTextMuted
                        )
                    },
                    modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
                )
            }
        }

        when (currentSubTab) {
            PracticeSubTab.MOCK_TEST -> MockTestTab(
                mockHistory = mockHistory,
                onTestCompleted = onRecordMockResult
            )
            PracticeSubTab.QUESTION_BANK -> QuestionBankTab()
            PracticeSubTab.FORMULAS -> FormulasAndScienceTab()
            PracticeSubTab.POMODORO_MISTAKES -> ProductivityTab(
                mistakes = mistakes,
                onAddMistake = onAddMistake,
                onToggleMistake = onToggleMistake
            )
        }
    }
}

// -------------------------------------------------------------
// 1. MOCK TEST TAB (Timed exam simulation)
// -------------------------------------------------------------
@Composable
fun MockTestTab(
    mockHistory: List<MockTestResult>,
    onTestCompleted: (MockTestResult) -> Unit
) {
    val allQuestions = remember { QuestionBankData.getDefaultQuestions().filter { it.type == QuestionType.MCQ } }
    var isTestActive by remember { mutableStateOf(false) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() }
    var secondsRemaining by remember { mutableStateOf(300) } // 5 minutes test
    var testFinished by remember { mutableStateOf(false) }
    var lastResult by remember { mutableStateOf<MockTestResult?>(null) }

    // Timer effect
    LaunchedEffect(isTestActive, secondsRemaining) {
        if (isTestActive && secondsRemaining > 0 && !testFinished) {
            delay(1000)
            secondsRemaining -= 1
            if (secondsRemaining <= 0) {
                testFinished = true
                isTestActive = false
                val score = calculateScore(allQuestions, userAnswers)
                val res = MockTestResult(
                    testId = "test_${System.currentTimeMillis()}",
                    totalQuestions = allQuestions.size,
                    correctAnswers = score,
                    scorePercentage = (score * 100) / allQuestions.size.coerceAtLeast(1),
                    timeTakenSeconds = (300 - secondsRemaining).toLong()
                )
                lastResult = res
                onTestCompleted(res)
            }
        }
    }

    if (!isTestActive && !testFinished) {
        // Test Start / Instructions Screen
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SubhxSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "OAV Odisha Class 10 Mock Examination",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = SubhxText
                        )
                        Text(
                            text = "Standard timed simulation covering Mathematics, Science, and English (The Proposal)",
                            style = MaterialTheme.typography.bodySmall,
                            color = SubhxSecondary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${allQuestions.size}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SubhxText)
                                Text("Questions", fontSize = 11.sp, color = SubhxTextMuted)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("5 Mins", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SubhxText)
                                Text("Duration", fontSize = 11.sp, color = SubhxTextMuted)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Instant", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SubhxSuccess)
                                Text("Evaluation", fontSize = 11.sp, color = SubhxTextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                userAnswers.clear()
                                currentQuestionIndex = 0
                                secondsRemaining = 300
                                isTestActive = true
                                testFinished = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SubhxPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("start_mock_test_btn")
                        ) {
                            Text("Start Timed Mock Exam", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Past Attempts History
            item {
                Text(
                    text = "PAST ATTEMPT HISTORY",
                    style = MaterialTheme.typography.labelSmall,
                    color = SubhxTextMuted,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.1.sp
                )
            }

            items(mockHistory) { attempt ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SubhxSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Score: ${attempt.scorePercentage}% (${attempt.correctAnswers}/${attempt.totalQuestions})",
                                fontWeight = FontWeight.Bold,
                                color = if (attempt.scorePercentage >= 80) SubhxSuccess else SubhxWarning
                            )
                            Text(
                                text = "Time taken: ${attempt.timeTakenSeconds}s",
                                fontSize = 11.sp,
                                color = SubhxTextMuted
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SubhxPrimary.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Completed", fontSize = 11.sp, color = SubhxPrimaryLight, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    } else if (isTestActive) {
        // Active Test Screen
        val currentQ = allQuestions.getOrNull(currentQuestionIndex)
        if (currentQ != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header with Timer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${currentQuestionIndex + 1} of ${allQuestions.size}",
                        fontWeight = FontWeight.Bold,
                        color = SubhxSecondary
                    )

                    val mins = secondsRemaining / 60
                    val secs = secondsRemaining % 60
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = SubhxWarning, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%02d:%02d", mins, secs),
                            fontWeight = FontWeight.Bold,
                            color = if (secondsRemaining < 60) SubhxDanger else SubhxWarning
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { (currentQuestionIndex + 1).toFloat() / allQuestions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = SubhxSecondary,
                    trackColor = SubhxSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Question Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SubhxSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "[${currentQ.chapterName}]",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SubhxPrimaryLight
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentQ.questionText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SubhxText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Options List
                currentQ.options.forEachIndexed { optIdx, optText ->
                    val isSelected = userAnswers[currentQuestionIndex] == optIdx
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) SubhxPrimary.copy(alpha = 0.25f) else SubhxSurface)
                            .border(
                                1.dp,
                                if (isSelected) SubhxPrimary else SubhxCardBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { userAnswers[currentQuestionIndex] = optIdx }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${('A' + optIdx)}. ",
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) SubhxSecondary else SubhxTextMuted
                        )
                        Text(
                            text = optText,
                            color = SubhxText,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Navigation Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { if (currentQuestionIndex > 0) currentQuestionIndex-- },
                        enabled = currentQuestionIndex > 0,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder)
                    ) {
                        Text("Previous", color = SubhxTextSecondary)
                    }

                    if (currentQuestionIndex < allQuestions.size - 1) {
                        Button(
                            onClick = { currentQuestionIndex++ },
                            colors = ButtonDefaults.buttonColors(containerColor = SubhxPrimary)
                        ) {
                            Text("Next")
                        }
                    } else {
                        Button(
                            onClick = {
                                testFinished = true
                                isTestActive = false
                                val score = calculateScore(allQuestions, userAnswers)
                                val res = MockTestResult(
                                    testId = "test_${System.currentTimeMillis()}",
                                    totalQuestions = allQuestions.size,
                                    correctAnswers = score,
                                    scorePercentage = (score * 100) / allQuestions.size.coerceAtLeast(1),
                                    timeTakenSeconds = (300 - secondsRemaining).toLong()
                                )
                                lastResult = res
                                onTestCompleted(res)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SubhxSuccess),
                            modifier = Modifier.testTag("submit_mock_test_btn")
                        ) {
                            Text("Submit Test")
                        }
                    }
                }
            }
        }
    } else {
        // Result Screen
        lastResult?.let { res ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SubhxSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SubhxSuccess),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Mock Test Completed!",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = SubhxText
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "${res.scorePercentage}%",
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Black,
                                color = if (res.scorePercentage >= 80) SubhxSuccess else SubhxWarning
                            )
                            Text(
                                text = "${res.correctAnswers} of ${res.totalQuestions} Questions Correct",
                                color = SubhxTextMuted,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    testFinished = false
                                    isTestActive = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SubhxPrimary)
                            ) {
                                Text("Back to Tests")
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "QUESTION BY QUESTION REVIEW",
                        style = MaterialTheme.typography.labelSmall,
                        color = SubhxTextMuted,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(allQuestions.indices.toList()) { idx ->
                    val q = allQuestions[idx]
                    val chosen = userAnswers[idx]
                    val isCorrect = chosen == q.correctOptionIndex

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SubhxSurface),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCorrect) SubhxSuccess.copy(alpha = 0.5f) else SubhxDanger.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Q${idx + 1}. [${q.chapterName}]",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = SubhxPrimaryLight
                                )
                                Text(
                                    text = if (isCorrect) "+1 Mark" else "0 Marks",
                                    color = if (isCorrect) SubhxSuccess else SubhxDanger,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = q.questionText, color = SubhxText, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Correct Answer: ${q.options.getOrNull(q.correctOptionIndex) ?: ""}",
                                color = SubhxSuccess,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            if (q.explanation.isNotBlank()) {
                                Text(
                                    text = "Explanation: ${q.explanation}",
                                    color = SubhxTextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun calculateScore(questions: List<QuestionItem>, answers: Map<Int, Int>): Int {
    var score = 0
    questions.forEachIndexed { index, questionItem ->
        if (answers[index] == questionItem.correctOptionIndex) {
            score++
        }
    }
    return score
}

// -------------------------------------------------------------
// 2. QUESTION BANK TAB
// -------------------------------------------------------------
@Composable
fun QuestionBankTab() {
    val allQuestions = remember { QuestionBankData.getDefaultQuestions() }
    var selectedType by remember { mutableStateOf<QuestionType?>(null) }
    var expandedAnswers by remember { mutableStateOf(setOf<String>()) }

    val filtered = if (selectedType == null) allQuestions else allQuestions.filter { it.type == selectedType }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Filter pills for question types
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selectedType == null) SubhxPrimary else SubhxSurface)
                        .clickable { selectedType = null }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("All Questions (${allQuestions.size})", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            items(QuestionType.values()) { type ->
                val isSelected = selectedType == type
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) SubhxPrimary else SubhxSurface)
                        .clickable { selectedType = type }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(type.label, color = if (isSelected) Color.White else SubhxTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { q ->
                val isExpanded = expandedAnswers.contains(q.id)

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SubhxSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${q.subjectId.uppercase()} • ${q.chapterName}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SubhxSecondary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(SubhxPrimary.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("${q.marks} Marks", fontSize = 10.sp, color = SubhxPrimaryLight, fontWeight = FontWeight.Bold)
                                }
                                if (q.isVerifiedPYQ) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(SubhxSuccess.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("PYQ Verified", fontSize = 10.sp, color = SubhxSuccess, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = q.questionText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SubhxText
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Toggle Answer Button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedAnswers = if (isExpanded) expandedAnswers - q.id else expandedAnswers + q.id
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isExpanded) "Hide Model Answer & Notes" else "View Model Answer & Notes",
                                color = SubhxSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = SubhxSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SubhxCardBg)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Model Answer:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = SubhxSuccess
                                )
                                Text(
                                    text = q.modelAnswer,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SubhxText,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                if (q.explanation.isNotBlank()) {
                                    Text(
                                        text = "Exam Note: ${q.explanation}",
                                        fontSize = 11.sp,
                                        color = SubhxTextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. FORMULAS & SCIENCE TOOLS TAB
// -------------------------------------------------------------
@Composable
fun FormulasAndScienceTab() {
    val formulas = remember { FormulaData.getDefaultFormulas() }
    val chemicalEquations = remember { FormulaData.getChemicalEquations() }
    var selectedCategory by remember { mutableStateOf("All") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "CORE FORMULAS & WORKED NUMERICALS",
                style = MaterialTheme.typography.labelSmall,
                color = SubhxTextMuted,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp
            )
        }

        items(formulas) { formula ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SubhxSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${formula.subject} • ${formula.topic}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SubhxSecondary
                        )
                        Text(text = formula.units, fontSize = 10.sp, color = SubhxTextMuted)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formula.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = SubhxText
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SubhxBackground)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = formula.formulaLatexOrText,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = SubhxPrimaryLight
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = formula.description, fontSize = 11.sp, color = SubhxTextSecondary)
                    if (formula.workedExample.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Worked Example: ${formula.workedExample}",
                            fontSize = 11.sp,
                            color = SubhxSuccess
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "CHEMICAL EQUATIONS & OBSERVATIONS",
                style = MaterialTheme.typography.labelSmall,
                color = SubhxTextMuted,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp
            )
        }

        items(chemicalEquations) { eq ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SubhxSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = eq.reactionType,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SubhxWarning
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SubhxBackground)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = eq.balancedEquation,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = SubhxSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Key Observation: ${eq.keyObservation}",
                        fontSize = 11.sp,
                        color = SubhxTextSecondary
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. PRODUCTIVITY TAB (Pomodoro & Mistake Notebook)
// -------------------------------------------------------------
@Composable
fun ProductivityTab(
    mistakes: List<MistakeItem>,
    onAddMistake: (String, String, String, String) -> Unit,
    onToggleMistake: (String) -> Unit
) {
    // Pomodoro Timer State
    var timerMode by remember { mutableStateOf("Focus") } // Focus (25m), Short Break (5m), Long Break (15m)
    var pomodoroSeconds by remember { mutableStateOf(25 * 60) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var showAddMistakeDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isTimerRunning, pomodoroSeconds) {
        if (isTimerRunning && pomodoroSeconds > 0) {
            delay(1000)
            pomodoroSeconds -= 1
        } else if (pomodoroSeconds == 0) {
            isTimerRunning = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Pomodoro Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SubhxSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "POMODORO STUDY TIMER",
                        style = MaterialTheme.typography.labelSmall,
                        color = SubhxSecondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Mode selectors
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PillButton(
                            label = "Focus (25m)",
                            isSelected = timerMode == "Focus",
                            onClick = {
                                timerMode = "Focus"
                                pomodoroSeconds = 25 * 60
                                isTimerRunning = false
                            }
                        )
                        PillButton(
                            label = "Break (5m)",
                            isSelected = timerMode == "Short",
                            onClick = {
                                timerMode = "Short"
                                pomodoroSeconds = 5 * 60
                                isTimerRunning = false
                            }
                        )
                        PillButton(
                            label = "Long (15m)",
                            isSelected = timerMode == "Long",
                            onClick = {
                                timerMode = "Long"
                                pomodoroSeconds = 15 * 60
                                isTimerRunning = false
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val mins = pomodoroSeconds / 60
                    val secs = pomodoroSeconds % 60
                    Text(
                        text = String.format("%02d:%02d", mins, secs),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        color = SubhxText
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { isTimerRunning = !isTimerRunning },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isTimerRunning) SubhxWarning else SubhxPrimary
                            )
                        ) {
                            Icon(
                                imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isTimerRunning) "Pause" else "Start Focus")
                        }

                        OutlinedButton(
                            onClick = {
                                isTimerRunning = false
                                pomodoroSeconds = if (timerMode == "Focus") 25 * 60 else if (timerMode == "Short") 5 * 60 else 15 * 60
                            },
                            border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = SubhxTextMuted)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset", color = SubhxTextSecondary)
                        }
                    }
                }
            }
        }

        // Mistake Notebook Header & Add Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MISTAKE NOTEBOOK",
                        style = MaterialTheme.typography.labelSmall,
                        color = SubhxTextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp
                    )
                    Text(
                        text = "Log exam mistakes & correct them permanently",
                        fontSize = 11.sp,
                        color = SubhxSecondary
                    )
                }

                Button(
                    onClick = { showAddMistakeDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = SubhxPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Mistake", fontSize = 11.sp)
                }
            }
        }

        items(mistakes) { mistake ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SubhxSurface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (mistake.isResolved) SubhxSuccess.copy(alpha = 0.5f) else SubhxDanger.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mistake.subject,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = SubhxSecondary
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (mistake.isResolved) SubhxSuccess.copy(alpha = 0.2f) else SubhxDanger.copy(alpha = 0.2f))
                                .clickable { onToggleMistake(mistake.id) }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (mistake.isResolved) "Resolved ✓" else "Needs Revision",
                                color = if (mistake.isResolved) SubhxSuccess else SubhxDanger,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = mistake.questionOrTopic,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = SubhxText
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "What went wrong: ${mistake.whatWentWrong}",
                        fontSize = 11.sp,
                        color = SubhxDanger
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Correct approach: ${mistake.correctMethod}",
                        fontSize = 11.sp,
                        color = SubhxSuccess
                    )
                }
            }
        }
    }

    if (showAddMistakeDialog) {
        var sub by remember { mutableStateOf("Science") }
        var topic by remember { mutableStateOf("") }
        var wrong by remember { mutableStateOf("") }
        var right by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddMistakeDialog = false },
            containerColor = SubhxSurface,
            title = { Text("Log a Study Mistake", color = SubhxText, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = sub,
                        onValueChange = { sub = it },
                        label = { Text("Subject") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SubhxText,
                            unfocusedTextColor = SubhxText,
                            focusedBorderColor = SubhxSecondary,
                            unfocusedBorderColor = SubhxCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Question or Topic") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SubhxText,
                            unfocusedTextColor = SubhxText,
                            focusedBorderColor = SubhxSecondary,
                            unfocusedBorderColor = SubhxCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = wrong,
                        onValueChange = { wrong = it },
                        label = { Text("What mistake did you make?") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SubhxText,
                            unfocusedTextColor = SubhxText,
                            focusedBorderColor = SubhxSecondary,
                            unfocusedBorderColor = SubhxCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = right,
                        onValueChange = { right = it },
                        label = { Text("Correct Method / Concept") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SubhxText,
                            unfocusedTextColor = SubhxText,
                            focusedBorderColor = SubhxSecondary,
                            unfocusedBorderColor = SubhxCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (topic.isNotBlank()) {
                            onAddMistake(sub, topic, wrong, right)
                            showAddMistakeDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SubhxPrimary)
                ) {
                    Text("Save to Notebook")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMistakeDialog = false }) {
                    Text("Cancel", color = SubhxTextMuted)
                }
            }
        )
    }
}

@Composable
fun PillButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) SubhxPrimary else SubhxSurfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else SubhxTextSecondary
        )
    }
}

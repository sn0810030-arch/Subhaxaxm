package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChapterStatus
import com.example.data.model.DailyTask
import com.example.data.model.Subject
import com.example.ui.components.MainDestination
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    subjects: List<Subject>,
    dailyTasks: List<DailyTask>,
    streakDays: Int,
    examCountdownDays: Int,
    targetPercentage: Int,
    onNavigateTo: (MainDestination) -> Unit,
    onToggleTask: (String) -> Unit,
    onAddTask: (String, String, Int) -> Unit,
    onUpdateExamDays: (Int) -> Unit,
    onUpdateTargetPct: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showEditTargetDialog by remember { mutableStateOf(false) }

    // Calculate syllabus stats
    val allChapters = subjects.flatMap { it.chapters }
    val totalChapters = allChapters.size.coerceAtLeast(1)
    val completedChapters = allChapters.count { it.status == ChapterStatus.COMPLETED }
    val revisionChapters = allChapters.count { it.status == ChapterStatus.REVISION }
    val learningChapters = allChapters.count { it.status == ChapterStatus.LEARNING }
    val notStartedChapters = allChapters.count { it.status == ChapterStatus.NOT_STARTED }

    val overallProgress = (completedChapters.toFloat() / totalChapters.toFloat())
    val animatedProgress by animateFloatAsState(targetValue = overallProgress, label = "progress")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SubhxBackground)
            .padding(horizontal = 16.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // Hero Banner: Progress & Target Board Overview
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SubhxSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_progress_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "OAV ODISHA BOARD 2026",
                                style = MaterialTheme.typography.labelMedium,
                                color = SubhxSecondary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Target: $targetPercentage% Aggregate",
                                style = MaterialTheme.typography.titleLarge,
                                color = SubhxText,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Study Smart. Build Your Future.",
                                style = MaterialTheme.typography.bodySmall,
                                color = SubhxTextMuted
                            )
                        }

                        // Circular Progress
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier.size(72.dp),
                                color = SubhxPrimary,
                                trackColor = SubhxSurfaceVariant,
                                strokeWidth = 8.dp,
                                strokeCap = StrokeCap.Round
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${(overallProgress * 100).toInt()}%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = SubhxText
                                )
                                Text(
                                    text = "Done",
                                    fontSize = 9.sp,
                                    color = SubhxTextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Status breakdown pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusMiniPill("Completed", completedChapters, SubhxSuccess)
                        StatusMiniPill("Revision", revisionChapters, SubhxSecondary)
                        StatusMiniPill("Learning", learningChapters, SubhxWarning)
                        StatusMiniPill("Pending", notStartedChapters, SubhxTextMuted)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showEditTargetDialog = true },
                            modifier = Modifier.testTag("edit_target_button")
                        ) {
                            Text("Adjust Exam Target / Days", color = SubhxSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Quick Feature Banner for Study Proposals
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SubhxCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubhxPrimary.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateTo(MainDestination.PROPOSALS) }
                    .testTag("proposal_banner_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SubhxPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = "Proposals",
                            tint = SubhxPrimaryLight,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Exam Study Proposals & Plan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SubhxText
                        )
                        Text(
                            text = "15-Day 85% Strategy & Chekhov's 'The Proposal' play analysis",
                            style = MaterialTheme.typography.bodySmall,
                            color = SubhxTextMuted
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open Proposals",
                        tint = SubhxSecondary
                    )
                }
            }
        }

        // Quick Action Grid
        item {
            Text(
                text = "STUDY MODULES & TOOLS",
                style = MaterialTheme.typography.labelSmall,
                color = SubhxTextMuted,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardActionCard(
                    title = "SUBHXAXM AI",
                    subtitle = "OAV Odisha study tutor",
                    icon = Icons.Default.SmartToy,
                    accentColor = SubhxPrimary,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo(MainDestination.AI_TUTOR) }
                )
                DashboardActionCard(
                    title = "Chapter Tracker",
                    subtitle = "9 subjects syllabus",
                    icon = Icons.Default.Checklist,
                    accentColor = SubhxSecondary,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo(MainDestination.SYLLABUS) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardActionCard(
                    title = "Timed Mock Test",
                    subtitle = "Board exam simulator",
                    icon = Icons.Default.Quiz,
                    accentColor = SubhxSuccess,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo(MainDestination.PRACTICE) }
                )
                DashboardActionCard(
                    title = "Study Proposals",
                    subtitle = "Create & pledge plan",
                    icon = Icons.Default.Assignment,
                    accentColor = SubhxWarning,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo(MainDestination.PROPOSALS) }
                )
            }
        }

        // Daily Checklist Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DAILY STUDY CHECKLIST",
                    style = MaterialTheme.typography.labelSmall,
                    color = SubhxTextMuted,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.1.sp
                )
                IconButton(
                    onClick = { showAddTaskDialog = true },
                    modifier = Modifier.size(32.dp).testTag("add_task_icon_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task",
                        tint = SubhxSecondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                dailyTasks.take(5).forEach { task ->
                    DailyTaskRow(
                        task = task,
                        onToggle = { onToggleTask(task.id) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Add Task Dialog
    if (showAddTaskDialog) {
        var taskTitle by remember { mutableStateOf("") }
        var taskSubject by remember { mutableStateOf("General") }
        var taskMinutes by remember { mutableStateOf("30") }

        AlertDialog(
            onDismissRequest = { showAddTaskDialog = false },
            containerColor = SubhxSurface,
            title = {
                Text("Add Daily Study Task", color = SubhxText, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("Task Description") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SubhxText,
                            unfocusedTextColor = SubhxText,
                            focusedBorderColor = SubhxSecondary,
                            unfocusedBorderColor = SubhxCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_task_title")
                    )
                    OutlinedTextField(
                        value = taskSubject,
                        onValueChange = { taskSubject = it },
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
                        value = taskMinutes,
                        onValueChange = { taskMinutes = it },
                        label = { Text("Estimated Minutes") },
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
                        if (taskTitle.isNotBlank()) {
                            val mins = taskMinutes.toIntOrNull() ?: 30
                            onAddTask(taskTitle, taskSubject, mins)
                            showAddTaskDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SubhxPrimary),
                    modifier = Modifier.testTag("confirm_add_task")
                ) {
                    Text("Add Task")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTaskDialog = false }) {
                    Text("Cancel", color = SubhxTextMuted)
                }
            }
        )
    }

    // Edit Target Dialog
    if (showEditTargetDialog) {
        var daysText by remember { mutableStateOf(examCountdownDays.toString()) }
        var targetText by remember { mutableStateOf(targetPercentage.toString()) }

        AlertDialog(
            onDismissRequest = { showEditTargetDialog = false },
            containerColor = SubhxSurface,
            title = {
                Text("Update Target & Exam Countdown", color = SubhxText, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = targetText,
                        onValueChange = { targetText = it },
                        label = { Text("Target Percentage (%)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SubhxText,
                            unfocusedTextColor = SubhxText,
                            focusedBorderColor = SubhxSecondary,
                            unfocusedBorderColor = SubhxCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = daysText,
                        onValueChange = { daysText = it },
                        label = { Text("Days Left to Board Exam") },
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
                        val days = daysText.toIntOrNull() ?: examCountdownDays
                        val target = targetText.toIntOrNull() ?: targetPercentage
                        onUpdateExamDays(days)
                        onUpdateTargetPct(target)
                        showEditTargetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SubhxPrimary)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditTargetDialog = false }) {
                    Text("Cancel", color = SubhxTextMuted)
                }
            }
        )
    }
}

@Composable
fun StatusMiniPill(label: String, count: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SubhxSurfaceVariant)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = count.toString(),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            color = color
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = SubhxTextMuted
        )
    }
}

@Composable
fun DashboardActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SubhxSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder),
        modifier = modifier
            .clickable { onClick() }
            .testTag("action_card_${title.lowercase().replace(" ", "_")}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = SubhxText
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = SubhxTextMuted,
                maxLines = 1
            )
        }
    }
}

@Composable
fun DailyTaskRow(
    task: DailyTask,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SubhxSurface)
            .border(1.dp, SubhxCardBorder, RoundedCornerShape(12.dp))
            .clickable { onToggle() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("daily_task_row_${task.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (task.isCompleted) SubhxSuccess else Color.Transparent)
                .border(
                    2.dp,
                    if (task.isCompleted) SubhxSuccess else SubhxTextMuted,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (task.isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Done",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (task.isCompleted) SubhxTextMuted else SubhxText
            )
            Text(
                text = "${task.subject} • ${task.estimatedMinutes} mins",
                fontSize = 11.sp,
                color = SubhxSecondary
            )
        }
    }
}

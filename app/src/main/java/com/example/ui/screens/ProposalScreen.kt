package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProposalCategory
import com.example.data.model.ProposalMilestone
import com.example.data.model.StudyProposal
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalScreen(
    proposals: List<StudyProposal>,
    onAddProposal: (StudyProposal) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<ProposalCategory?>(null) }
    var selectedProposalForDetail by remember { mutableStateOf<StudyProposal?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }

    val filteredProposals = if (selectedCategory == null) {
        proposals
    } else {
        proposals.filter { it.category == selectedCategory }
    }

    Scaffold(
        containerColor = SubhxBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = SubhxPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("create_proposal_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Create Proposal")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New Proposal", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        },
        modifier = modifier.testTag("proposal_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Screen Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Exam Study Proposals",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = SubhxText
                    )
                    Text(
                        text = "Formal study proposals, 15-day strategies & Chekhov play analysis",
                        style = MaterialTheme.typography.bodySmall,
                        color = SubhxTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Category Filter Pills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    CategoryPill(
                        label = "All Proposals",
                        isSelected = selectedCategory == null,
                        onClick = { selectedCategory = null }
                    )
                }
                items(ProposalCategory.values()) { cat ->
                    CategoryPill(
                        label = cat.label,
                        isSelected = selectedCategory == cat,
                        onClick = { selectedCategory = cat }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Proposals List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProposals) { proposal ->
                    ProposalItemCard(
                        proposal = proposal,
                        onClick = { selectedProposalForDetail = proposal }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    // Proposal Detail Sheet / Modal
    selectedProposalForDetail?.let { proposal ->
        ProposalDetailBottomSheet(
            proposal = proposal,
            onDismiss = { selectedProposalForDetail = null }
        )
    }

    // Create New Custom Proposal Dialog
    if (showCreateDialog) {
        CreateProposalDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { newProp ->
                onAddProposal(newProp)
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun CategoryPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) SubhxPrimary else SubhxSurface)
            .border(
                1.dp,
                if (isSelected) SubhxPrimary else SubhxCardBorder,
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else SubhxTextSecondary
        )
    }
}

@Composable
fun ProposalItemCard(
    proposal: StudyProposal,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SubhxSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("proposal_card_${proposal.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Badges row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SubhxPrimary.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = proposal.category.label,
                        color = SubhxPrimaryLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Approved",
                        tint = SubhxSuccess,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${proposal.targetScorePercentage}% Target",
                        color = SubhxSuccess,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = proposal.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SubhxText
            )

            Text(
                text = proposal.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = SubhxSecondary,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            Text(
                text = proposal.executiveSummary,
                style = MaterialTheme.typography.bodySmall,
                color = SubhxTextMuted,
                maxLines = 2,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer meta: Duration, Daily Hours, Milestones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "⏱ ${proposal.durationDays} Days",
                        fontSize = 11.sp,
                        color = SubhxTextSecondary
                    )
                    Text(
                        text = "⚡ ${proposal.dailyHoursGoal} hrs/day",
                        fontSize = 11.sp,
                        color = SubhxTextSecondary
                    )
                    Text(
                        text = "🎯 ${proposal.milestones.size} Milestones",
                        fontSize = 11.sp,
                        color = SubhxTextSecondary
                    )
                }

                Text(
                    text = "View Proposal →",
                    color = SubhxPrimaryLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalDetailBottomSheet(
    proposal: StudyProposal,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SubhxSurface,
        modifier = Modifier.testTag("proposal_detail_sheet")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "FORMAL ACADEMIC PROPOSAL",
                            style = MaterialTheme.typography.labelSmall,
                            color = SubhxSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = proposal.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = SubhxText
                        )
                        Text(
                            text = proposal.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SubhxPrimaryLight
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = SubhxTextMuted)
                    }
                }
            }

            // Key Metrics Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SubhxSurfaceVariant)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MetricColumn("Target", "${proposal.targetScorePercentage}%", SubhxSuccess)
                    MetricColumn("Duration", "${proposal.durationDays} Days", SubhxSecondary)
                    MetricColumn("Daily Hours", "${proposal.dailyHoursGoal} hrs", SubhxWarning)
                    MetricColumn("Scope", proposal.targetBoard.split(" ").firstOrNull() ?: "Board", SubhxPrimaryLight)
                }
            }

            // Executive Summary
            item {
                SectionHeader("1. Executive Summary")
                Text(
                    text = proposal.executiveSummary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubhxTextSecondary,
                    lineHeight = 22.sp
                )
            }

            // Key Objectives
            item {
                SectionHeader("2. Key Objectives & Deliverables")
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    proposal.keyObjectives.forEachIndexed { idx, obj ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = "✓",
                                color = SubhxSuccess,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = obj,
                                style = MaterialTheme.typography.bodyMedium,
                                color = SubhxText
                            )
                        }
                    }
                }
            }

            // Milestones Timeline
            item {
                SectionHeader("3. Execution Milestones & Timeline")
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    proposal.milestones.forEach { milestone ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SubhxCardBg)
                                .border(1.dp, SubhxCardBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (milestone.isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (milestone.isDone) SubhxSuccess else SubhxTextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = milestone.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = SubhxText
                                )
                                Text(
                                    text = milestone.description,
                                    fontSize = 11.sp,
                                    color = SubhxTextMuted
                                )
                            }
                            Text(
                                text = "Day ${milestone.targetDays}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SubhxSecondary
                            )
                        }
                    }
                }
            }

            // Strategic Action Plan
            item {
                SectionHeader("4. Daily Strategic Action Plan")
                Text(
                    text = proposal.strategicActionPlan,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubhxTextSecondary,
                    lineHeight = 22.sp
                )
            }

            // Expected Outcome
            item {
                SectionHeader("5. Expected Academic Outcome")
                Text(
                    text = proposal.expectedOutcome,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubhxSuccess,
                    fontWeight = FontWeight.Medium
                )
            }

            // Student Pledge & Signature Box
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SubhxCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SubhxPrimary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = SubhxPrimaryLight,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ACADEMIC COMMITMENT PLEDGE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SubhxPrimaryLight
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "\"${proposal.authorPledge}\"",
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = SubhxText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Verified for: ${proposal.targetBoard}",
                                fontSize = 10.sp,
                                color = SubhxTextMuted
                            )
                            Text(
                                text = "Status: ACTIVE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SubhxSuccess
                            )
                        }
                    }
                }
            }

            // Share & Action Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "Study Proposal: ${proposal.title}"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    """
                                    === ${proposal.title} ===
                                    Target: ${proposal.targetScorePercentage}% | Duration: ${proposal.durationDays} Days | Board: ${proposal.targetBoard}
                                    
                                    Summary:
                                    ${proposal.executiveSummary}
                                    
                                    Action Plan:
                                    ${proposal.strategicActionPlan}
                                    
                                    Pledge: "${proposal.authorPledge}"
                                    -- Built with SUBHXAXM Board Master
                                    """.trimIndent()
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Study Proposal"))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SubhxPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("share_proposal_button")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Proposal")
                    }
                    OutlinedButton(
                        onClick = onDismiss,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Close", color = SubhxTextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = SubhxSecondary,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
fun MetricColumn(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = color)
        Text(text = label, fontSize = 10.sp, color = SubhxTextMuted)
    }
}

@Composable
fun CreateProposalDialog(
    onDismiss: () -> Unit,
    onCreate: (StudyProposal) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("Custom Board Preparation Strategy") }
    var targetPct by remember { mutableStateOf("85") }
    var durationDays by remember { mutableStateOf("15") }
    var dailyHours by remember { mutableStateOf("6") }
    var summary by remember { mutableStateOf("") }
    var actionPlan by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SubhxSurface,
        title = {
            Text("Draft New Study Proposal", color = SubhxText, fontWeight = FontWeight.Bold)
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Proposal Title") },
                        placeholder = { Text("e.g. 10-Day Mathematics Mastery") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SubhxText,
                            unfocusedTextColor = SubhxText,
                            focusedBorderColor = SubhxSecondary,
                            unfocusedBorderColor = SubhxCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_new_proposal_title")
                    )
                }
                item {
                    OutlinedTextField(
                        value = targetPct,
                        onValueChange = { targetPct = it },
                        label = { Text("Target Percentage (%)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SubhxText,
                            unfocusedTextColor = SubhxText,
                            focusedBorderColor = SubhxSecondary,
                            unfocusedBorderColor = SubhxCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = durationDays,
                            onValueChange = { durationDays = it },
                            label = { Text("Duration (Days)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SubhxText,
                                unfocusedTextColor = SubhxText,
                                focusedBorderColor = SubhxSecondary,
                                unfocusedBorderColor = SubhxCardBorder
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = dailyHours,
                            onValueChange = { dailyHours = it },
                            label = { Text("Daily Hours") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SubhxText,
                                unfocusedTextColor = SubhxText,
                                focusedBorderColor = SubhxSecondary,
                                unfocusedBorderColor = SubhxCardBorder
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        label = { Text("Executive Summary & Goal") },
                        placeholder = { Text("Describe what this study proposal aims to accomplish...") },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SubhxText,
                            unfocusedTextColor = SubhxText,
                            focusedBorderColor = SubhxSecondary,
                            unfocusedBorderColor = SubhxCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = actionPlan,
                        onValueChange = { actionPlan = it },
                        label = { Text("Action Plan & Schedule") },
                        placeholder = { Text("e.g. 2h theory + 2h question practice + 1h mock test...") },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SubhxText,
                            unfocusedTextColor = SubhxText,
                            focusedBorderColor = SubhxSecondary,
                            unfocusedBorderColor = SubhxCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val newProp = StudyProposal(
                            id = "custom_prop_${System.currentTimeMillis()}",
                            title = title,
                            subtitle = subtitle,
                            category = ProposalCategory.CUSTOM_PROPOSAL,
                            targetScorePercentage = targetPct.toIntOrNull() ?: 85,
                            durationDays = durationDays.toIntOrNull() ?: 15,
                            dailyHoursGoal = dailyHours.toIntOrNull() ?: 6,
                            executiveSummary = if (summary.isNotBlank()) summary else "Personalized study proposal for Class 10 Board exam success.",
                            keyObjectives = listOf(
                                "Complete targeted revision within $durationDays days",
                                "Maintain $dailyHours hours daily focus",
                                "Solve question bank and past year exam papers"
                            ),
                            milestones = listOf(
                                ProposalMilestone("Phase 1: Syllabus Sprint", (durationDays.toIntOrNull() ?: 15) / 2, "Consolidate high yield chapters", false),
                                ProposalMilestone("Phase 2: Final Mock Tests", durationDays.toIntOrNull() ?: 15, "Full board exam mock trials", false)
                            ),
                            strategicActionPlan = if (actionPlan.isNotBlank()) actionPlan else "Daily structured revision blocks using SUBHXAXM tools.",
                            expectedOutcome = "Attaining $targetPct% target score on board examinations."
                        )
                        onCreate(newProp)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SubhxPrimary),
                modifier = Modifier.testTag("confirm_create_proposal_btn")
            ) {
                Text("Create Proposal")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = SubhxTextMuted)
            }
        }
    )
}

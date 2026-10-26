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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.model.Chapter
import com.example.data.model.ChapterStatus
import com.example.data.model.Subject
import com.example.ui.theme.*

@Composable
fun ChapterTrackerScreen(
    subjects: List<Subject>,
    onUpdateChapterStatus: (String, ChapterStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubjectId by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf<ChapterStatus?>(null) }

    val currentSubjects = if (selectedSubjectId == null) {
        subjects
    } else {
        subjects.filter { it.id == selectedSubjectId }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SubhxBackground)
            .padding(horizontal = 16.dp)
            .testTag("chapter_tracker_screen")
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Title
        Text(
            text = "Class 10 Syllabus Tracker",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = SubhxText
        )
        Text(
            text = "Track completion status across NCERT & OAV prescribed textbooks",
            style = MaterialTheme.typography.bodySmall,
            color = SubhxTextMuted
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search chapters e.g. 'The Proposal', 'Trigonometry'...", fontSize = 13.sp) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = SubhxTextMuted)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = SubhxText,
                unfocusedTextColor = SubhxText,
                focusedBorderColor = SubhxSecondary,
                unfocusedBorderColor = SubhxCardBorder,
                focusedContainerColor = SubhxSurface,
                unfocusedContainerColor = SubhxSurface
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_chapter_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Subject Horizontal Selector
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                SubjectTabPill(
                    name = "All Subjects (${subjects.size})",
                    isSelected = selectedSubjectId == null,
                    onClick = { selectedSubjectId = null }
                )
            }
            items(subjects) { subj ->
                val completedCount = subj.chapters.count { it.status == ChapterStatus.COMPLETED }
                SubjectTabPill(
                    name = "${subj.name} ($completedCount/${subj.chapters.size})",
                    isSelected = selectedSubjectId == subj.id,
                    onClick = { selectedSubjectId = subj.id }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Chapters List grouped by subject
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            currentSubjects.forEach { subj ->
                val matchingChapters = subj.chapters.filter { chap ->
                    (searchQuery.isBlank() || chap.name.contains(searchQuery, ignoreCase = true) || chap.book.contains(searchQuery, ignoreCase = true)) &&
                            (selectedStatusFilter == null || chap.status == selectedStatusFilter)
                }

                if (matchingChapters.isNotEmpty()) {
                    item(key = subj.id) {
                        SubjectSectionHeader(
                            subject = subj,
                            matchingCount = matchingChapters.size
                        )
                    }

                    items(matchingChapters, key = { it.id }) { chapter ->
                        ChapterCardRow(
                            chapter = chapter,
                            onStatusSelected = { newStatus ->
                                onUpdateChapterStatus(chapter.id, newStatus)
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
fun SubjectTabPill(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) SubhxSecondary else SubhxSurface)
            .border(
                1.dp,
                if (isSelected) SubhxSecondary else SubhxCardBorder,
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) SubhxBackground else SubhxTextSecondary
        )
    }
}

@Composable
fun SubjectSectionHeader(subject: Subject, matchingCount: Int) {
    val total = subject.chapters.size.coerceAtLeast(1)
    val completed = subject.chapters.count { it.status == ChapterStatus.COMPLETED }
    val progress = completed.toFloat() / total.toFloat()

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SubhxSurfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = subject.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SubhxText
                    )
                    Text(
                        text = subject.defaultBook,
                        fontSize = 11.sp,
                        color = SubhxTextMuted
                    )
                }

                Text(
                    text = "$completed / $total completed (${(progress * 100).toInt()}%)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SubhxSecondary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = SubhxSecondary,
                trackColor = SubhxBackground,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun ChapterCardRow(
    chapter: Chapter,
    onStatusSelected: (ChapterStatus) -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SubhxSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SubhxCardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("chapter_item_${chapter.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chapter.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = SubhxText
                    )
                    Text(
                        text = chapter.book,
                        fontSize = 11.sp,
                        color = SubhxTextMuted
                    )
                }

                // Status Badge with Dropdown
                Box {
                    val (statusBg, statusFg) = when (chapter.status) {
                        ChapterStatus.COMPLETED -> Pair(SubhxSuccess.copy(alpha = 0.2f), SubhxSuccess)
                        ChapterStatus.REVISION -> Pair(SubhxSecondary.copy(alpha = 0.2f), SubhxSecondary)
                        ChapterStatus.LEARNING -> Pair(SubhxWarning.copy(alpha = 0.2f), SubhxWarning)
                        ChapterStatus.NOT_STARTED -> Pair(SubhxSurfaceVariant, SubhxTextMuted)
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(statusBg)
                            .clickable { expandedMenu = true }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = chapter.status.label,
                            color = statusFg,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "Change Status",
                            tint = statusFg,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expandedMenu,
                        onDismissRequest = { expandedMenu = false },
                        modifier = Modifier.background(SubhxSurface)
                    ) {
                        ChapterStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = status.label,
                                        color = if (status == chapter.status) SubhxSecondary else SubhxText,
                                        fontWeight = if (status == chapter.status) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    onStatusSelected(status)
                                    expandedMenu = false
                                }
                            )
                        }
                    }
                }
            }

            // Optional note preview (e.g. for "The Proposal" play)
            if (chapter.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SubhxCardBg)
                        .padding(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Notes",
                        tint = SubhxPrimaryLight,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = chapter.notes,
                        fontSize = 11.sp,
                        color = SubhxTextSecondary
                    )
                }
            }
        }
    }
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.repository.AppPreferencesRepository
import com.example.ui.components.MainDestination
import com.example.ui.components.SubhxBottomNav
import com.example.ui.components.SubhxTopBar
import com.example.ui.screens.AiTutorScreen
import com.example.ui.screens.ChapterTrackerScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.PracticeAndToolsScreen
import com.example.ui.screens.ProposalScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SubhxBackground

class MainActivity : ComponentActivity() {

    private lateinit var repository: AppPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository = AppPreferencesRepository(applicationContext)

        setContent {
            MyApplicationTheme {
                MainAppContent(repository = repository)
            }
        }
    }
}

@Composable
fun MainAppContent(repository: AppPreferencesRepository) {
    var currentDestination by remember { mutableStateOf(MainDestination.DASHBOARD) }

    val subjects by repository.subjects.collectAsState()
    val proposals by repository.proposals.collectAsState()
    val dailyTasks by repository.dailyTasks.collectAsState()
    val mistakes by repository.mistakes.collectAsState()
    val mockHistory by repository.mockHistory.collectAsState()
    val streakDays by repository.streakDays.collectAsState()
    val examCountdownDays by repository.examCountdownDays.collectAsState()
    val targetPercentage by repository.targetPercentage.collectAsState()

    // Handle back button to return to Dashboard if on a sub-screen
    BackHandler(enabled = currentDestination != MainDestination.DASHBOARD) {
        currentDestination = MainDestination.DASHBOARD
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(SubhxBackground),
        topBar = {
            SubhxTopBar(
                streakDays = streakDays,
                examCountdownDays = examCountdownDays,
                targetPercentage = targetPercentage
            )
        },
        bottomBar = {
            SubhxBottomNav(
                currentDestination = currentDestination,
                onDestinationSelected = { destination ->
                    currentDestination = destination
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentDestination) {
                MainDestination.DASHBOARD -> {
                    DashboardScreen(
                        subjects = subjects,
                        dailyTasks = dailyTasks,
                        streakDays = streakDays,
                        examCountdownDays = examCountdownDays,
                        targetPercentage = targetPercentage,
                        onNavigateTo = { dest -> currentDestination = dest },
                        onToggleTask = { taskId -> repository.toggleTask(taskId) },
                        onAddTask = { title, subject, mins -> repository.addTask(title, subject, mins) },
                        onUpdateExamDays = { days -> repository.updateExamDays(days) },
                        onUpdateTargetPct = { pct -> repository.updateTargetPercentage(pct) }
                    )
                }

                MainDestination.PROPOSALS -> {
                    ProposalScreen(
                        proposals = proposals,
                        onAddProposal = { newProp -> repository.addCustomProposal(newProp) }
                    )
                }

                MainDestination.SYLLABUS -> {
                    ChapterTrackerScreen(
                        subjects = subjects,
                        onUpdateChapterStatus = { chapId, newStatus ->
                            repository.updateChapterStatus(chapId, newStatus)
                        }
                    )
                }

                MainDestination.AI_TUTOR -> {
                    AiTutorScreen()
                }

                MainDestination.PRACTICE -> {
                    PracticeAndToolsScreen(
                        mistakes = mistakes,
                        mockHistory = mockHistory,
                        onAddMistake = { sub, q, m, c -> repository.addMistake(sub, q, m, c) },
                        onToggleMistake = { mId -> repository.toggleMistakeResolved(mId) },
                        onRecordMockResult = { res -> repository.recordMockTestResult(res) }
                    )
                }
            }
        }
    }
}

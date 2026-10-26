package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.ChapterStatus
import com.example.data.model.DailyTask
import com.example.data.model.MistakeItem
import com.example.data.model.MockTestResult
import com.example.data.model.ProposalCategory
import com.example.data.model.ProposalMilestone
import com.example.data.model.StudyProposal
import com.example.data.model.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class AppPreferencesRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("subhxaxm_board_master_prefs", Context.MODE_PRIVATE)

    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()

    private val _proposals = MutableStateFlow<List<StudyProposal>>(emptyList())
    val proposals: StateFlow<List<StudyProposal>> = _proposals.asStateFlow()

    private val _dailyTasks = MutableStateFlow<List<DailyTask>>(emptyList())
    val dailyTasks: StateFlow<List<DailyTask>> = _dailyTasks.asStateFlow()

    private val _mistakes = MutableStateFlow<List<MistakeItem>>(emptyList())
    val mistakes: StateFlow<List<MistakeItem>> = _mistakes.asStateFlow()

    private val _mockHistory = MutableStateFlow<List<MockTestResult>>(emptyList())
    val mockHistory: StateFlow<List<MockTestResult>> = _mockHistory.asStateFlow()

    private val _streakDays = MutableStateFlow(14)
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    private val _targetPercentage = MutableStateFlow(85)
    val targetPercentage: StateFlow<Int> = _targetPercentage.asStateFlow()

    private val _examCountdownDays = MutableStateFlow(42)
    val examCountdownDays: StateFlow<Int> = _examCountdownDays.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _streakDays.value = prefs.getInt("key_streak", 14)
        _targetPercentage.value = prefs.getInt("key_target_pct", 85)
        _examCountdownDays.value = prefs.getInt("key_exam_countdown", 42)

        // Load subjects
        val defaultSubs = SyllabusData.getDefaultSubjects()
        val savedSubs = defaultSubs.map { subj ->
            val updatedChapters = subj.chapters.map { chap ->
                val savedStatus = prefs.getString("chap_status_${chap.id}", null)
                val status = if (savedStatus != null) {
                    try { ChapterStatus.valueOf(savedStatus) } catch (e: Exception) { chap.status }
                } else chap.status

                val savedPct = prefs.getInt("chap_pct_${chap.id}", chap.completedPercentage)
                val savedNotes = prefs.getString("chap_notes_${chap.id}", chap.notes) ?: chap.notes
                chap.copy(status = status, completedPercentage = savedPct, notes = savedNotes)
            }
            subj.copy(chapters = updatedChapters)
        }
        _subjects.value = savedSubs

        // Load proposals
        _proposals.value = ProposalTemplatesData.getDefaultProposals().toMutableList()

        // Load daily tasks
        _dailyTasks.value = listOf(
            DailyTask("dt_1", "Review 'The Proposal' play 5-mark answer framework", "English", 35, true),
            DailyTask("dt_2", "Derive Quadratic Formula & solve 5 AP problems", "Mathematics", 45, true),
            DailyTask("dt_3", "Balance 5 Redox equations from Chapter 1", "Science", 30, false),
            DailyTask("dt_4", "Map work: Nationalism in India historical sessions", "History", 25, false),
            DailyTask("dt_5", "Complete 15-minute quick mock test on Science", "Mock Tests", 15, false)
        )

        // Load mistakes
        _mistakes.value = listOf(
            MistakeItem("m_1", "Science", "Refraction Index formula sign", "Forgot that refractive index is ratio of speeds, got fraction inverted", "n = c / v. For dense mediums n > 1.", true),
            MistakeItem("m_2", "Mathematics", "AP Sum formula when 'l' is known", "Re-calculated (n-1)d needlessly instead of using S_n = n/2(a + l)", "Directly use S_n = (n/2)(a + l) when last term is already given.", false),
            MistakeItem("m_3", "English", "Character traits of Stepan Chubukov", "Described him as purely villainous", "He is affectionate ('my darling', 'angel') yet opportunistic and volatile ('grabber', 'pettifogger').", true)
        )

        // Initial mock history
        _mockHistory.value = listOf(
            MockTestResult("mock_1", System.currentTimeMillis() - 86400000L * 2, 10, 8, 80, 540, mapOf("Science" to 4, "Mathematics" to 4)),
            MockTestResult("mock_2", System.currentTimeMillis() - 86400000L, 10, 9, 90, 480, mapOf("English" to 5, "Science" to 4))
        )
    }

    fun updateChapterStatus(chapterId: String, newStatus: ChapterStatus) {
        prefs.edit().putString("chap_status_$chapterId", newStatus.name).apply()
        val newPct = when (newStatus) {
            ChapterStatus.COMPLETED -> 100
            ChapterStatus.REVISION -> 75
            ChapterStatus.LEARNING -> 40
            ChapterStatus.NOT_STARTED -> 0
        }
        prefs.edit().putInt("chap_pct_$chapterId", newPct).apply()

        _subjects.value = _subjects.value.map { subj ->
            subj.copy(chapters = subj.chapters.map { chap ->
                if (chap.id == chapterId) chap.copy(status = newStatus, completedPercentage = newPct)
                else chap
            })
        }
    }

    fun addCustomProposal(proposal: StudyProposal) {
        val current = _proposals.value.toMutableList()
        current.add(0, proposal)
        _proposals.value = current
    }

    fun toggleTask(taskId: String) {
        _dailyTasks.value = _dailyTasks.value.map { task ->
            if (task.id == taskId) task.copy(isCompleted = !task.isCompleted)
            else task
        }
    }

    fun addTask(title: String, subject: String, estimatedMin: Int) {
        val newTask = DailyTask("dt_${System.currentTimeMillis()}", title, subject, estimatedMin, false)
        _dailyTasks.value = listOf(newTask) + _dailyTasks.value
    }

    fun addMistake(subject: String, question: String, mistake: String, correct: String) {
        val newMistake = MistakeItem("m_${System.currentTimeMillis()}", subject, question, mistake, correct, false)
        _mistakes.value = listOf(newMistake) + _mistakes.value
    }

    fun toggleMistakeResolved(mistakeId: String) {
        _mistakes.value = _mistakes.value.map { m ->
            if (m.id == mistakeId) m.copy(isResolved = !m.isResolved)
            else m
        }
    }

    fun recordMockTestResult(result: MockTestResult) {
        _mockHistory.value = listOf(result) + _mockHistory.value
    }

    fun updateExamDays(days: Int) {
        _examCountdownDays.value = days
        prefs.edit().putInt("key_exam_countdown", days).apply()
    }

    fun updateTargetPercentage(pct: Int) {
        _targetPercentage.value = pct
        prefs.edit().putInt("key_target_pct", pct).apply()
    }
}

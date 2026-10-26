package com.example.data.model

enum class ProposalCategory(val label: String) {
    EXAM_STRATEGY("Exam Strategy"),
    LITERATURE_STUDY("Literature Analysis"),
    REVISION_SPRINT("Revision Sprint"),
    CUSTOM_PROPOSAL("Custom Study Proposal")
}

data class ProposalMilestone(
    val title: String,
    val targetDays: Int,
    val description: String,
    val isDone: Boolean = false
)

data class StudyProposal(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: ProposalCategory,
    val targetScorePercentage: Int = 85,
    val durationDays: Int = 15,
    val dailyHoursGoal: Int = 8,
    val targetBoard: String = "OAV Odisha (Class 10)",
    val executiveSummary: String,
    val keyObjectives: List<String>,
    val milestones: List<ProposalMilestone>,
    val strategicActionPlan: String,
    val expectedOutcome: String,
    val authorPledge: String = "Committed to consistent daily study and excellence.",
    val createdAt: Long = System.currentTimeMillis(),
    val isApproved: Boolean = true
)

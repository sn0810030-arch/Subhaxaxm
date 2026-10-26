package com.example.data.model

enum class QuestionType(val label: String, val defaultMarks: Int) {
    MCQ("MCQ", 1),
    ONE_MARK("1-Mark", 1),
    TWO_MARK("2-Mark", 2),
    THREE_MARK("3-Mark", 3),
    FIVE_MARK("5-Mark", 5),
    NUMERICAL("Numerical", 3),
    CASE_BASED("Case-Based", 4)
}

data class QuestionItem(
    val id: String,
    val subjectId: String,
    val chapterName: String,
    val type: QuestionType,
    val questionText: String,
    val options: List<String> = emptyList(), // For MCQ
    val correctOptionIndex: Int = -1,       // For MCQ
    val modelAnswer: String,
    val explanation: String = "",
    val marks: Int = 1,
    val difficulty: String = "Medium",       // Easy, Medium, Hard
    val isVerifiedPYQ: Boolean = false       // Previous Year Question
)

data class MockQuestion(
    val question: QuestionItem,
    val selectedOptionIndex: Int? = null,
    val isMarkedForReview: Boolean = false
)

data class MockTestResult(
    val testId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val totalQuestions: Int,
    val correctAnswers: Int,
    val scorePercentage: Int,
    val timeTakenSeconds: Long,
    val subjectBreakdown: Map<String, Int> = emptyMap()
)

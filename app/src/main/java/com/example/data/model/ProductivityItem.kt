package com.example.data.model

data class FormulaItem(
    val id: String,
    val subject: String,
    val topic: String,
    val title: String,
    val formulaLatexOrText: String,
    val description: String,
    val units: String = "",
    val workedExample: String = ""
)

data class DailyTask(
    val id: String,
    val title: String,
    val subject: String,
    val estimatedMinutes: Int = 30,
    val isCompleted: Boolean = false
)

data class MistakeItem(
    val id: String,
    val subject: String,
    val questionOrTopic: String,
    val whatWentWrong: String,
    val correctMethod: String,
    val isResolved: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String,
    val sender: String, // "user" or "ai"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isQuickSuggestion: Boolean = false
)

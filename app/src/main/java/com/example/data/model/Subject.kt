package com.example.data.model

enum class ChapterStatus(val label: String) {
    NOT_STARTED("Not Started"),
    LEARNING("Learning"),
    REVISION("Revision"),
    COMPLETED("Completed")
}

data class Chapter(
    val id: String,
    val name: String,
    val book: String,
    val status: ChapterStatus = ChapterStatus.NOT_STARTED,
    val notes: String = "",
    val completedPercentage: Int = 0
)

data class Subject(
    val id: String,
    val name: String,
    val defaultBook: String,
    val iconName: String,
    val priority: String = "medium", // high, medium, low
    val chapters: List<Chapter> = emptyList()
)

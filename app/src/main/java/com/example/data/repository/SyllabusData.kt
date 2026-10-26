package com.example.data.repository

import com.example.data.model.Chapter
import com.example.data.model.ChapterStatus
import com.example.data.model.Subject

object SyllabusData {

    fun getDefaultSubjects(): List<Subject> {
        return listOf(
            Subject(
                id = "mathematics",
                name = "Mathematics",
                defaultBook = "NCERT Mathematics",
                iconName = "Calculate",
                priority = "high",
                chapters = listOf(
                    "Real Numbers",
                    "Polynomials",
                    "Pair of Linear Equations in Two Variables",
                    "Quadratic Equations",
                    "Arithmetic Progressions",
                    "Triangles",
                    "Coordinate Geometry",
                    "Introduction to Trigonometry",
                    "Some Applications of Trigonometry",
                    "Circles",
                    "Areas Related to Circles",
                    "Surface Areas and Volumes",
                    "Statistics",
                    "Probability"
                ).mapIndexed { idx, name ->
                    Chapter(
                        id = "math_$idx",
                        name = name,
                        book = "NCERT Mathematics",
                        status = if (idx < 5) ChapterStatus.COMPLETED else if (idx < 8) ChapterStatus.REVISION else if (idx < 11) ChapterStatus.LEARNING else ChapterStatus.NOT_STARTED,
                        completedPercentage = if (idx < 5) 100 else if (idx < 8) 75 else if (idx < 11) 40 else 0
                    )
                }
            ),
            Subject(
                id = "science",
                name = "Science",
                defaultBook = "NCERT Science",
                iconName = "Science",
                priority = "high",
                chapters = listOf(
                    "Chemical Reactions and Equations",
                    "Acids, Bases and Salts",
                    "Metals and Non-metals",
                    "Carbon and Its Compounds",
                    "Life Processes",
                    "Control and Coordination",
                    "How do Organisms Reproduce?",
                    "Heredity",
                    "Light – Reflection and Refraction",
                    "The Human Eye and the Colourful World",
                    "Electricity",
                    "Magnetic Effects of Electric Current",
                    "Our Environment"
                ).mapIndexed { idx, name ->
                    Chapter(
                        id = "sci_$idx",
                        name = name,
                        book = "NCERT Science",
                        status = if (idx < 4) ChapterStatus.COMPLETED else if (idx < 7) ChapterStatus.REVISION else if (idx < 10) ChapterStatus.LEARNING else ChapterStatus.NOT_STARTED,
                        completedPercentage = if (idx < 4) 100 else if (idx < 7) 80 else if (idx < 10) 50 else 0
                    )
                }
            ),
            Subject(
                id = "history",
                name = "History",
                defaultBook = "India and the Contemporary World – II",
                iconName = "HistoryEdu",
                priority = "medium",
                chapters = listOf(
                    "The Rise of Nationalism in Europe",
                    "Nationalism in India",
                    "The Making of a Global World",
                    "The Age of Industrialisation",
                    "Print Culture and the Modern World"
                ).mapIndexed { idx, name ->
                    Chapter(
                        id = "hist_$idx",
                        name = name,
                        book = "India and the Contemporary World – II",
                        status = if (idx < 2) ChapterStatus.COMPLETED else ChapterStatus.LEARNING,
                        completedPercentage = if (idx < 2) 100 else 60
                    )
                }
            ),
            Subject(
                id = "geography",
                name = "Geography",
                defaultBook = "Contemporary India – II",
                iconName = "Public",
                priority = "medium",
                chapters = listOf(
                    "Resources and Development",
                    "Forest and Wildlife Resources",
                    "Water Resources",
                    "Agriculture",
                    "Minerals and Energy Resources",
                    "Manufacturing Industries",
                    "Lifelines of National Economy"
                ).mapIndexed { idx, name ->
                    Chapter(
                        id = "geo_$idx",
                        name = name,
                        book = "Contemporary India – II",
                        status = if (idx < 3) ChapterStatus.COMPLETED else ChapterStatus.LEARNING,
                        completedPercentage = if (idx < 3) 100 else 45
                    )
                }
            ),
            Subject(
                id = "political-science",
                name = "Political Science",
                defaultBook = "Democratic Politics – II",
                iconName = "Gavel",
                priority = "medium",
                chapters = listOf(
                    "Power Sharing",
                    "Federalism",
                    "Gender, Religion and Caste",
                    "Political Parties",
                    "Outcomes of Democracy"
                ).mapIndexed { idx, name ->
                    Chapter(
                        id = "pol_$idx",
                        name = name,
                        book = "Democratic Politics – II",
                        status = if (idx < 2) ChapterStatus.COMPLETED else ChapterStatus.REVISION,
                        completedPercentage = if (idx < 2) 100 else 70
                    )
                }
            ),
            Subject(
                id = "economics",
                name = "Economics",
                defaultBook = "Understanding Economic Development",
                iconName = "TrendingUp",
                priority = "medium",
                chapters = listOf(
                    "Development",
                    "Sectors of the Indian Economy",
                    "Money and Credit",
                    "Globalisation and the Indian Economy",
                    "Consumer Rights"
                ).mapIndexed { idx, name ->
                    Chapter(
                        id = "eco_$idx",
                        name = name,
                        book = "Understanding Economic Development",
                        status = if (idx < 3) ChapterStatus.COMPLETED else ChapterStatus.NOT_STARTED,
                        completedPercentage = if (idx < 3) 100 else 0
                    )
                }
            ),
            Subject(
                id = "english",
                name = "English",
                defaultBook = "First Flight & Footprints Without Feet",
                iconName = "MenuBook",
                priority = "high",
                chapters = listOf(
                    // First Flight
                    Chapter("eng_ff_1", "A Letter to God", "First Flight", ChapterStatus.COMPLETED, completedPercentage = 100),
                    Chapter("eng_ff_2", "Nelson Mandela: Long Walk to Freedom", "First Flight", ChapterStatus.COMPLETED, completedPercentage = 100),
                    Chapter("eng_ff_3", "Two Stories about Flying", "First Flight", ChapterStatus.COMPLETED, completedPercentage = 100),
                    Chapter("eng_ff_4", "From the Diary of Anne Frank", "First Flight", ChapterStatus.REVISION, completedPercentage = 80),
                    Chapter("eng_ff_5", "Glimpses of India", "First Flight", ChapterStatus.REVISION, completedPercentage = 80),
                    Chapter("eng_ff_6", "Mijbil the Otter", "First Flight", ChapterStatus.LEARNING, completedPercentage = 50),
                    Chapter("eng_ff_7", "Madam Rides the Bus", "First Flight", ChapterStatus.LEARNING, completedPercentage = 60),
                    Chapter("eng_ff_8", "The Sermon at Benares", "First Flight", ChapterStatus.REVISION, completedPercentage = 85),
                    Chapter("eng_ff_9", "The Proposal (Play by Anton Chekhov)", "First Flight", ChapterStatus.COMPLETED, completedPercentage = 100, notes = "Farce play. Key characters: Lomov, Natalya, Chubukov. Disputes: Oxen Meadows, Guess vs Squeezer."),
                    // Footprints Without Feet
                    Chapter("eng_fw_1", "A Triumph of Surgery", "Footprints Without Feet", ChapterStatus.COMPLETED, completedPercentage = 100),
                    Chapter("eng_fw_2", "The Thief's Story", "Footprints Without Feet", ChapterStatus.COMPLETED, completedPercentage = 100),
                    Chapter("eng_fw_3", "The Midnight Visitor", "Footprints Without Feet", ChapterStatus.REVISION, completedPercentage = 75),
                    Chapter("eng_fw_4", "A Question of Trust", "Footprints Without Feet", ChapterStatus.LEARNING, completedPercentage = 40),
                    Chapter("eng_fw_5", "Footprints Without Feet", "Footprints Without Feet", ChapterStatus.NOT_STARTED, completedPercentage = 0),
                    Chapter("eng_fw_6", "The Making of a Scientist", "Footprints Without Feet", ChapterStatus.NOT_STARTED, completedPercentage = 0),
                    Chapter("eng_fw_7", "The Necklace", "Footprints Without Feet", ChapterStatus.COMPLETED, completedPercentage = 100),
                    Chapter("eng_fw_8", "Bholi", "Footprints Without Feet", ChapterStatus.COMPLETED, completedPercentage = 100),
                    Chapter("eng_fw_9", "The Book That Saved the Earth", "Footprints Without Feet", ChapterStatus.NOT_STARTED, completedPercentage = 0)
                )
            ),
            Subject(
                id = "odia",
                name = "Odia (MIL)",
                defaultBook = "Sahitya Sindhu (Odisha Board)",
                iconName = "AutoStories",
                priority = "medium",
                chapters = listOf(
                    "Bande Utkala Janani (Lakshmikanta)",
                    "Janmabhumi (Dr. Krishna Chandra Panigrahi)",
                    "Subha (Rabindranath Tagore / Translation)",
                    "Sabuja Janha (Odia Prose)",
                    "Chilika re Sayantana (Radhanath Ray)",
                    "Brikshya Mahatmya (Jagannatha Dasa)",
                    "Kanta Kabi Lakshmikanta Mohapatra Jeevani",
                    "Byakarana: Sandhi, Samasa, Bakya Bichara"
                ).mapIndexed { idx, name ->
                    Chapter(
                        id = "odia_$idx",
                        name = name,
                        book = "Sahitya Sindhu",
                        status = if (idx < 4) ChapterStatus.COMPLETED else ChapterStatus.LEARNING,
                        completedPercentage = if (idx < 4) 100 else 50
                    )
                }
            ),
            Subject(
                id = "information-technology",
                name = "Information Technology",
                defaultBook = "IT (Subject Code 402)",
                iconName = "Computer",
                priority = "medium",
                chapters = listOf(
                    "Digital Documentation (Advanced)",
                    "Electronic Spreadsheet (Advanced)",
                    "Database Management System (RDBMS)",
                    "Web Applications and Security",
                    "Communication Skills - II",
                    "Self-Management Skills - II",
                    "ICT Skills - II",
                    "Entrepreneurial Skills - II"
                ).mapIndexed { idx, name ->
                    Chapter(
                        id = "it_$idx",
                        name = name,
                        book = "IT Code 402",
                        status = if (idx < 4) ChapterStatus.COMPLETED else ChapterStatus.NOT_STARTED,
                        completedPercentage = if (idx < 4) 100 else 0
                    )
                }
            )
        )
    }
}

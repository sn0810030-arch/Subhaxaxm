package com.example.data.repository

import com.example.data.model.ProposalCategory
import com.example.data.model.ProposalMilestone
import com.example.data.model.StudyProposal

object ProposalTemplatesData {

    fun getDefaultProposals(): List<StudyProposal> {
        return listOf(
            StudyProposal(
                id = "prop_1",
                title = "OAV Odisha 85%+ Board Exam Mastery Proposal",
                subtitle = "15-Day Strategic Revision & High-Yield Blueprint",
                category = ProposalCategory.EXAM_STRATEGY,
                targetScorePercentage = 85,
                durationDays = 15,
                dailyHoursGoal = 8,
                targetBoard = "OAV Odisha (Class 10)",
                executiveSummary = "A comprehensive academic preparation proposal designed to systematically bridge conceptual gaps, elevate mock exam accuracy, and secure an aggregate score exceeding 85% across all 9 subjects.",
                keyObjectives = listOf(
                    "Achieve 100% syllabus coverage across high-priority subjects (Maths, Science, English)",
                    "Maintain 8 hours of productive daily study using structured Pomodoro blocks",
                    "Complete at least 5 full-length timed mock tests with minimum 80% accuracy",
                    "Review and resolve all entries in the Mistake Notebook prior to exam day"
                ),
                milestones = listOf(
                    ProposalMilestone("Phase 1: High-Weightage Concept Consolidation", 5, "Finish Real Numbers, Chemical Reactions, Electricity, and First Flight revision", isDone = true),
                    ProposalMilestone("Phase 2: Question Bank Mastery & Numericals", 10, "Solve 5-mark geometry proofs, science numericals, and social science case questions", isDone = false),
                    ProposalMilestone("Phase 3: Simulated Mock Exams & Weak Area Sprints", 15, "Take 3 full board simulation tests, analyze error patterns, and finalize formula recall", isDone = false)
                ),
                strategicActionPlan = """
                    • Morning Block (06:00 - 09:00): Mathematics formulas & NCERT exemplar problem solving.
                    • Midday Block (10:30 - 13:00): Science chemical reactions, ray diagrams, and biology life cycle diagrams.
                    • Afternoon Block (14:30 - 17:00): Social Science (History & Geography) timeline maps and structured answers.
                    • Evening Block (18:30 - 21:00): English literature analysis, grammar exercises, and Odia MIL grammar revision.
                    • Night Wrap-up (21:30 - 22:30): Mistake notebook revision and AI Tutor query resolution.
                """.trimIndent(),
                expectedOutcome = "Targeted aggregate percentage >= 85% with zero unrevised chapters and high confidence under timed exam conditions.",
                authorPledge = "I formally commit to following this 15-day proposal with discipline, integrity, and daily consistency."
            ),
            StudyProposal(
                id = "prop_2",
                title = "Literature Study Proposal: 'The Proposal' by Anton Chekhov",
                subtitle = "Character Dynamics, Farce Elements & 5-Mark Exam Answers",
                category = ProposalCategory.LITERATURE_STUDY,
                targetScorePercentage = 95,
                durationDays = 3,
                dailyHoursGoal = 4,
                targetBoard = "English First Flight (Class 10)",
                executiveSummary = "In-depth literary analysis and answer-structuring proposal focusing on Anton Pavlovich Chekhov's one-act farce 'The Proposal', exploring Russian landed gentry marriage customs, humor through trivial quarrels, and high-scoring character sketches.",
                keyObjectives = listOf(
                    "Analyze Lomov, Natalya, and Chubukov's personality flaws and economic motivations",
                    "Deconstruct the two major quarrels: The Oxen Meadows dispute and the hunting dog rivalry (Guess vs. Squeezer)",
                    "Master Anton Chekhov's use of dramatic irony, exaggeration, and slapstick elements",
                    "Prepare ready-to-write 5-mark answers on 'Marriage of convenience vs romantic love'"
                ),
                milestones = listOf(
                    ProposalMilestone("Character Sketch & Dramatis Personae", 1, "Detailed traits of Lomov (hypochondriac, nervous), Natalya (quarrelsome, proud), Chubukov (opportunistic patriarch)", isDone = true),
                    ProposalMilestone("The Two Quarrels Analysis", 2, "Examining how trivial pride overshadows life-changing marriage decisions", isDone = true),
                    ProposalMilestone("Board Exam Model Answers Practice", 3, "Drafting answers for value-based questions on greed and ego in interpersonal relationships", isDone = false)
                ),
                strategicActionPlan = """
                    1. Read the complete play highlighting key quotes: 'He's come to borrow money!', 'They're our Meadows!', 'Your Guess is old and ugly!'.
                    2. Contrast Lomov's formal dress suit with his chaotic physical ailments (palpitations, twitching).
                    3. Practice writing Chubukov's frantic blessing: 'Hurry up and get married!... Champagne! Champagne!'.
                """.trimIndent(),
                expectedOutcome = "Flawless score on all English First Flight drama/prose questions with rich textual citations.",
                authorPledge = "I will master the thematic depth and comedic mechanics of Chekhov's play for full marks in English."
            ),
            StudyProposal(
                id = "prop_3",
                title = "STEM Intensive Sprint: Mathematics & Science Drill",
                subtitle = "Step-by-Step Derivations & Chemical Equation Practice",
                category = ProposalCategory.REVISION_SPRINT,
                targetScorePercentage = 90,
                durationDays = 7,
                dailyHoursGoal = 6,
                targetBoard = "OAV Odisha (Class 10)",
                executiveSummary = "A rigorous, hands-on study proposal targeting full marks in numericals, geometric theorems (BPT, Tangents), and balancing complex redox chemical equations.",
                keyObjectives = listOf(
                    "Memorize all 42 core mathematics formulas and trigonometric identities",
                    "Solve 20 high-frequency physics numericals on mirror formula, refraction index, and Ohm's law",
                    "Balance 30 chemical equations from Acids, Bases, Salts and Carbon Compounds"
                ),
                milestones = listOf(
                    ProposalMilestone("Formula Library Rapid Drill", 2, "Flashcard memorization of mensuration, AP, and coordinate geometry", isDone = true),
                    ProposalMilestone("Theorem Proofs & Ray Diagrams", 4, "Drawing concave mirror image formations and proving Basic Proportionality Theorem", isDone = false),
                    ProposalMilestone("Timed Numerical Marathon", 7, "Solving 50 numericals under time pressure without calculator", isDone = false)
                ),
                strategicActionPlan = """
                    • Day 1-2: Quadratic equations & Arithmetic Progressions step-by-step solutions.
                    • Day 3-4: Light & Electricity circuits, series-parallel resistance calculations.
                    • Day 5-6: Life processes & carbon allotropes comparative tables.
                    • Day 7: Comprehensive STEM diagnostic test.
                """.trimIndent(),
                expectedOutcome = "Elimination of silly numerical mistakes and total mastery of high-weightage 5-mark derivation questions.",
                authorPledge = "I will write out every proof and numerical step without skipping calculations."
            )
        )
    }
}

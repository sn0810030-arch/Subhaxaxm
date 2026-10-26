package com.example.data.repository

import com.example.data.model.QuestionItem
import com.example.data.model.QuestionType

object QuestionBankData {

    fun getDefaultQuestions(): List<QuestionItem> {
        return listOf(
            // English - The Proposal
            QuestionItem(
                id = "q_eng_prop_1",
                subjectId = "english",
                chapterName = "The Proposal",
                type = QuestionType.MCQ,
                questionText = "Why does Ivan Vassilevitch Lomov visit Chubukov's house in evening dress?",
                options = listOf(
                    "To propose marriage to Chubukov's daughter Natalya",
                    "To borrow money for his agricultural estate",
                    "To file a legal suit regarding Oxen Meadows",
                    "To celebrate New Year's Eve"
                ),
                correctOptionIndex = 0,
                modelAnswer = "Lomov arrives wearing evening clothes with gloves to formally ask for Natalya Stepanovna's hand in marriage.",
                explanation = "Lomov came to Chubukov's house dressed in a formal suit to make a marriage proposal to Natalya.",
                marks = 1,
                difficulty = "Easy",
                isVerifiedPYQ = true
            ),
            QuestionItem(
                id = "q_eng_prop_2",
                subjectId = "english",
                chapterName = "The Proposal",
                type = QuestionType.THREE_MARK,
                questionText = "What was the initial dispute between Lomov and Natalya, and how did it escalate?",
                modelAnswer = "The initial dispute was over the ownership of the Oxen Meadows, a patch of land wedged between the Birchwoods and the Burnt Marsh. Lomov claimed they belonged to his aunt's grandmother, while Natalya insisted her family owned them for nearly 300 years. The argument escalated into personal insults, accusations of usurpation, and Lomov experiencing severe palpitations until he stormed out.",
                explanation = "Chekhov uses this dispute as a satire on petty egotism and greed among landowners.",
                marks = 3,
                difficulty = "Medium",
                isVerifiedPYQ = true
            ),
            QuestionItem(
                id = "q_eng_prop_3",
                subjectId = "english",
                chapterName = "The Proposal",
                type = QuestionType.FIVE_MARK,
                questionText = "Discuss the humorous and satirical elements in Anton Chekhov's play 'The Proposal'.",
                modelAnswer = "'The Proposal' is a farce that satirizes the shallow nature of Russian upper-class marriages of convenience. Humor arises from: 1) The contrast between Lomov's formal attire/noble romantic mission and his neurotic hypochondria; 2) The sudden escalation from courteous greetings into furious shouting matches over trivial Oxen Meadows and dogs (Guess vs Squeezer); 3) Natalya's comical reversal of hysterics the moment she discovers Lomov came to propose; and 4) Chubukov forcing their hands together while shouting 'Champagne!' to consummate the proposal even as they immediately resume arguing. The characters prioritize frivolous pride over marital harmony.",
                explanation = "Covers farce techniques, character hypocrisies, and structural irony in Chekhov's dramaturgy.",
                marks = 5,
                difficulty = "Hard",
                isVerifiedPYQ = true
            ),
            // Mathematics
            QuestionItem(
                id = "q_math_1",
                subjectId = "mathematics",
                chapterName = "Quadratic Equations",
                type = QuestionType.MCQ,
                questionText = "If the discriminant b² - 4ac > 0 and is not a perfect square, what is the nature of the roots of ax² + bx + c = 0?",
                options = listOf(
                    "Real, unequal, and irrational",
                    "Real and equal",
                    "Complex conjugate roots",
                    "Real, rational, and unequal"
                ),
                correctOptionIndex = 0,
                modelAnswer = "Real, unequal, and irrational roots.",
                explanation = "When D > 0 and not a square, the square root of D remains irrational, producing two distinct irrational conjugate roots.",
                marks = 1,
                difficulty = "Medium",
                isVerifiedPYQ = true
            ),
            QuestionItem(
                id = "q_math_2",
                subjectId = "mathematics",
                chapterName = "Arithmetic Progressions",
                type = QuestionType.NUMERICAL,
                questionText = "Find the 20th term of the AP: 2, 7, 12, ... and determine the sum of its first 20 terms.",
                modelAnswer = "First term a = 2, common difference d = 7 - 2 = 5.\na₂₀ = a + (20 - 1)d = 2 + 19(5) = 2 + 95 = 97.\nSum S₂₀ = (n/2)[2a + (n-1)d] = (20/2)[2(2) + 19(5)] = 10[4 + 95] = 10 × 99 = 990.",
                explanation = "Use standard AP formulas a_n = a + (n-1)d and S_n = n/2(2a + (n-1)d).",
                marks = 3,
                difficulty = "Medium",
                isVerifiedPYQ = true
            ),
            QuestionItem(
                id = "q_math_3",
                subjectId = "mathematics",
                chapterName = "Triangles",
                type = QuestionType.FIVE_MARK,
                questionText = "State and prove the Basic Proportionality Theorem (Thales Theorem).",
                modelAnswer = "Statement: If a line is drawn parallel to one side of a triangle intersecting the other two sides in distinct points, then the other two sides are divided in the same ratio.\nProof:\nGiven: ΔABC where DE || BC.\nConstruction: Join BE, CD and draw DM ⊥ AC and EN ⊥ AB.\nArea(ΔADE) = 1/2 × AD × EN.\nArea(ΔBDE) = 1/2 × DB × EN.\nRatio: Area(ΔADE)/Area(ΔBDE) = AD/DB ...(1)\nSimilarly: Area(ΔADE)/Area(ΔCDE) = AE/EC ...(2)\nSince ΔBDE and ΔCDE lie on the same base DE and between the same parallels DE and BC, Area(ΔBDE) = Area(ΔCDE).\nTherefore, from (1) and (2): AD/DB = AE/EC. (Hence Proved).",
                explanation = "Standard 5-mark geometry proof frequently asked in CBSE and OAV Odisha board exams.",
                marks = 5,
                difficulty = "Hard",
                isVerifiedPYQ = true
            ),
            // Science
            QuestionItem(
                id = "q_sci_1",
                subjectId = "science",
                chapterName = "Chemical Reactions and Equations",
                type = QuestionType.MCQ,
                questionText = "What type of reaction takes place when quicklime (CaO) reacts vigorously with water?",
                options = listOf(
                    "Combination and Exothermic reaction",
                    "Decomposition and Endothermic reaction",
                    "Displacement reaction",
                    "Double displacement reaction"
                ),
                correctOptionIndex = 0,
                modelAnswer = "Combination and Exothermic reaction: CaO(s) + H₂O(l) → Ca(OH)₂(aq) + Heat.",
                explanation = "Two reactants combine to form slaked lime with release of substantial thermal energy.",
                marks = 1,
                difficulty = "Easy",
                isVerifiedPYQ = true
            ),
            QuestionItem(
                id = "q_sci_2",
                subjectId = "science",
                chapterName = "Electricity",
                type = QuestionType.NUMERICAL,
                questionText = "An electric lamp of 100 Ω, a toaster of resistance 50 Ω, and a water filter of resistance 500 Ω are connected in parallel to a 220 V source. Find the equivalent resistance and total current.",
                modelAnswer = "In parallel: 1/R = 1/100 + 1/50 + 1/500 = (5 + 10 + 1)/500 = 16/500 = 4/125 Ω⁻¹.\nTherefore R = 125/4 = 31.25 Ω.\nTotal current I = V / R = 220 / 31.25 = 7.04 A.",
                explanation = "Parallel resistance formula 1/R_eq = sum(1/R_i) followed by Ohm's law V = IR.",
                marks = 3,
                difficulty = "Medium",
                isVerifiedPYQ = true
            ),
            QuestionItem(
                id = "q_sci_3",
                subjectId = "science",
                chapterName = "Life Processes",
                type = QuestionType.THREE_MARK,
                questionText = "Describe the role of hydrochloric acid, pepsin, and mucus in gastric juice inside the human stomach.",
                modelAnswer = "1) Hydrochloric Acid (HCl): Creates an acidic medium (pH 1.5-2.5) necessary for activating inactive pepsinogen into active pepsin, and eliminates ingested microbes.\n2) Pepsin: A proteolytic enzyme that digests dietary proteins into peptones and proteoses.\n3) Mucus: Coats and protects the inner stomach lining from the corrosive action of HCl under normal conditions.",
                explanation = "Key functions of gastric secretions in mammalian digestion.",
                marks = 3,
                difficulty = "Medium",
                isVerifiedPYQ = true
            ),
            // Social Science
            QuestionItem(
                id = "q_hist_1",
                subjectId = "history",
                chapterName = "Nationalism in India",
                type = QuestionType.MCQ,
                questionText = "Why was the Simon Commission boycotted by Indian political leaders in 1928?",
                options = listOf(
                    "It did not contain a single Indian member",
                    "It supported the Rowlatt Act",
                    "It refused to recognize Mahatma Gandhi",
                    "It aimed to divide Bengal again"
                ),
                correctOptionIndex = 0,
                modelAnswer = "It did not have a single Indian member; all members were British.",
                explanation = "The Indian Statutory Commission was all-White, leading to nationwide protests with slogans 'Go Back Simon'.",
                marks = 1,
                difficulty = "Easy",
                isVerifiedPYQ = true
            ),
            QuestionItem(
                id = "q_pol_1",
                subjectId = "political-science",
                chapterName = "Power Sharing",
                type = QuestionType.TWO_MARK,
                questionText = "Distinguish between horizontal and vertical power sharing in a democracy.",
                modelAnswer = "Horizontal Power Sharing: Power is divided among different organs of government at the same level (Legislature, Executive, Judiciary), ensuring checks and balances.\nVertical Power Sharing: Power is divided among governments at different vertical levels (Central/Union, State/Provincial, and Local government), representing federalism.",
                explanation = "Fundamental concepts of democratic power distribution.",
                marks = 2,
                difficulty = "Medium",
                isVerifiedPYQ = true
            )
        )
    }
}

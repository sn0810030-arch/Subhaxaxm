package com.example

import com.example.data.model.ChapterStatus
import com.example.data.model.QuestionType
import com.example.data.repository.FormulaData
import com.example.data.repository.ProposalTemplatesData
import com.example.data.repository.QuestionBankData
import com.example.data.repository.SyllabusData
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testSyllabusDataIntegrity() {
        val subjects = SyllabusData.getDefaultSubjects()
        assertTrue("Subjects list should not be empty", subjects.isNotEmpty())
        assertEquals(9, subjects.size)

        val englishSubject = subjects.find { it.id == "english" }
        assertNotNull("English subject must exist", englishSubject)

        val proposalChapter = englishSubject?.chapters?.find { it.name.contains("The Proposal") }
        assertNotNull("The Proposal chapter must exist in English", proposalChapter)
        assertEquals(ChapterStatus.COMPLETED, proposalChapter?.status)
    }

    @Test
    fun testProposalTemplatesData() {
        val proposals = ProposalTemplatesData.getDefaultProposals()
        assertTrue("Proposals should have default entries", proposals.isNotEmpty())

        val chekhovProposal = proposals.find { it.title.contains("The Proposal") }
        assertNotNull("Literary study proposal for The Proposal must exist", chekhovProposal)
        assertEquals(95, chekhovProposal?.targetScorePercentage)
        assertTrue(chekhovProposal?.milestones?.isNotEmpty() == true)

        val masterProposal = proposals.find { it.title.contains("85%+") }
        assertNotNull("85%+ Mastery Proposal must exist", masterProposal)
        assertEquals(15, masterProposal?.durationDays)
    }

    @Test
    fun testQuestionBankAndFormulas() {
        val questions = QuestionBankData.getDefaultQuestions()
        assertTrue(questions.isNotEmpty())

        val proposalMcq = questions.find { it.chapterName == "The Proposal" && it.type == QuestionType.MCQ }
        assertNotNull(proposalMcq)
        assertEquals(0, proposalMcq?.correctOptionIndex)

        val formulas = FormulaData.getDefaultFormulas()
        assertTrue(formulas.isNotEmpty())

        val chemicalEquations = FormulaData.getChemicalEquations()
        assertTrue(chemicalEquations.isNotEmpty())
    }
}

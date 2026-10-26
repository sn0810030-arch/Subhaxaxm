package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AiTutorScreen(
    modifier: Modifier = Modifier
) {
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = "msg_0",
                sender = "ai",
                text = "Namaskar! I am SUBHXAXM AI, your dedicated Class 10 study tutor for OAV Odisha examinations. I teach step by step across Mathematics, Science, Social Science, English (including 'The Proposal' play analysis), Odia MIL, and IT. How can I help you excel today?"
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val quickPrompts = listOf(
        "Explain 'The Proposal' play by Anton Chekhov",
        "Derive Quadratic Formula step-by-step",
        "Explain Ohm's Law and solve numericals",
        "Balance quicklime & water chemical equation",
        "Summary of Nationalism in India",
        "State and prove Thales Theorem (BPT)"
    )

    fun sendUserMessage(text: String) {
        if (text.isBlank()) return
        messages.add(ChatMessage(id = "msg_${System.currentTimeMillis()}", sender = "user", text = text))
        inputText = ""
        isThinking = true

        scope.launch {
            listState.animateScrollToItem((messages.size - 1).coerceAtLeast(0))
            delay(600) // Realistic tutoring reflection delay

            val aiResponse = generateAiTutorResponse(text)
            messages.add(ChatMessage(id = "msg_${System.currentTimeMillis()}", sender = "ai", text = aiResponse))
            isThinking = false
            listState.animateScrollToItem((messages.size - 1).coerceAtLeast(0))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SubhxBackground)
            .testTag("ai_tutor_screen")
    ) {
        // Tutor Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SubhxSurface)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(SubhxPrimary, SubhxSecondary))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "Tutor Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SUBHXAXM AI Tutor",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = SubhxText
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SubhxSuccess.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Online", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SubhxSuccess)
                    }
                }
                Text(
                    text = "OAV Odisha Class 10 Syllabus Specialist",
                    fontSize = 11.sp,
                    color = SubhxSecondary
                )
            }
        }

        // Quick Suggestion Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(quickPrompts) { prompt ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(SubhxSurface)
                        .border(1.dp, SubhxCardBorder, RoundedCornerShape(16.dp))
                        .clickable { sendUserMessage(prompt) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SubhxSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = prompt,
                            fontSize = 11.sp,
                            color = SubhxTextSecondary
                        )
                    }
                }
            }
        }

        // Chat Messages Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message = message)
            }

            if (isThinking) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(SubhxSurface)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "SUBHXAXM AI is thinking & structuring answer...",
                                fontSize = 12.sp,
                                color = SubhxSecondary
                            )
                        }
                    }
                }
            }
        }

        // Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SubhxSurface)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Ask any syllabus question...", fontSize = 13.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = SubhxText,
                    unfocusedTextColor = SubhxText,
                    focusedBorderColor = SubhxSecondary,
                    unfocusedBorderColor = SubhxCardBorder,
                    focusedContainerColor = SubhxCardBg,
                    unfocusedContainerColor = SubhxCardBg
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_tutor_input")
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { sendUserMessage(inputText) },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (inputText.isNotBlank()) SubhxPrimary else SubhxSurfaceVariant)
                    .testTag("ai_tutor_send_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (inputText.isNotBlank()) Color.White else SubhxTextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val isUser = message.sender == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .background(if (isUser) SubhxPrimary else SubhxSurface)
                .border(
                    1.dp,
                    if (isUser) SubhxPrimaryDark else SubhxCardBorder,
                    RoundedCornerShape(16.dp)
                )
                .padding(14.dp)
                .fillMaxWidth(if (isUser) 0.85f else 0.95f)
        ) {
            Column {
                if (!isUser) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = null,
                            tint = SubhxSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SUBHXAXM AI Tutor",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SubhxSecondary
                        )
                    }
                }
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubhxText,
                    lineHeight = 21.sp
                )
            }
        }
    }
}

private fun generateAiTutorResponse(query: String): String {
    val lower = query.lowercase()

    return when {
        lower.contains("the proposal") || lower.contains("lomov") || lower.contains("natalya") || lower.contains("chekhov") -> {
            """
            📖 Anton Chekhov's 'The Proposal' (Class 10 English First Flight):
            
            1. Core Theme & Genre:
            A one-act farce satirizing Russian landed aristocracy where marriage is viewed as an economic transaction rather than love.
            
            2. Key Dramatis Personae:
            • Ivan Vassilevitch Lomov: A 35-year-old hypochondriac landowner dressed in evening clothes. He is nervous, prone to palpitations, twitching, and numbness.
            • Natalya Stepanovna: 25-year-old educated housekeeper, proud, stubborn, and highly argumentative.
            • Stepan Stepanovitch Chubukov: An opportunistic, volatile father who shifts from showering Lomov with fake affection ('my darling', 'angel') to cursing him ('villain', 'scarecrow').
            
            3. The Two Quarrels:
            • Dispute 1 (Oxen Meadows): A small strip between Birchwoods and Burnt Marsh. Lomov claims it was given by his aunt's grandmother; Natalya insists her family owned it for 300 years.
            • Dispute 2 (The Hunting Dogs): Lomov boasts his dog 'Guess' cost 125 roubles; Natalya mocks Guess as old and lame while boasting her dog 'Squeezer' is superior.
            
            4. Board Exam High-Yield Tip:
            Remember Chubukov forcing them to kiss and shouting 'Champagne! Champagne!' while they resume arguing over their dogs even at the moment of proposal!
            """.trimIndent()
        }

        lower.contains("quadratic") || lower.contains("discriminant") -> {
            """
            📐 Step-by-Step Quadratic Equation Derivation (Mathematics):
            
            Standard Form: ax² + bx + c = 0 (where a ≠ 0)
            
            Derivation by Completing the Square:
            1. Divide the entire equation by 'a':
               x² + (b/a)x + (c/a) = 0
            2. Move the constant to the RHS:
               x² + (b/a)x = -(c/a)
            3. Add (b / 2a)² to both sides:
               x² + 2·x·(b/2a) + (b/2a)² = (b²/4a²) - (c/a)
            4. Factor the LHS into a perfect square:
               (x + b/2a)² = (b² - 4ac) / (4a²)
            5. Take square roots on both sides:
               x + b/2a = ± √(b² - 4ac) / (2a)
            6. Solve for x:
               x = (-b ± √(b² - 4ac)) / (2a)
            
            Discriminant D = b² - 4ac:
            • D > 0: Two distinct real roots
            • D = 0: Two equal real roots
            • D < 0: No real roots
            """.trimIndent()
        }

        lower.contains("ohm") || lower.contains("electricity") || lower.contains("numerical") -> {
            """
            ⚡ Science Physics - Ohm's Law & Circuit Numericals:
            
            Ohm's Law:
            At constant temperature, current (I) flowing through a metallic conductor is directly proportional to the potential difference (V) across its ends:
            V ∝ I  =>  V = I × R
            
            Series vs Parallel Resistors:
            • Series: R_eq = R₁ + R₂ + ... + Rₙ (Current remains constant throughout)
            • Parallel: 1/R_eq = 1/R₁ + 1/R₂ + ... + 1/Rₙ (Voltage remains equal across all branches)
            
            Example Worked Numerical:
            Given: V = 220 V, I = 5 A
            Resistance R = V / I = 220 / 5 = 44 Ω.
            Power P = V × I = 220 × 5 = 1100 W = 1.1 kW.
            Heat generated in 1 hour (3600 s):
            H = P × t = 1100 W × 3600 s = 3.96 × 10⁶ Joules.
            """.trimIndent()
        }

        lower.contains("quicklime") || lower.contains("reaction") || lower.contains("chemical") -> {
            """
            🧪 Science Chemistry - Chemical Reactions & Equations:
            
            Reaction of Quicklime (Calcium Oxide) with Water:
            CaO(s) + H₂O(l) → Ca(OH)₂(aq) + Heat
            
            Key Characteristics:
            1. Type: Combination Reaction (two reactants combine into a single product: Slaked Lime).
            2. Enthalpy: Highly Exothermic (intense heat is liberated, boiling the water).
            3. Whitewashing Application:
               Ca(OH)₂ painted on walls slowly reacts with atmospheric CO₂:
               Ca(OH)₂(aq) + CO₂(g) → CaCO₃(s) + H₂O(l)
               This forms a shiny, protective white layer of Calcium Carbonate after 2 to 3 days!
            """.trimIndent()
        }

        lower.contains("thales") || lower.contains("triangle") || lower.contains("bpt") -> {
            """
            📐 Basic Proportionality Theorem (Thales Theorem):
            
            Statement:
            If a line is drawn parallel to one side of a triangle intersecting the other two sides at distinct points, then the other two sides are divided in the same ratio.
            
            Given: ΔABC where line DE || BC.
            To Prove: AD / DB = AE / EC.
            
            Construction: Join BE and CD. Draw perpendiculars EN ⊥ AB and DM ⊥ AC.
            
            Proof Steps:
            1. Area(ΔADE) = 1/2 × base × height = 1/2 × AD × EN
            2. Area(ΔBDE) = 1/2 × DB × EN
               Ratio: Area(ΔADE) / Area(ΔBDE) = AD / DB  --- (Eq 1)
            3. Area(ΔADE) = 1/2 × AE × DM
            4. Area(ΔCDE) = 1/2 × EC × DM
               Ratio: Area(ΔADE) / Area(ΔCDE) = AE / EC  --- (Eq 2)
            5. Since ΔBDE and ΔCDE share the same base DE and lie between parallel lines DE || BC:
               Area(ΔBDE) = Area(ΔCDE)
            From (1) and (2):
            AD / DB = AE / EC. (Hence Proved!)
            """.trimIndent()
        }

        lower.contains("simon") || lower.contains("nationalism") || lower.contains("history") -> {
            """
            📜 History - Nationalism in India (Class 10 OAV Odisha):
            
            Simon Commission Boycott (1928):
            • Formed by the British Government under Sir John Simon to review the functioning of the constitutional system in India.
            • Why Indians boycotted it: The commission had 7 British Members of Parliament and NOT A SINGLE INDIAN MEMBER.
            • Public Outcry: When the commission arrived in India in February 1928, it was greeted with black flags and slogans 'Go Back Simon'.
            • Tragic Event: Lala Lajpat Rai was severely assaulted during a peaceful police lathi charge at Lahore led by Saunders, and later succumbed to his injuries.
            """.trimIndent()
        }

        else -> {
            """
            💡 SUBHXAXM AI Tutor Explanation:
            
            Regarding "$query":
            
            • Key Concept: In the Class 10 OAV Odisha board syllabus, this topic carries consistent exam weightage.
            • Methodical Approach:
              1. Break down the core definition and verified textbook terms.
              2. Identify whether the question expects a 1-mark objective, a 3-mark analytical answer, or a 5-mark derivation/diagram.
              3. Always include relevant chemical equations, formulas with standard SI units, or textual citations.
            
            Would you like a step-by-step worked example or a 5-question practice quiz on this topic?
            """.trimIndent()
        }
    }
}

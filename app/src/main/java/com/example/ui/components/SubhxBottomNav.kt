package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SubhxCardBorder
import com.example.ui.theme.SubhxPrimary
import com.example.ui.theme.SubhxSecondary
import com.example.ui.theme.SubhxSurface
import com.example.ui.theme.SubhxText
import com.example.ui.theme.SubhxTextMuted

enum class MainDestination(val label: String, val icon: ImageVector) {
    DASHBOARD("Home", Icons.Default.Dashboard),
    PROPOSALS("Proposals", Icons.Default.Assignment),
    SYLLABUS("Syllabus", Icons.Default.Checklist),
    AI_TUTOR("AI Tutor", Icons.Default.SmartToy),
    PRACTICE("Practice", Icons.Default.Quiz)
}

@Composable
fun SubhxBottomNav(
    currentDestination: MainDestination,
    onDestinationSelected: (MainDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = SubhxSurface,
        modifier = modifier.testTag("subhx_bottom_nav")
    ) {
        MainDestination.values().forEach { destination ->
            val isSelected = currentDestination == destination
            NavigationBarItem(
                selected = isSelected,
                onClick = { onDestinationSelected(destination) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label
                    )
                },
                label = {
                    Text(
                        text = destination.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SubhxSecondary,
                    selectedTextColor = SubhxSecondary,
                    unselectedIconColor = SubhxTextMuted,
                    unselectedTextColor = SubhxTextMuted,
                    indicatorColor = SubhxPrimary.copy(alpha = 0.25f)
                ),
                modifier = Modifier.testTag("nav_item_${destination.name.lowercase()}")
            )
        }
    }
}

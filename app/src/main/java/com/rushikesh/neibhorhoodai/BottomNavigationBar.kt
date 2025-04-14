package com.rushikesh.neibhorhoodai

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// Define a dynamic list for bottom navigation items
@Composable
fun BottomNavigationBar(navController: NavController) {
    // Dynamic list of items, which can be fetched from a database or passed as arguments at runtime
    val navItems = listOf(
        BottomNavItem("Home", "questions_screen/test/20", Icons.Default.Home),
        BottomNavItem("Properties", "properties_list", Icons.Default.Search),
        BottomNavItem("Settings", "settings_screen", Icons.Default.Settings)
    )

    // Getting the current route from the NavController's back stack
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Bottom Navigation Bar
    NavigationBar(containerColor = Color(0xFF0288D1)) {
        navItems.forEach { item ->
            // Determine if the current item is selected based on route matching
            val selected = currentRoute?.startsWith(item.route.substringBefore('/')) == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) Color(0xFF0288D1) else Color.White
                    )
                },
                label = {
                    Text(item.label, color = if (selected) Color(0xFF0288D1) else Color.White)
                },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            // Pop up to the start destination to avoid stack overflow
                            popUpTo("user_details") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF0288D1),
                    selectedTextColor = Color(0xFF0288D1),
                    indicatorColor = Color(0xFFE1F5FE)
                )
            )
        }
    }
}

// Data class for each navigation item
data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

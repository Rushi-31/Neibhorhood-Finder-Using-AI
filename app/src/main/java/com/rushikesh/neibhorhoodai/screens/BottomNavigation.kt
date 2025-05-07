package com.rushikesh.neibhorhoodai

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var userName by remember { mutableStateOf("User") }
    var userAge by remember { mutableStateOf("0") }
    var userRole by remember { mutableStateOf<String?>(null) }

    // Fetch user data
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        uid?.let {
            try {
                val userRef = Firebase.database.getReference("users").child(uid)
                val dataSnapshot = withContext(Dispatchers.IO) { userRef.get().await() }

                userName = dataSnapshot.child("name").getValue(String::class.java) ?: "User"
                userAge = dataSnapshot.child("age").getValue(String::class.java) ?: "0"
                userRole = dataSnapshot.child("role").getValue(String::class.java)
            } catch (e: Exception) {
                Log.e("BottomNav", "Failed to fetch user data: ${e.message}")
                userRole = "Relocation Help Seeker" // fallback
            }
        } ?: run {
            userRole = "Relocation Help Seeker"
        }
    }

    // Wait for role to load before rendering
    if (userRole == null) return

    val homeRoute by remember(userRole, userName, userAge) {
        mutableStateOf(
            if (userRole == "Property Owner") {
                "property_owner_dashboard"
            } else {
                "questions_screen/${userName}/${userAge}"
            }
        )
    }

    val navItems = listOf(
        BottomNavItem("Home", homeRoute, Icons.Filled.Home),
        BottomNavItem("Properties", "properties_list", Icons.Filled.Search),
        BottomNavItem("Profile", "profile_screen", Icons.Filled.Person),
        BottomNavItem("Settings", "settings_screen", Icons.Filled.Settings),
//        BottomNavItem("About", "about_screen", Icons.Filled.Info)
    )


    NavigationBar(containerColor = Color(0xFF0288D1)) {
        navItems.forEach { item ->
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
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
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

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

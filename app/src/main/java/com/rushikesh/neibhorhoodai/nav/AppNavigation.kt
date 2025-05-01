package com.rushikesh.neibhorhoodai.nav

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rushikesh.neibhorhoodai.BottomNavigationBar
import com.rushikesh.neibhorhoodai.screens.AboutScreen
import com.rushikesh.neibhorhoodai.screens.AddPropertyScreen
import com.rushikesh.neibhorhoodai.screens.AuthScreen
import com.rushikesh.neibhorhoodai.screens.ManagePropertiesScreen
import com.rushikesh.neibhorhoodai.screens.ProfileScreen
import com.rushikesh.neibhorhoodai.screens.PropertiesListScreen
import com.rushikesh.neibhorhoodai.screens.PropertyOwnerDashboard
import com.rushikesh.neibhorhoodai.screens.QuestionsScreen
import com.rushikesh.neibhorhoodai.screens.SettingsScreen
import com.rushikesh.neibhorhoodai.viewmodel.ResultScreenJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

// Routes used for navigation
object NavRoutes {
    const val AUTH = "auth"
    const val QUESTIONS = "questions_screen/{name}/{role}"
    const val OWNER_DASHBOARD = "property_owner_dashboard"
    const val PROPERTIES_LIST = "properties_list"
    const val PROFILE = "profile_screen"
    const val SETTINGS = "settings_screen"
    const val RESULT_SCREEN = "result_screen/{result}"
    const val ABOUT = "about_screen"
    const val ADD_PROPERTY = "add_property"
    const val MANAGE_PROPERTIES = "manage_properties"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        startDestination = if (currentUser != null) {
            try {
                val uid = currentUser.uid
                val userSnapshot = withContext(Dispatchers.IO) {
                    Firebase.database.getReference("users").child(uid).get().await()
                }
                val role = userSnapshot.child("role").getValue(String::class.java) ?: "Relocation Help Seeker"
                val name = userSnapshot.child("name").getValue(String::class.java) ?: "User"

                // Passing only name and role
                if (role == "Property Owner") NavRoutes.OWNER_DASHBOARD
                else "questions_screen/$name/$role"
            } catch (e: Exception) {
                Log.e("AppNavigation", "Error fetching user data: ${e.message}")
                NavRoutes.AUTH
            }
        } else {
            NavRoutes.AUTH
        }
    }

    startDestination?.let { destination ->
        // Show bottom bar only on these screens
        val bottomBarRoutes = listOf(
            NavRoutes.QUESTIONS.split("/")[0],
            NavRoutes.OWNER_DASHBOARD,
            NavRoutes.PROPERTIES_LIST,
            NavRoutes.PROFILE,
            NavRoutes.SETTINGS,
            NavRoutes.RESULT_SCREEN.split("/")[0],
        )
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
        val showBottomBar = bottomBarRoutes.any { currentDestination?.startsWith(it) == true }

        Scaffold(
            bottomBar = {
                if (showBottomBar) BottomNavigationBar(navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = destination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(NavRoutes.AUTH) {
                    AuthScreen(navController)
                }
                composable(
                    route = NavRoutes.QUESTIONS,
                    arguments = listOf(
                        navArgument("name") { type = NavType.StringType },
                        navArgument("role") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val role = backStackEntry.arguments?.getString("role") ?: ""
                    // Pass only name and role to QuestionsScreen
                    QuestionsScreen(name = name, navController = navController)
                }
                composable(NavRoutes.OWNER_DASHBOARD) {
                    PropertyOwnerDashboard(navController)
                }
                composable(NavRoutes.PROPERTIES_LIST) {
                    PropertiesListScreen(navController)
                }
                composable(NavRoutes.PROFILE) {
                    ProfileScreen(navController)
                }

                composable(NavRoutes.ADD_PROPERTY) {
                    AddPropertyScreen(navController)
                }
                composable(NavRoutes.MANAGE_PROPERTIES) {
                    ManagePropertiesScreen()
                }
                composable(NavRoutes.SETTINGS) {
                    SettingsScreen(navController)
                }
                composable(NavRoutes.ABOUT) {
                    AboutScreen() // Navigate to About Screen
                }
                composable(
                    route = NavRoutes.RESULT_SCREEN,
                    arguments = listOf(navArgument("result") { type = NavType.StringType })
                ) { backStackEntry ->
                    val result = backStackEntry.arguments?.getString("result") ?: ""
                    val decoded = URLDecoder.decode(result, StandardCharsets.UTF_8.toString())
                    ResultScreenJson(result = decoded)
                }
            }
        }
    }
}

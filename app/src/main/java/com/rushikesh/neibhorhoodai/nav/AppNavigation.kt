package com.rushikesh.neibhorhoodai.nav

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rushikesh.neibhorhoodai.screen.SettingsScreen
import com.rushikesh.neibhorhoodai.screens.PropertiesList
import com.rushikesh.neibhorhoodai.screens.QuestionsScreen
import com.rushikesh.neibhorhoodai.screens.UserDetailsScreen
import com.rushikesh.neibhorhoodai.viewmodel.ResultScreenJson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "user_details") {
        composable("user_details") { UserDetailsScreen(navController) }

        composable(
            "questions_screen/{name}/{age}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("age") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val age = backStackEntry.arguments?.getString("age") ?: ""
            QuestionsScreen(name, navController)
        }

        composable(
            "result_screen/{result}",
            arguments = listOf(navArgument("result") { type = NavType.StringType })
        ) { backStackEntry ->
            val result = backStackEntry.arguments?.getString("result") ?: ""
            val decodedResult = URLDecoder.decode(result, StandardCharsets.UTF_8.toString())
            ResultScreenJson(decodedResult)
        }

        composable("properties_list") {
            PropertiesList(navController)
        }

        composable("settings_screen") {
            SettingsScreen(navController)
        }
    }
}

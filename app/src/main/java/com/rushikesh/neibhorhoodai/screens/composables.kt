package com.rushikesh.neibhorhoodai.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.rushikesh.neibhorhoodai.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun AboutScreen() {
    val context = LocalContext.current
    val footerText = stringResource(id = R.string.footer_text)
    val linkedInUrl = stringResource(id = R.string.linkedin_url)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About", color = Color.White) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(Color(0xFF1976D2))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Neighbourhood Finder AI",
                fontSize = 26.sp,
                color = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "This project helps you find the right neighborhood for relocation using AI and geospatial analytics, based on your lifestyle, interests, and economic factors.",
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Introduction & Objective:",
                fontSize = 20.sp,
                color = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = """ 
                    ✅ Offer data-driven, tailored relocation insights.
                    ✅ Cluster neighborhoods using AI for better decision-making.
                    ✅ Enhance urban planning with scalable, smart recommendations.
                """.trimIndent(),
                fontSize = 15.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Research Methodology:",
                fontSize = 20.sp,
                color = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = """ 
                    📊 Data Collection: Housing prices, demographics, amenities, transport, crime rates.
                    🧠 NLP & Clustering: Understanding user preferences and categorizing localities.
                    🗺 Geospatial Analytics: Mapping real-time location data for accurate recommendations.
                    🎯 AI-Powered Refinement: Machine learning improves suggestions based on user feedback.
                """.trimIndent(),
                fontSize = 15.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Scalability & Innovation:",
                fontSize = 20.sp,
                color = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = """ 
                    🚀 Expansion Potential: Works across Indian cities & globally.
                    🔄 Adaptive System: Updates dynamically with market & socio-economic trends.
                    📡 Future-Ready: Cloud-based infrastructure ensures efficient scaling.
                """.trimIndent(),
                fontSize = 15.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Technological Requirements:",
                fontSize = 20.sp,
                color = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = """ 
                    ⚙ Cloud Computing & Storage: Scalable AWS/GCP/Azure infrastructure.
                    📡 APIs & Data Sources: Integrating real estate, census, and open-data APIs.
                    🤖 AI & ML Models: Advanced clustering & recommendation algorithms.
                """.trimIndent(),
                fontSize = 15.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = footerText,
                textDecoration = TextDecoration.Underline,
                color = Color.Gray,
                fontSize = 9.sp,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl))
                    context.startActivity(intent)
                }
            )
        }
    }
}
// --- PROFILE SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Check if the user is logged in
    val name = currentUser?.displayName ?: "User"
    val email = currentUser?.email ?: "No email"

    // Fallback if currentUser is null (user is not logged in)
    if (currentUser == null) {
        // Handle the case where the user is not logged in (optional)
        Text("No user logged in")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = Color.White) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(Color(0xFF1976D2))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Display the profile picture (initial or avatar)
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                color = Color(0xFF1976D2)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = name.firstOrNull()?.uppercase() ?: "U",
                        fontSize = 48.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Display user's name and email
            Text(name, fontSize = 24.sp, color = Color(0xFF333333), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(email, fontSize = 14.sp, color = Color(0xFF666666))

            Spacer(modifier = Modifier.height(40.dp))

            // App Version Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("App Version", fontSize = 18.sp, color = Color(0xFF1976D2))
                    Text("v1.0.0", fontSize = 14.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Logout Button
            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

// 🔍 SEARCH PROPERTY SCREEN
@Composable
fun SearchPropertyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Search Properties", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        var query by remember { mutableStateOf("") }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Enter location or keyword") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle search */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF87CEEB))
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Search")
        }
    }
}


//setting screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = Color.White) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(Color(0xFF1976D2))
            )
        },
        containerColor = Color(0xFFF7F9FC)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SettingsItem(
                title = "Notifications",
                subtitle = "Manage notification settings",
                icon = Icons.Default.Notifications,
                onClick = { /* navigate or open notification settings */ }
            )

            SettingsItem(
                title = "Privacy Policy",
                subtitle = "Read our privacy policy",
                icon = Icons.Default.Warning,
                onClick = { /* open privacy screen or link */ }
            )

            SettingsItem(
                title = "Terms of Service",
                subtitle = "View the terms and conditions",
                icon = Icons.Default.Lock,
                onClick = { /* open terms screen or link */ }
            )

            SettingsItem(
                title = "Help & Support",
                subtitle = "Get assistance",
                icon = Icons.Default.Call,
                onClick = { /* open help screen */ }
            )

            SettingsItem(
                title = "About",

                subtitle = "Learn more about the app",
                icon = Icons.Default.Info,
                onClick = { navController.navigate("about_screen") }
            )
            SettingsItem(title = "Logout", subtitle ="Logout from your account" , icon =Icons.Default.ExitToApp , onClick = {
                val auth = FirebaseAuth.getInstance()
                auth.signOut()

                navController.navigate("auth") {
                    popUpTo(0) { inclusive = true } // clear all backstack
                    launchSingleTop = true
                }
            })

        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontSize = 18.sp, color = Color.Black)
                Text(text = subtitle, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}


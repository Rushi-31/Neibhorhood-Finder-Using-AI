package com.rushikesh.neibhorhoodai.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rushikesh.neibhorhoodai.R
import com.rushikesh.neibhorhoodai.models.Neighborhood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeighborhoodList(
    neighborhoods: List<Neighborhood>,
    onSelect: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Explore Neighborhoods", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF6200EE)) // Vibrant Purple
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        if (neighborhoods.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No neighborhoods found. Try again.", fontSize = 16.sp, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(brush = Brush.verticalGradient(listOf(Color(0xFFBB86FC), Color(0xFF03DAC5)))) // Gradient background
                ,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(neighborhoods) { neighborhood ->
                    NeighborhoodCard(neighborhood, onSelect)
                }
            }
        }
    }
}

@Composable
fun NeighborhoodCard(
    neighborhood: Neighborhood,
    onSelect: (String) -> Unit
) {
    // Colorful Cards with Vibrant Backgrounds
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(neighborhood.Neighborhood) }
            .padding(horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = MaterialTheme.shapes.large, // Rounded corners
        colors = CardDefaults.cardColors(containerColor = randomColor())
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Image for the neighborhood (replace with actual dynamic images)
            Image(
                painter = painterResource(id = R.drawable.logo), // Placeholder image
                contentDescription = "Neighborhood Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(bottom = 12.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .border(2.dp, Color.White, MaterialTheme.shapes.medium) // White border for image
            )

            // Neighborhood Name with vibrant color
            Text(
                text = neighborhood.Neighborhood,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dynamic details with colors
            Text("Crime Rate: ${neighborhood.CrimeRate}", fontSize = 14.sp, color = Color.White)
            Text("Schools Nearby: ${neighborhood.SchoolsNearby}", fontSize = 14.sp, color = Color.White)
            Text("Hospitals Nearby: ${neighborhood.HospitalsNearby}", fontSize = 14.sp, color = Color.White)

            // Match Score with a colorful font
            Text(
                "Match Score: ${(neighborhood.SimilarityScore * 100).toInt()}%",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Yellow
            )
        }
    }
}

fun randomColor(): Color {
    // Function to generate random colors for cards
    return when ((1..5).random()) {
        1 -> Color(0xFFFF5722) // Orange
        2 -> Color(0xFF4CAF50) // Green
        3 -> Color(0xFF2196F3) // Blue
        4 -> Color(0xFFFFEB3B) // Yellow
        5 -> Color(0xFFF44336) // Red
        else -> Color(0xFF6200EE) // Default Purple
    }
}

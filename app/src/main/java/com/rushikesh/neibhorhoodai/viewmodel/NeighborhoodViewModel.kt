package com.rushikesh.neibhorhoodai.viewmodel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rushikesh.neibhorhoodai.R
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreenJson(result: String) {
    val decodedResult = URLDecoder.decode(result, StandardCharsets.UTF_8.toString())
    val items = decodedResult
        .split("||")
        .map { it.split("##") }
        .filter { it.size >= 6 }
        .distinctBy { it[0] }

    val imageResIds = listOf(
        R.drawable.r1,
        R.drawable.r2,
        R.drawable.r3,
        R.drawable.r4
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🌤️ Recommended Neighborhoods",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF29B6F6)
                )
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC))
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(items) { index, item ->
                val imageResId = imageResIds[index % imageResIds.size]
                ResultCard(
                    neighborhood = item[0],
                    professions = item[1],
                    schools = item[2],
                    hospitals = item[3],
                    crimeRate = item[4],
                    score = item[5],
                    drawableId = imageResId
                )
            }
        }
    }
}

@Composable
fun ResultCard(
    neighborhood: String,
    professions: String,
    schools: String,
    hospitals: String,
    crimeRate: String,
    score: String,
    drawableId: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = "Neighborhood Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFE3F2FD), Color(0xFFB3E5FC))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "🏡 $neighborhood",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0288D1)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("👥 Professions: $professions", fontSize = 14.sp)
                Text("🏫 Schools Nearby: $schools", fontSize = 14.sp)
                Text("🏥 Hospitals Nearby: $hospitals", fontSize = 14.sp)
                Text("🚨 Crime Rate: $crimeRate", fontSize = 14.sp)
                Text(
                    "🎯 Match Score: $score",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF00796B)
                )
            }
        }
    }
}

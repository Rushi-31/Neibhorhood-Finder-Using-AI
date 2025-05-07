
package com.rushikesh.neibhorhoodai.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rushikesh.neibhorhoodai.data.professionList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsScreen(name: String, navController: NavController) {
    val questions = listOf(
        "What is your profession?",
        "Select your monthly income?",
        "How much rent are you interested in paying?",
        "How would you describe your community sentiment?",
        "How accessible is public transport for you?",
        "Do you prefer a quiet or lively neighborhood?",
        "How important are green spaces to you?",
        "Do you own a vehicle?",
        "Do you have children, or are you planning for children?"
    )

    val optionsList = listOf(
        professionList,
        listOf("0-10000", "10000-30000", "30000-50000", "50000-100000", "100000+"),
        listOf("0-10000", "10000-50000", "30000-50000", "50000-100000", "100000+"),
        listOf("Low", "Moderate", "High", "Very High"),
        listOf("Low", "Moderate", "High", "Very High"),
        listOf("Quiet", "Lively"),
        listOf("Not Important", "Somewhat Important", "Very Important"),
        listOf("Yes", "No"),
        listOf("Yes", "No")
    )

    val requiredCount = 5
    val answers = remember { mutableStateListOf<String?>().apply { repeat(questions.size) { add(null) } } }
    var currentIndex by remember { mutableStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val progress = (currentIndex + 1).toFloat() / questions.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Neighborhood AI", color = Color.White, fontWeight = FontWeight.Bold) },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { navController.navigate("settings_screen") },
                        tint = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0288D1))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 22.dp, vertical = 30.dp)
        ) {
            Text("Hello $name 👋", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0288D1))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Answer a few questions about yourself and know your neighborhood better!", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            Crossfade(targetState = currentIndex, label = "") { index ->
                if (index < questions.size) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE1F5FE), RoundedCornerShape(16.dp))
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(questions[index], fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0288D1))
                        Spacer(modifier = Modifier.height(16.dp))

                        if (index == 0) {
                            var searchText by remember { mutableStateOf("") }
                            val filteredOptions = remember(searchText) {
                                professionList.filter { it.contains(searchText, ignoreCase = true) }.take(6)
                            }

                            OutlinedTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                label = { Text("Search Profession") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            if (searchText.isNotBlank()) {
                                LazyColumn(modifier = Modifier.heightIn(max = 180.dp)) {
                                items(filteredOptions) { profession ->
                                    Text(
                                        text = profession,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                answers[index] = profession
                                                currentIndex++
                                                error = null
                                            }
                                            .padding(12.dp),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }} else {
                            var selectedOption by remember { mutableStateOf(answers[index] ?: "") }
                            DropdownQuestion(
                                selected = selectedOption,
                                options = optionsList[index],
                                onSelected = {
                                    selectedOption = it
                                    answers[index] = it
                                }
                            )
                        }

                        AnimatedVisibility(visible = error != null) {
                            Text(error ?: "", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 10.dp))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (currentIndex > 0) {
                                Button(
                                    onClick = {
                                        currentIndex--
                                        error = null
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1), contentColor = Color.White)
                                ) {
                                    Text("Previous")
                                }
                            }

                            Button(
                                onClick = {
                                    if (currentIndex < requiredCount && answers[currentIndex].isNullOrBlank()) {
                                        error = "This field is required"
                                        return@Button
                                    }

                                    error = null

                                    if (currentIndex == questions.lastIndex) {
                                        isLoading = true
                                        coroutineScope.launch {
                                            submitAnswers(
                                                answers, navController,
                                                onError = { error = it },
                                                onDone = { isLoading = false }
                                            )
                                        }
                                    } else {
                                        currentIndex++
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1), contentColor = Color.White)
                            ) {
                                Text(if (currentIndex == questions.lastIndex) "Submit" else "Next")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())

            if (isLoading) {
                Spacer(modifier = Modifier.height(20.dp))
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownQuestion(selected: String, options: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text("Select") },
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

suspend fun submitAnswers(
    answers: List<String?>,
    navController: NavController,
    onError: (String) -> Unit,
    onDone: () -> Unit
) {
    try {
        val incomeMax = answers[1]?.split("-")?.lastOrNull()?.replace("+", "")?.toFloatOrNull() ?: 0f
        val body = mapOf(
            "Professions" to answers[0],
            "Income_Estimate" to incomeMax,
            "Rent_Range" to answers[2],
            "Community Sentiment" to answers[3],
            "Public Transport" to answers[4]
        )

        val gson = Gson()
        val json = gson.toJson(body)
        val client = OkHttpClient()
        val requestBody = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://10.0.2.2:5000/recommend")
            .post(requestBody)
            .build()

        val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
        if (!response.isSuccessful) throw Exception("Network error: ${response.code}")
        val responseBody = response.body?.string() ?: throw Exception("Empty response")

        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        val resultList: List<Map<String, Any>> = gson.fromJson(responseBody, type)

        val result = resultList.joinToString("||") {
            listOf(
                it["Neighborhood"].toString(),
                it["Professions"].toString(),
                it["Schools Nearby"].toString(),
                it["Hospitals Nearby"].toString(),
                it["Crime Rate"].toString(),
                it["Similarity Score"].toString()
            ).joinToString("##")
        }

        val encoded = URLEncoder.encode(result, StandardCharsets.UTF_8.toString())
        withContext(Dispatchers.Main) {
            navController.navigate("result_screen/$encoded")
        }
    } catch (e: Exception) {
        Log.e("API", "Error: ${e.localizedMessage}")
        withContext(Dispatchers.Main) { onError("Failed to connect. Please try again.") }
    } finally {
        onDone()
    }
}

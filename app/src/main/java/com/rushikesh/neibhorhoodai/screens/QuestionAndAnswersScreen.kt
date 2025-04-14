package com.rushikesh.neibhorhoodai.screens


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.TextFieldDefaults
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
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rushikesh.neibhorhoodai.BottomNavigationBar
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
        "What is your monthly income?",
        "How would you describe your community sentiment?",
        "How accessible is public transport for you?",
        "Do you prefer a quiet or lively neighborhood?",
        "How important are green spaces to you?",
        "Do you own a vehicle?",
        "Do you have children, or are you planning for children?"
    )


    val professionOptions = professionList
    val sentimentOptions = listOf("Low", "Moderate", "High", "Very High")
    val transportOptions = listOf("Low", "Moderate", "High", "Very High")
    val binaryOptions = listOf("Yes", "No")
    val importanceOptions = listOf("Not Important", "Somewhat Important", "Very Important")
    val lifestyleOptions = listOf("Quiet", "Lively")
    val incomeOptions = listOf("0-10000", "10000-50000", "50000-100000", "100000+")

    val answers = remember { mutableStateListOf<String>() }
    var currentIndex by remember { mutableStateOf(0) }
    var input by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val progress = (currentIndex + 1).toFloat() / questions.size

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Neighborhood AI", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                navigationIcon = {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.padding(start = 16.dp), tint = Color.White)
                },
                actions = {
                    Icon(Icons.Default.MoreVert, contentDescription = "More Options", modifier = Modifier.padding(end = 16.dp), tint = Color.White)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0288D1))
            )
        },
        bottomBar = {BottomNavigationBar(navController= rememberNavController())}

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // QnA Visual (optional)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Hello $name 👋",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0288D1)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Answer a few questions about yourself and know your neighborhood better!",
                fontSize = 18.sp, fontWeight = FontWeight.Normal, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            Crossfade(targetState = currentIndex, label = "") { index ->
                if (index < questions.size) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE1F5FE), RoundedCornerShape(16.dp))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = questions[index],
                            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            color = Color(0xFF0288D1)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        when (index) {
                            0 -> {
                                // Profession input
                                OutlinedTextField(
                                    value = input,
                                    onValueChange = {
                                        input = it
                                        error = null
                                    },
                                    label = { Text("Search profession") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Color(0xFF0288D1),
                                        unfocusedBorderColor = Color.Gray,
                                        focusedLabelColor = Color(0xFF0288D1)
                                    )
                                )

                                val filteredProfessions = professionOptions.filter {
                                    it.contains(input, ignoreCase = true)
                                }.take(6)

                                if (input.isNotEmpty()) {
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(160.dp)
                                            .background(Color.Transparent, RoundedCornerShape(8.dp))
                                    ) {
                                        items(filteredProfessions) { profession ->
                                            Text(
                                                text = profession,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        input = profession
                                                        answers.add(profession)
                                                        input = ""
                                                        currentIndex++
                                                    }
                                                    .padding(12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            1 -> {
                                // Income input
                                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                                    OutlinedTextField(
                                        readOnly = true,
                                        value = input,
                                        onValueChange = {},
                                        label = { Text("Select Income Range") },
                                        trailingIcon = {
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                        },
                                        modifier = Modifier
                                            .menuAnchor()
                                            .fillMaxWidth(),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF0288D1),
                                            unfocusedBorderColor = Color.Gray
                                        )
                                    )
                                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                        incomeOptions.forEach { option ->
                                            DropdownMenuItem(
                                                text = { Text(option) },
                                                onClick = {
                                                    input = option
                                                    expanded = false
                                                    error = null
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            else -> {
                                // Other inputs (Sentiment, Transport, Lifestyle, etc.)
                                val options = when (index) {
                                    2 -> sentimentOptions
                                    3 -> transportOptions
                                    4 -> lifestyleOptions
                                    5 -> importanceOptions
                                    6, 7 -> binaryOptions
                                    else -> emptyList()
                                }

                                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                                    OutlinedTextField(
                                        readOnly = true,
                                        value = input,
                                        onValueChange = {},
                                        label = { Text("Select") },
                                        trailingIcon = {
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                        },
                                        modifier = Modifier
                                            .menuAnchor()
                                            .fillMaxWidth(),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF0288D1),
                                            unfocusedBorderColor = Color.Gray
                                        )
                                    )
                                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                        options.forEach { option ->
                                            DropdownMenuItem(
                                                text = { Text(option) },
                                                onClick = {
                                                    input = option
                                                    expanded = false
                                                    error = null
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Error message visibility
                        AnimatedVisibility(visible = error != null, enter = fadeIn(), exit = fadeOut()) {
                            Text(error ?: "", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Submit or Next button
                        Button(
                            onClick = {
                                if (input.isBlank()) {
                                    error = "Please enter a response"
                                    return@Button
                                }

                                answers.add(input)
                                input = ""
                                error = null

                                if (index == questions.size - 1) {
                                    isLoading = true
                                    coroutineScope.launch {
                                        try {
                                            val incomeMax = answers[1].split("-").lastOrNull()?.replace("+", "")?.toFloatOrNull() ?: 0f

                                            val body = mapOf(
                                                "Professions" to answers[0],
                                                "Income_Estimate" to incomeMax,
                                                "Community Sentiment" to answers[2],
                                                "Public Transport" to answers[3]
                                            )

                                            val gson = Gson()
                                            val json = gson.toJson(body)
                                            val client = OkHttpClient()
                                            val requestBody = json.toRequestBody("application/json".toMediaType())
                                            val request = Request.Builder()
                                                .url("http://10.0.2.2:5000/recommend")
                                                .post(requestBody)
                                                .build()

                                            val response = withContext(Dispatchers.IO) {
                                                client.newCall(request).execute()
                                            }

                                            if (!response.isSuccessful) {
                                                throw Exception("Network error: ${response.code}")
                                            }

                                            val responseBody = response.body?.string() ?: throw Exception("Empty response")
                                            val type = object : TypeToken<List<Map<String, Any>>>() {}.type
                                            val responseList: List<Map<String, Any>> = gson.fromJson(responseBody, type)

                                            val resultJson = responseList.joinToString("||") {
                                                listOf(
                                                    it["Neighborhood"].toString(),
                                                    it["Professions"].toString(),
                                                    it["Schools Nearby"].toString(),
                                                    it["Hospitals Nearby"].toString(),
                                                    it["Crime Rate"].toString(),
                                                    it["Similarity Score"].toString()
                                                ).joinToString("##")
                                            }

                                            val encodedResult = URLEncoder.encode(resultJson, StandardCharsets.UTF_8.toString())

                                            // Ensure navigation is done on the main thread
                                            withContext(Dispatchers.Main) {
                                                navController.navigate("result_screen/$encodedResult")
                                            }
                                        } catch (e: Exception) {
                                            Log.e("API", "Error: ${e.localizedMessage}")
                                            error = "Failed to connect. Please try again."
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                } else {
                                    currentIndex++
                                }
                            },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                        ) {
                            Text(if (index == questions.size - 1) "Submit" else "Next", color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0288D1)
            )
//            Image(painter = painterResource(id = R.drawable.qna), contentDescription = "")

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}

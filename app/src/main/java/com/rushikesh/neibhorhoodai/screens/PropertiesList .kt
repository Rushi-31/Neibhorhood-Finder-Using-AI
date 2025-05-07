//package com.rushikesh.neibhorhoodai.screens
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.google.firebase.database.FirebaseDatabase
//import com.rushikesh.neibhorhoodai.R
//
//data class Property(
//    val id: String = "",
//    val name: String = "",
//    val address: String = "",
//    val rent: String = "",
//    val type: String = "",
//    val availability: Boolean = false
//
//)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PropertiesListScreen(navController: NavController) {
//    val dbRef = FirebaseDatabase.getInstance().getReference("properties")
//    var allProperties by remember { mutableStateOf(listOf<Property>()) }
//    var selectedProperty by remember { mutableStateOf<Property?>(null) }
//    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
//
//    // Fetch from Firebase once
//    LaunchedEffect(Unit) {
//        dbRef.get().addOnSuccessListener { snapshot ->
//            val list = mutableListOf<Property>()
//            snapshot.children.forEach { group ->
//                group.children.forEach { prop ->
//                    prop.getValue(Property::class.java)?.let { list.add(it) }
//                }
//            }
//            allProperties = list
//        }
//    }
//
//    if (selectedProperty == null) {
//        // Main List View
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Properties", color = Color.White) },
//                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF0288D1))
//                )
//            }
//        ) { padding ->
//            Column(
//                modifier = Modifier
//                    .padding(padding)
//                    .padding(16.dp)
//                    .fillMaxSize()
//                    .background(Color(0xFFE1F5FE))
//            ) {
//                OutlinedTextField(
//                    value = searchQuery,
//                    onValueChange = { searchQuery = it },
//                    label = { Text("Search by name or address") },
//                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp)
//                )
//
//                val filtered = allProperties.filter {
//                    it.name.contains(searchQuery.text, ignoreCase = true) ||
//                            it.address.contains(searchQuery.text, ignoreCase = true)
//                }
//
//                if (filtered.isEmpty()) {
//                    Text("No properties found", color = Color.Gray)
//                } else {
//                    LazyColumn {
//                        items(filtered) { property ->
//                            PropertyCard1(property = property) {
//                                selectedProperty = property
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    } else {
//        // Detailed View
//        PropertyDetailScreen(property = selectedProperty!!) {
//            selectedProperty = null
//        }
//    }
//}
//
//@Composable
//fun PropertyCard1(property: Property, onClick: () -> Unit) {
//    Card(
//        shape = RoundedCornerShape(12.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//            .clickable { onClick() },
//        elevation = CardDefaults.cardElevation(6.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(property.name, style = MaterialTheme.typography.titleMedium, color = Color(0xFF01579B))
//            Text(property.address, style = MaterialTheme.typography.bodyMedium)
//            Text("Rent: ₹${property.rent} • Type: ${property.type}", style = MaterialTheme.typography.bodySmall)
//            Text(
//                if (property.availability) "Available" else "Not Available",
//                color = if (property.availability) Color.Green else Color.Red,
//                style = MaterialTheme.typography.labelMedium
//            )
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PropertyDetailScreen(property: Property, onBack: () -> Unit) {
//    val imageList = listOf(
//        R.drawable.r1, R.drawable.r2, R.drawable.r3,
//        R.drawable.r4, R.drawable.r2, R.drawable.r1, R.drawable.r2
//    )
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(property.name, color = Color.White) },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
//                    }
//                },
//                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF0288D1))
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp)
//                .fillMaxSize()
//                .background(Color(0xFFE1F5FE))
//        ) {
//            LazyRow(modifier = Modifier.fillMaxWidth()) {
//                items(imageList) { imageRes ->
//                    Image(
//                        painter = painterResource(id = imageRes),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .size(200.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(property.name, style = MaterialTheme.typography.titleLarge, color = Color(0xFF01579B))
//            Text(property.address, style = MaterialTheme.typography.bodyMedium)
//            Spacer(modifier = Modifier.height(8.dp))
//            Text("Rent: ₹${property.rent}", style = MaterialTheme.typography.bodyLarge)
//            Text("Type: ${property.type}", style = MaterialTheme.typography.bodyLarge)
//            Text(
//                if (property.availability) "Currently Available" else "Currently Unavailable",
//                color = if (property.availability) Color(0xFF2E7D32) else Color.Red,
//                style = MaterialTheme.typography.bodyLarge
//            )
//        }
//    }
//}

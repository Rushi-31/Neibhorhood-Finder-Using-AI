package com.rushikesh.neibhorhoodai.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase
import com.rushikesh.neibhorhoodai.ui.theme.darkBlue
import kotlinx.coroutines.launch

// Light sky blue theme color
//val SkyBlue = Color(0xFF87CEEB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePropertiesScreen() {
    var properties by remember { mutableStateOf<List<Property>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        FirebaseDatabase.getInstance().getReference("properties").get().addOnSuccessListener {
            properties = it.children.mapNotNull { snapshot ->
                snapshot.getValue(Property::class.java)
            }
            isLoading = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Manage Properties", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBlue)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator(color = darkBlue)
                }
                properties.isEmpty() -> {
                    Text("No properties available.", color = Color.Gray)
                }
                else -> {
                    properties.forEach { property ->
                        PropertyCard(property) {
                            FirebaseDatabase.getInstance()
                                .getReference("properties")
                                .child(property.id)
                                .removeValue()

                            scope.launch {
                                snackbarHostState.showSnackbar("Property deleted")
                            }

                            properties = properties.filterNot { it.id == property.id }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyCard(property: Property, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)) // very light blue background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Name: ${property.name}", style = MaterialTheme.typography.titleMedium, color = Color.Black)
            Text("Address: ${property.address}", color = Color.DarkGray)
            Text("Rent: ₹${property.rent}", color = Color.DarkGray)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = darkBlue)
            ) {
                Text("Delete", color = Color.White)
            }
        }
    }
}

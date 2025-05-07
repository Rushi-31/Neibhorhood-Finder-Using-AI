package com.rushikesh.neibhorhoodai.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rushikesh.neibhorhoodai.R
import com.rushikesh.neibhorhoodai.ui.theme.darkBlue

// Theme colors
val LightSkyBlueBackground = Color(0xFFF0FAFF)

// Data Class
data class Property(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val rent: String = "",
    val type: String = "",
    val availability: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPropertyScreen(navController: NavController) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    var propertyName by remember { mutableStateOf(TextFieldValue("")) }
    var propertyAddress by remember { mutableStateOf(TextFieldValue("")) }
    var propertyRent by remember { mutableStateOf(TextFieldValue("")) }
    var propertyType by remember { mutableStateOf("Select Property Type") }
    var availability by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val propertyTypes = listOf("Apartment", "House", "Villa", "Commercial", "Studio")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Property", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBlue)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
                .background(LightSkyBlueBackground)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = propertyName,
                onValueChange = { propertyName = it },
                label = { Text("Property Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = darkBlue,
                    unfocusedBorderColor = Color.Gray
                )
            )

            OutlinedTextField(
                value = propertyAddress,
                onValueChange = { propertyAddress = it },
                label = { Text("Property Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = darkBlue,
                    unfocusedBorderColor = Color.Gray
                )
            )

            OutlinedTextField(
                value = propertyRent,
                onValueChange = { propertyRent = it },
                label = { Text("Rent Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = darkBlue,
                    unfocusedBorderColor = Color.Gray
                )
            )

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    readOnly = true,
                    value = propertyType,
                    onValueChange = {},
                    label = { Text("Property Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = darkBlue,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    propertyTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                propertyType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
                Text("Available", fontSize = 16.sp, color = darkBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = availability, onCheckedChange = { availability = it })
            }

            Button(
                onClick = {
                    val property = Property(
                        id = FirebaseDatabase.getInstance().getReference("properties").push().key ?: "",
                        name = propertyName.text,
                        address = propertyAddress.text,
                        rent = propertyRent.text,
                        type = propertyType,
                        availability = availability
                    )

                    FirebaseDatabase.getInstance()
                        .getReference("properties")
                        .child(property.id)
                        .setValue(property)

                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = darkBlue)
            ) {
                Text("Add Property", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesListScreen(navController: NavController) {
    val dbRef = FirebaseDatabase.getInstance().getReference("properties")
    var allProperties by remember { mutableStateOf(listOf<Property>()) }
    var selectedProperty by remember { mutableStateOf<Property?>(null) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        dbRef.get().addOnSuccessListener { snapshot ->
            val list = mutableListOf<Property>()
            snapshot.children.forEach { prop ->
                prop.getValue(Property::class.java)?.let { list.add(it) }
            }
            allProperties = list
        }
    }

    if (selectedProperty == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Properties", color = Color.White) },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF0288D1))
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .background(Color(0xFFE1F5FE))
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search by name or address") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                val filtered = allProperties.filter {
                    it.name.contains(searchQuery.text, ignoreCase = true) ||
                            it.address.contains(searchQuery.text, ignoreCase = true)
                }

                if (filtered.isEmpty()) {
                    Text("No properties found", color = Color.Gray)
                } else {
                    LazyColumn {
                        items(filtered) { property ->
                            PropertyCard1(property = property) {
                                selectedProperty = property
                            }
                        }
                    }
                }
            }
        }
    } else {
        PropertyDetailScreen(property = selectedProperty!!) {
            selectedProperty = null
        }
    }
}

@Composable
fun PropertyCard1(property: Property, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(property.name, style = MaterialTheme.typography.titleMedium, color = Color(0xFF01579B))
            Text(property.address, style = MaterialTheme.typography.bodyMedium)
            Text("Rent: ₹${property.rent} • Type: ${property.type}", style = MaterialTheme.typography.bodySmall)
            Text(
                if (property.availability) "Available" else "Not Available",
                color = if (property.availability) Color.Green else Color.Red,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyDetailScreen(property: Property, onBack: () -> Unit) {
    val imageList = listOf(
        R.drawable.p1, R.drawable.p2, R.drawable.p3,
        R.drawable.p4, R.drawable.p5, R.drawable.p7
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(property.name, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0288D1))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFE1F5FE))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // Image Gallery
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                items(imageList) { imageRes ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(220.dp)
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Property Info Card
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = property.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF01579B)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = property.address, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Image(painter = painterResource(id = R.drawable.money), contentDescription ="", modifier = Modifier.size(20.dp) )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Rent: ₹${property.rent}", style = MaterialTheme.typography.bodyLarge)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFF5D4037))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Type: ${property.type}", style = MaterialTheme.typography.bodyLarge)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(
                                id = if (property.availability) R.drawable.available else R.drawable.unavailable
                            ),

                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
//                            tint = if (property.availability) Color(0xFF2E7D32) else Color.Red
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (property.availability) "Currently Available" else "Currently Unavailable",
                            color = if (property.availability) Color(0xFF2E7D32) else Color.Red,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

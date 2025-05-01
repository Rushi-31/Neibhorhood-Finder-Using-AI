package com.rushikesh.neibhorhoodai.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rushikesh.neibhorhoodai.ui.theme.darkBlue

// Consistent theme colors

val LightSkyBlueBackground = Color(0xFFF0FAFF)

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

            // Property Name Field
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

            // Address
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

            // Rent
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

            // Property Type Dropdown
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    readOnly = true,
                    value = propertyType,
                    onValueChange = {},
                    label = { Text("Property Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
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

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
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

            // Availability
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
                Text("Available", fontSize = 16.sp, color = darkBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = availability, onCheckedChange = { availability = it })
            }

            // Submit Button
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

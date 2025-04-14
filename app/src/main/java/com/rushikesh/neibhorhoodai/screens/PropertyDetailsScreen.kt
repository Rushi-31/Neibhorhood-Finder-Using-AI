package com.rushikesh.neibhorhoodai.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rushikesh.neibhorhoodai.models.Property

@Composable
fun PropertyDetailScreen(property: Property) {
    Column(Modifier.padding(16.dp)) {
        Text(property.projectname ?: "No Project", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("₹${property.price}", fontSize = 18.sp, color = Color.Green)
        Text("${property.bhk} BHK, ${property.area} sqft")
        Text("Facing: ${property.facing ?: "N/A"}")
        Text("Amenities: ${property.amenities ?: "None"}")
    }
}

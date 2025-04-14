package com.rushikesh.neibhorhoodai.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesList(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Properties List") })
        }
    ) {
        Text("Yet to implement ", modifier = androidx.compose.ui.Modifier.padding(it))
    }
}

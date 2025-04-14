package com.rushikesh.neibhorhoodai

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rushikesh.neibhorhoodai.nav.AppNavigation
import com.rushikesh.neibhorhoodai.ui.theme.NeibhorhoodAITheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeibhorhoodAITheme {
             AppNavigation()
                }
        }
    }
}


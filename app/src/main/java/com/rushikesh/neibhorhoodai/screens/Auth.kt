package com.rushikesh.neibhorhoodai.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rushikesh.neibhorhoodai.R
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AuthScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    val roles = listOf("Relocation Help Seeker", "Property Owner")
    var selectedRole by remember { mutableStateOf(roles[0]) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logi),
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(bottom = 16.dp)
                )
                if (!isLogin) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (!isLogin) {
                    Text("Select Role", fontSize = 14.sp, color = Color.Gray)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.LightGray.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { expanded = true }
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = selectedRole)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            roles.forEach { role ->
                                DropdownMenuItem(
                                    text = { Text(role) },
                                    onClick = {
                                        selectedRole = role
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (isLogin) {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener { authResult ->
                                    val uid = authResult.user?.uid ?: return@addOnSuccessListener
                                    database.child("users").child(uid).get()
                                        .addOnSuccessListener { snapshot ->
                                            val role = snapshot.child("role").value as? String
                                            val fetchedName = snapshot.child("name").value as? String
                                            if (!fetchedName.isNullOrBlank()) {
                                                val encodedName = URLEncoder.encode(fetchedName, StandardCharsets.UTF_8.toString())
                                                if (role == "Relocation Help Seeker") {
                                                    navController.navigate("questions_screen/$encodedName/20")
                                                } else if (role == "Property Owner") {
                                                    navController.navigate("property_owner_dashboard")
                                                } else {
                                                    Toast.makeText(context, "Unknown role", Toast.LENGTH_SHORT).show()
                                                }
                                            } else {
                                                Toast.makeText(context, "Name not found in Realtime Database", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            if (name.isBlank()) {
                                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnSuccessListener { authResult ->
                                    val uid = authResult.user?.uid ?: return@addOnSuccessListener
                                    val user = mapOf(
                                        "email" to email,
                                        "name" to name,
                                        "role" to selectedRole
                                    )
                                    database.child("users").child(uid).setValue(user)
                                        .addOnSuccessListener {
                                            val roleNode = if (selectedRole == "Relocation Help Seeker") "relocation_help_seekers" else "property_owners"
                                            database.child(roleNode).child(uid).setValue(user)
                                            val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
                                            if (selectedRole == "Relocation Help Seeker") {
                                                navController.navigate("questions_screen/$encodedName/20")
                                            } else {
                                                navController.navigate("property_owner_dashboard")
                                            }
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Signup failed: ${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Signup failed: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                ) {
                    Text(text = if (isLogin) "Sign In" else "Sign Up", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { isLogin = !isLogin }) {
                    Text(if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Sign In")
                }
            }
        }
    }
}

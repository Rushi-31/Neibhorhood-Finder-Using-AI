package com.rushikesh.neibhorhoodai.models

data class PropertyInput(
    val locality: String,
    val type: String = "rent" // default to sale
)

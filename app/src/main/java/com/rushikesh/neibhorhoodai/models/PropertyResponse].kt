package com.rushikesh.neibhorhoodai.models

data class PropertyResponse(
    val locality: String,
    val type: String,
    val recommended_properties: List<Property>
)

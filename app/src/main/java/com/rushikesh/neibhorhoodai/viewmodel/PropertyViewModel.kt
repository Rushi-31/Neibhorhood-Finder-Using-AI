package com.rushikesh.neibhorhoodai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rushikesh.neibhorhoodai.api.RetrofitClient
import com.rushikesh.neibhorhoodai.models.Property
import com.rushikesh.neibhorhoodai.models.PropertyInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PropertyViewModel : ViewModel() {

    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    val properties: StateFlow<List<Property>> = _properties

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchProperties(input: PropertyInput) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getProperties(input)
                if (response.isSuccessful && response.body() != null) {
                    _properties.value = response.body()!!.recommended_properties
                } else {
                    _error.value = "Error: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Exception: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }
}

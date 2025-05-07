package com.rushikesh.neibhorhoodai.api
import com.rushikesh.neibhorhoodai.models.Neighborhood
import com.rushikesh.neibhorhoodai.models.UserInput
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("recommend")
    suspend fun getNeighborhoods(
        @Body input: UserInput
    ): Response<List<Neighborhood>>

//    @POST("recommend_properties")
//    suspend fun getProperties(
//        @Body input: PropertyInput
//    ): Response<PropertyResponse>
}

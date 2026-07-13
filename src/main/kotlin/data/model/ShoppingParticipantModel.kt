package com.project.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingParticipantModel(
    @SerialName("shopping_id")
    val shoppingId: Int,
    @SerialName("user_id")
    val userId: String,
)
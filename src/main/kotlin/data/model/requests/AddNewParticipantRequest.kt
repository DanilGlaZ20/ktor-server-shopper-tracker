package com.project.data.model.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddNewParticipantRequest(
    @SerialName("category_id")
    val categoryId: Int,
    @SerialName("participant_phone")
    val participantPhone: String,
)

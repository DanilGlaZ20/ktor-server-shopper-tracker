package com.project.domain.repository

interface ShoppingParticipantRepository {
   suspend fun inviteShoppingParticipant(categoryId: Int, participantPhone: Int): Boolean
   suspend fun removeParticipant(categoryId: Int, participantPhone: Int)
}
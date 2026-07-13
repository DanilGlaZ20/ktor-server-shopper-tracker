package com.project.domain.usecase

import com.project.data.model.CategoryModel
import com.project.data.model.ShoppingParticipantModel
import com.project.domain.repository.ShoppingParticipantRepository
import com.project.utils.IdFinder

class ShoppingParticipantUseCase(
    private val shoppingParticipantRepository: ShoppingParticipantRepository,
    private val idFinder: IdFinder<List<Int>>
) {
    suspend fun addNewParticipant(categoryId: Int, participantPhone: Int): Boolean = shoppingParticipantRepository.inviteShoppingParticipant(categoryId, participantPhone)
    suspend fun removeParticipant(categoryId: Int, participantPhone: Int) = shoppingParticipantRepository.removeParticipant(categoryId, participantPhone)
    suspend fun getId(id: Int) = idFinder.getItemById(id)

}
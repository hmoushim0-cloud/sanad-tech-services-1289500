package sa.factory.sanadtechservices1.domain.repository

import sa.factory.sanadtechservices1.model.SanadTechItem
import sa.factory.sanadtechservices1.model.SanadTechOrder
import sa.factory.sanadtechservices1.model.SanadTechTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SanadTechRepository {
    val catalogFlow: Flow<List<SanadTechItem>>
    val activeCart: StateFlow<List<SanadTechItem>>
    val telemetryFlow: Flow<SanadTechTelemetry>
    
    fun addToCart(item: SanadTechItem)
    fun removeFromCart(item: SanadTechItem)
    fun clearCart()
    suspend fun checkout(paymentMethod: String): SanadTechOrder
    suspend fun refreshCatalog()
}

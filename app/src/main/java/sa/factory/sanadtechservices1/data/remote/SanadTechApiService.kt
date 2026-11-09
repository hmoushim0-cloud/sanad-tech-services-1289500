package sa.factory.sanadtechservices1.data.remote

import sa.factory.sanadtechservices1.model.SanadTechItem
import sa.factory.sanadtechservices1.model.SanadTechOrder

interface SanadTechApiService {
    suspend fun fetchCatalog(): List<SanadTechItem>
    suspend fun submitOrder(order: SanadTechOrder): Boolean
}

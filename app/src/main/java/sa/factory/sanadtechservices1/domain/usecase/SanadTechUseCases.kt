package sa.factory.sanadtechservices1.domain.usecase

import sa.factory.sanadtechservices1.domain.repository.SanadTechRepository
import sa.factory.sanadtechservices1.model.SanadTechItem
import sa.factory.sanadtechservices1.model.SanadTechOrder
import sa.factory.sanadtechservices1.model.SanadTechTelemetry
import kotlinx.coroutines.flow.Flow

class GetCatalogUseCase(private val repository: SanadTechRepository) {
    operator fun invoke(): Flow<List<SanadTechItem>> = repository.catalogFlow
}

class ManageCartUseCase(private val repository: SanadTechRepository) {
    val cart = repository.activeCart
    fun add(item: SanadTechItem) = repository.addToCart(item)
    fun remove(item: SanadTechItem) = repository.removeFromCart(item)
    fun clear() = repository.clearCart()
}

class ProcessCheckoutUseCase(private val repository: SanadTechRepository) {
    suspend operator fun invoke(paymentMethod: String = "Mada Contactless EMV"): SanadTechOrder =
        repository.checkout(paymentMethod)
}

class ObserveHardwareTelemetryUseCase(private val repository: SanadTechRepository) {
    operator fun invoke(): Flow<SanadTechTelemetry> = repository.telemetryFlow
}

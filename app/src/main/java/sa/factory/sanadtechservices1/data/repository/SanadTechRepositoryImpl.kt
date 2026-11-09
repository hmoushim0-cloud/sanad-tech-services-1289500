package sa.factory.sanadtechservices1.data.repository

import sa.factory.sanadtechservices1.domain.repository.SanadTechRepository
import sa.factory.sanadtechservices1.hardware.HardwareSensorDriver
import sa.factory.sanadtechservices1.model.SanadTechItem
import sa.factory.sanadtechservices1.model.SanadTechOrder
import sa.factory.sanadtechservices1.model.SanadTechTelemetry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SanadTechRepositoryImpl(
    private val sensorDriver: HardwareSensorDriver
) : SanadTechRepository {

    private val _catalog = MutableStateFlow<List<SanadTechItem>>(listOf(
        SanadTechItem("1", "دورة السلامة الأساسية", "تدريب تفاعلي على بروتوكولات السلامة التشغيلية مع شهادة معتمدة من الهيئة العامة للطيران المدني", 150.0, "معتمد", "تدريب السلامة"),
        SanadTechItem("2", "تدريب محاكاة الطيران", "جلسات محاكاة واقعية لسيناريوهات الطوارئ مع تحليل فوري عبر GPS", 280.0, "فوري", "إدارة العمليات"),
        SanadTechItem("3", "تقرير تتبع الرحلة", "تحليل بيانات GPS مع معاملات مدى الآمنة لمراجعة مسارات الطيران", 95.0, "جديد", "مدفوعات مدى")
    ))
    override val catalogFlow: Flow<List<SanadTechItem>> = _catalog.asStateFlow()

    private val _cart = MutableStateFlow<List<SanadTechItem>>(emptyList())
    override val activeCart: StateFlow<List<SanadTechItem>> = _cart.asStateFlow()

    override val telemetryFlow: Flow<SanadTechTelemetry> = sensorDriver.observePeripheralStream()
        .map { frame ->
            SanadTechTelemetry(
                peripheralName = frame.peripheralName,
                status = frame.connectionState,
                liveReading = frame.livePayload,
                batteryPct = 96,
                signalDbm = -54
            )
        }

    override fun addToCart(item: SanadTechItem) {
        val current = _cart.value.toMutableList()
        current.add(item)
        _cart.value = current
    }

    override fun removeFromCart(item: SanadTechItem) {
        val current = _cart.value.toMutableList()
        current.remove(item)
        _cart.value = current
    }

    override fun clearCart() {
        _cart.value = emptyList()
    }

    override suspend fun checkout(paymentMethod: String): SanadTechOrder {
        val currentItems = _cart.value.toList()
        val subtotal = currentItems.sumOf { it.priceSar }
        val vat = subtotal * 0.15
        val total = subtotal + vat
        val order = SanadTechOrder(
            orderId = "ORD-${System.currentTimeMillis() % 100000}",
            items = currentItems,
            subtotalSar = subtotal,
            vatSar = vat,
            totalSar = total,
            paymentMethod = paymentMethod
        )
        clearCart()
        return order
    }

    override suspend fun refreshCatalog() {
        // Offline-first sync logic
    }
}

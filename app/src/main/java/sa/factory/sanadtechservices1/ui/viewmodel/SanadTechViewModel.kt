package sa.factory.sanadtechservices1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sa.factory.sanadtechservices1.domain.usecase.*
import sa.factory.sanadtechservices1.model.SanadTechItem
import sa.factory.sanadtechservices1.model.SanadTechOrder
import sa.factory.sanadtechservices1.model.SanadTechTelemetry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SanadTechUiState(
    val catalog: List<SanadTechItem> = emptyList(),
    val filteredCatalog: List<SanadTechItem> = emptyList(),
    val selectedCategory: String = "الكل",
    val cart: List<SanadTechItem> = emptyList(),
    val telemetry: SanadTechTelemetry? = null,
    val lastOrder: SanadTechOrder? = null,
    val isCheckingOut: Boolean = false,
    val activeTab: String = "home"
)

class SanadTechViewModel(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val processCheckoutUseCase: ProcessCheckoutUseCase,
    private val observeHardwareTelemetryUseCase: ObserveHardwareTelemetryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SanadTechUiState())
    val uiState: StateFlow<SanadTechUiState> = _uiState.asStateFlow()

    init {
        // Observe Catalog
        getCatalogUseCase().onEach { list ->
            _uiState.update { it.copy(catalog = list, filteredCatalog = list) }
        }.launchIn(viewModelScope)

        // Observe Cart
        manageCartUseCase.cart.onEach { cartItems ->
            _uiState.update { it.copy(cart = cartItems) }
        }.launchIn(viewModelScope)

        // Observe Telemetry
        observeHardwareTelemetryUseCase().onEach { telem ->
            _uiState.update { it.copy(telemetry = telem) }
        }.launchIn(viewModelScope)
    }

    fun selectCategory(category: String) {
        _uiState.update { state ->
            val filtered = if (category == "الكل") state.catalog else state.catalog.filter { it.category == category }
            state.copy(selectedCategory = category, filteredCatalog = filtered)
        }
    }

    fun switchTab(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun addToCart(item: SanadTechItem) = manageCartUseCase.add(item)
    fun removeFromCart(item: SanadTechItem) = manageCartUseCase.remove(item)

    fun checkout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true) }
            val order = processCheckoutUseCase()
            _uiState.update { it.copy(isCheckingOut = false, lastOrder = order, activeTab = "checkout") }
        }
    }
}

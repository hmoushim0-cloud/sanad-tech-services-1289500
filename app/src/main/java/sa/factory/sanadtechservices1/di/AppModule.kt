package sa.factory.sanadtechservices1.di

import sa.factory.sanadtechservices1.data.repository.SanadTechRepositoryImpl
import sa.factory.sanadtechservices1.domain.repository.SanadTechRepository
import sa.factory.sanadtechservices1.domain.usecase.*
import sa.factory.sanadtechservices1.hardware.HardwareSensorDriver
import sa.factory.sanadtechservices1.ui.viewmodel.SanadTechViewModel

class DiContainer {
    val sensorDriver = HardwareSensorDriver()
    val repository: SanadTechRepository = SanadTechRepositoryImpl(sensorDriver)

    val getCatalogUseCase = GetCatalogUseCase(repository)
    val manageCartUseCase = ManageCartUseCase(repository)
    val processCheckoutUseCase = ProcessCheckoutUseCase(repository)
    val observeHardwareTelemetryUseCase = ObserveHardwareTelemetryUseCase(repository)

    fun provideViewModel(): SanadTechViewModel = SanadTechViewModel(
        getCatalogUseCase,
        manageCartUseCase,
        processCheckoutUseCase,
        observeHardwareTelemetryUseCase
    )
}

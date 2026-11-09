package sa.factory.sanadtechservices1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import sa.factory.sanadtechservices1.model.SanadTechItem

@Entity(tableName = "services_items_cache")
data class SanadTechEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val priceSar: Double,
    val badge: String,
    val category: String,
    val status: String
) {
    fun toDomain(): SanadTechItem = SanadTechItem(
        id = id,
        title = title,
        description = description,
        priceSar = priceSar,
        badge = badge,
        category = category,
        status = status
    )
}

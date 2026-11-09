package sa.factory.sanadtechservices1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SanadTechDao {
    @Query("SELECT * FROM services_items_cache ORDER BY id ASC")
    fun observeAllItems(): Flow<List<SanadTechEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<SanadTechEntity>)

    @Query("DELETE FROM services_items_cache")
    suspend fun clearAll()
}

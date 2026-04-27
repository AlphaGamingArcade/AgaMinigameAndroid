package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.data.utils.Syncable
import com.alphagamingarcade.model.data.Banner
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing home-related operations.
 */
interface BannersRepository {
    fun getBanners(): Flow<List<Banner>>
}
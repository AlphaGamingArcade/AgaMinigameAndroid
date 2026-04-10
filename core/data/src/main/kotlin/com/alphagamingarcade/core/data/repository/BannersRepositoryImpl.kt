package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.core.data.utils.SyncProgress
import com.alphagamingarcade.core.network.data.BannerDataSource
import com.alphagamingarcade.core.network.model.toExternalModel
import com.alphagamingarcade.model.data.Banner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BannersRepositoryImpl @Inject constructor(
    private val bannerDataSource: BannerDataSource
) : BannersRepository {

    override fun getBanners(): Flow<List<Banner>> = flow {
        emit(bannerDataSource.getBanners().map { it.toExternalModel() })
    }

    override suspend fun sync(): Flow<SyncProgress> = flow {
        emit(SyncProgress(total = 1, current = 1, message = "Banners synced"))
    }
}
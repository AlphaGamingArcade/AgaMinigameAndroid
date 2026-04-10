package com.alphagamingarcade.core.data.repository

import com.alphagamingarcade.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserDataRepository @Inject constructor(
    private val agamgPreferencesDataSource: AgamgPreferencesDataSource,
) : UserDataRepository {
    override val userData: Flow<UserData> =
        agamgPreferencesDataSource.userData
}
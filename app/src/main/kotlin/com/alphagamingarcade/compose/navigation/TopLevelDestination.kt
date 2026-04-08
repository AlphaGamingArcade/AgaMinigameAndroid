package com.alphagamingarcade.compose.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Games
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.alphagamingarcade.compose.R
import com.alphagamingarcade.feature.auth.navigation.AuthNavGraph
import com.alphagamingarcade.feature.browse.navigation.Browse
import com.alphagamingarcade.feature.favorite.navigation.Favorite
import com.alphagamingarcade.feature.games.navigation.Games
import com.alphagamingarcade.feature.user.navigation.User
import kotlin.reflect.KClass

/**
 * Enum class representing top-level destinations in a navigation system.
 *
 * @property selectedIcon The selected icon associated with the destination.
 * @property unselectedIcon The unselected icon associated with the destination.
 * @property iconTextId The resource ID for the icon's content description text.
 * @property titleTextId The resource ID for the title text.
 * @property route The route associated with the destination.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
) {
    GAMES(
        selectedIcon = Icons.Filled.Games,
        unselectedIcon = Icons.Outlined.Games,
        iconTextId = R.string.games,
        titleTextId = R.string.games,
        route = Games::class,
    ),
    BROWSE(
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        iconTextId = R.string.browse,
        titleTextId = R.string.browse,
        route = Browse::class,
    ),
    FAVORITE(
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        iconTextId = R.string.favorite,
        titleTextId = R.string.favorite,
        route = Favorite::class,
    ),
    USER(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = R.string.user,
        titleTextId = R.string.user,
        route = User::class,
    ),
}

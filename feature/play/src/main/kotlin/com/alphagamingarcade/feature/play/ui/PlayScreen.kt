package com.alphagamingarcade.feature.play.ui

import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.res.stringResource
import androidx.activity.compose.BackHandler
import com.alphagamingarcade.core.ui.utils.SnackbarAction
import com.alphagamingarcade.feature.play.R

@Composable
internal fun PlayScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    playUrl: String,
    gameName: String
) {
    PlayScreen(
        onBackClick = onBackClick,
        playUrl = playUrl,
        gameName = gameName
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayScreen(
    onBackClick: () -> Unit,
    playUrl: String,
    gameName: String
) {
    var isLoading by remember { mutableStateOf(true) }
    var showExitDialog by remember { mutableStateOf(false) }

    // Handle the back button action with confirmation
    val handleBackClick = {
        showExitDialog = true
    }

    // Confirm exit and call the actual back action
    val confirmExit = {
        showExitDialog = false
        onBackClick.invoke()
    }

    // Intercept all back navigation (hardware button, swipe, system back)
    BackHandler {
        handleBackClick()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = handleBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                title = {
                    Text(
                        text = gameName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // ← WebView is bounded below the TopAppBar
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                isLoading = true
                            }
                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false
                            }
                        }

                        addJavascriptInterface(
                            PlayJsInterface(handleBackClick),
                            "Android"
                        )
                        loadUrl(playUrl)
                    }
                }
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    // Exit confirmation dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = {
                showExitDialog = false
            },
            title = { Text(stringResource(R.string.quit_game)) },
            text = { Text(stringResource(R.string.quit_game_confirmation, gameName)) },
            confirmButton = {
                TextButton(
                    onClick = confirmExit
                ) {
                    Text(stringResource(R.string.quit))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}

/**
 * JavaScript interface for communication between WebView and Android.
 */
class PlayJsInterface(
    private val onBackClick: () -> Unit,
) {
    @JavascriptInterface
    fun onBackClick() {
        onBackClick.invoke()
    }
}
package com.alphagamingarcade.feature.play.ui

import android.app.Activity
import android.graphics.Bitmap
import android.view.ViewGroup
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
    val context = LocalContext.current
    val activity = context as? Activity

    var isLoading by remember { mutableStateOf(true) }
    var showExitDialog by remember { mutableStateOf(false) }
    var isFullScreen by remember { mutableStateOf(false) }

    fun dispatchResize(webView: WebView) {
        webView.evaluateJavascript(
            "window.dispatchEvent(new Event('resize'));",
            null
        )
    }

    LaunchedEffect(isFullScreen) {
        activity?.window?.let { window ->
            val controller = WindowInsetsControllerCompat(window, window.decorView)

            if (isFullScreen) {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                controller.show(WindowInsetsCompat.Type.systemBars())
                WindowCompat.setDecorFitsSystemWindows(window, true)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            activity?.window?.let { window ->
                WindowInsetsControllerCompat(window, window.decorView)
                    .show(WindowInsetsCompat.Type.systemBars())

                WindowCompat.setDecorFitsSystemWindows(window, true)
            }
        }
    }

    val handleBackClick = {
        if (isFullScreen) {
            isFullScreen = false
        } else {
            showExitDialog = true
        }
    }

    val confirmExit = {
        showExitDialog = false
        onBackClick.invoke()
    }

    BackHandler {
        handleBackClick()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        containerColor = Color.Black,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (!isFullScreen) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = handleBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    title = {
                        Text(
                            text = gameName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .then(
                    if (isFullScreen) {
                        Modifier
                    } else {
                        Modifier.padding(innerPadding)
                    }
                )
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { webViewContext ->
                    WebView(webViewContext).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        setBackgroundColor(android.graphics.Color.BLACK)

                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                            useWideViewPort = true
                            loadWithOverviewMode = true
                            builtInZoomControls = false
                            displayZoomControls = false

                            userAgentString = "$userAgentString AGA-Mobile-App Android"
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                isLoading = true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false

                                view?.evaluateJavascript(
                                    "window.dispatchEvent(new Event('resize'));",
                                    null
                                )
                            }
                        }

                        addJavascriptInterface(
                            PlayJsInterface(handleBackClick),
                            "Android"
                        )

                        loadUrl(playUrl)
                    }
                },
                update = { webView ->
                    webView.setBackgroundColor(android.graphics.Color.BLACK)
                    dispatchResize(webView)
                }
            )

            SmallFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = if (isFullScreen) {
                            WindowInsets.statusBars.asPaddingValues()
                                .calculateTopPadding() + 12.dp
                        } else {
                            12.dp
                        },
                        end = 12.dp
                    ),
                onClick = { isFullScreen = !isFullScreen },
                containerColor = Color.Black.copy(alpha = 0.75f),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = if (isFullScreen) {
                        Icons.Default.FullscreenExit
                    } else {
                        Icons.Default.Fullscreen
                    },
                    contentDescription = if (isFullScreen) {
                        "Exit fullscreen"
                    } else {
                        "Enter fullscreen"
                    }
                )
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = {
                showExitDialog = false
            },
            title = { Text(stringResource(R.string.quit_game)) },
            text = {
                Text(stringResource(R.string.quit_game_confirmation, gameName))
            },
            confirmButton = {
                TextButton(onClick = confirmExit) {
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
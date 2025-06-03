package com.example.storylyjetpackcompose

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appsamurai.storyly.Story
import com.appsamurai.storyly.StorylyInit
import com.appsamurai.storyly.StorylyListener
import com.appsamurai.storyly.StorylyProductListener
import com.appsamurai.storyly.StorylyView
import com.appsamurai.storyly.config.StorylyConfig
import com.example.storylyjetpackcompose.ui.theme.StorylyJetpackComposeTheme

const val TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NfaWQiOjc2MCwiYXBwX2lkIjo0MDUsImluc19pZCI6NDA0fQ.1AkqOy_lsiownTBNhVOUKc91uc9fDcAxfQZtpm3nj40"

class MainActivity : ComponentActivity() {

    // Caches StorylyView during navigation to preserve its state.
    internal var storylyViewInstance: StorylyView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StorylyJetpackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // Setup Jetpack Navigation
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            StorylyScreen(navigateTo = {
                                // Navigate to a different screen
                                navController.navigate("secondScreen")
                            })
                        }
                        composable("secondScreen") {
                            SecondScreen(onBack = {
                                // Navigate back to StorylyScreen
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }

    /**
     * Retrieves the cached StorylyView instance for reuse.
     * Cleared after retrieval to ensure single use.
     */
    fun getStorylyView(): StorylyView? {
        val view = storylyViewInstance
        storylyViewInstance = null // Clear after retrieving
        return view
    }

    /**
     * Caches the StorylyView instance before navigating away.
     */
    fun cacheStorylyView(storylyView: StorylyView) {
        storylyViewInstance = storylyView
    }
}

@Composable
internal fun SecondScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Second Screen")
        Button(onClick = onBack) {
            Text("Go Back")
        }
    }
}

@Composable
internal fun StorylyScreen(
    navigateTo: () -> Unit
) {
    val activity = LocalContext.current as? MainActivity

    // Creates and remembers a StorylyView instance, handling its lifecycle and configuration.
    val storylyView = rememberStorylyView(
        TOKEN,
        storylyListener = object : StorylyListener {
            override fun storylyActionClicked(
                storylyView: StorylyView,
                story: Story
            ) {
                // On story interaction: pause story, cache StorylyView, then navigate.
                storylyView.pauseStory()
                activity?.cacheStorylyView(storylyView) // Cache the view for state restoration
                navigateTo() // Perform navigation
            }
        }
    )

    // Resumes story playback when storylyView is (re)composed or navigated back.
    LaunchedEffect(storylyView) {
        storylyView.resumeStory()
    }

    // Wraps the Android StorylyView for use in Jetpack Compose.
    StorylyComposeView(Modifier.fillMaxWidth().wrapContentHeight(), storylyView)
}

@Composable
internal fun rememberStorylyView(token: String, config: StorylyConfig? = null, storylyListener: StorylyListener? = null, productListener: StorylyProductListener? = null): StorylyView {
    val context = LocalContext.current
    val activity = context as? MainActivity

    // Remembers StorylyView across recompositions. Uses cached instance if available.
    val storylyView = remember(context, token, config) {
        // Attempt to retrieve a cached StorylyView from Activity.
        activity?.getStorylyView() ?: StorylyView(context).apply {
            // Otherwise, create a new StorylyView and initialize it.
            this.storylyInit = StorylyInit(
                token,
                config ?: StorylyConfig.Builder().build()
            )
        }
    }
    storylyView.storylyListener = storylyListener
    storylyView.storylyProductListener = productListener
    return storylyView
}

@Composable
internal fun StorylyComposeView(modifier: Modifier, storylyView: StorylyView) {
    // Embeds the Android StorylyView in Compose UI.
    AndroidView(
        modifier = modifier,
        factory = { context ->
            // Creates a FrameLayout to host StorylyView.
            // Removes StorylyView from any previous parent.
            (storylyView.parent as? ViewGroup)?.removeView(storylyView)
            FrameLayout(context).apply {
                addView(storylyView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
            }
        }, update = { parent ->
            // Ensures StorylyView is correctly parented during recompositions.
            (storylyView.parent as? ViewGroup)?.removeView(storylyView)
            parent.removeAllViews() // Clear previous views
            parent.addView(storylyView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        },
    )
}
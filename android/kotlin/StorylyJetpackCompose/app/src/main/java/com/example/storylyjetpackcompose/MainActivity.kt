package com.example.storylyjetpackcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.appsamurai.storyly.Story
import com.appsamurai.storyly.StoryGroup
import com.appsamurai.storyly.StorylyDataSource
import com.appsamurai.storyly.StorylyInit
import com.appsamurai.storyly.StorylyListener
import com.appsamurai.storyly.StorylyView
import com.appsamurai.storyly.config.StorylyConfig
import com.example.storylyjetpackcompose.ui.theme.StorylyJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StorylyJetpackComposeTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LazyTestView()
                }
            }
        }
    }
}

@Composable
fun LazyTestView() {
    val storylyView = rememberStorylyView(
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NfaWQiOjc2MCwiYXBwX2lkIjo0MDUsImluc19pZCI6NDA0fQ.1AkqOy_lsiownTBNhVOUKc91uc9fDcAxfQZtpm3nj40",
        object : StorylyListener {
            override fun storylyLoaded(
                storylyView: StorylyView,
                storyGroupList: List<StoryGroup>,
                dataSource: StorylyDataSource
            ) {
                Log.d("[Storyly]", "storylyLoaded:${storyGroupList.size}")
            }

            override fun storylyLoadFailed(storylyView: StorylyView, errorMessage: String) {
                Log.d("[Storyly]", "storylyLoadFailed:errorMessage:$errorMessage")
            }

            override fun storylyActionClicked(storylyView: StorylyView, story: Story) {
                Log.d("[Storyly]", "storylyActionClicked:story:${story.actionUrl}")
            }
        }
    )

    LazyColumn {
        item {
            StorylyComposeView(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                storylyView,
            )
        }
        items(50) { index ->
            Text(
                text = "Item $index",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun rememberStorylyView(token: String, storylyListener: StorylyListener): StorylyView {
    val context = LocalContext.current
    val storylyView = remember(context, token) {
        StorylyView(context).apply {
            storylyInit = StorylyInit(
                token,
                StorylyConfig.Builder().build()
            )
            this.storylyListener = storylyListener
        }
    }
    return storylyView
}

@Composable
fun StorylyComposeView(modifier: Modifier, storylyView: StorylyView) {
    AndroidView(
        modifier = modifier,
        factory = { storylyView },
        update = { _ -> }
    )
}
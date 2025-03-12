package com.example.storylyjetpackcompose

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.appsamurai.storyly.StorylyInit
import com.appsamurai.storyly.StorylyView
import com.example.storylyjetpackcompose.ui.theme.StorylyJetpackComposeTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private val dummyItems = createDummyFlow()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StorylyJetpackComposeTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val storylyView = createStorylyView(StorylyInitItem("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NfaWQiOjc2MCwiYXBwX2lkIjo0MDUsImluc19pZCI6NDA0fQ.1AkqOy_lsiownTBNhVOUKc91uc9fDcAxfQZtpm3nj40"))
                    val pagingList = dummyItems.collectAsLazyPagingItems()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        items(pagingList.itemCount, key = { itemId -> itemId }) { itemId ->
                            val pagingItem = pagingList[itemId]
                            when (pagingItem) {
                                is StorylyInitItem -> storylyView?.let {
                                    println("TEST: lazy: compose")
                                    StorylyView(storylyView)
                                }
                                is DummyItem -> {
                                    Box(Modifier.fillMaxWidth().height(120.dp).background(pagingItem.color))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun createDummyFlow(): MutableStateFlow<PagingData<PagingItem>> {
        val fakeData: List<PagingItem> = List(10) {
            if (it == 0) {
                StorylyInitItem("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NfaWQiOjc2MCwiYXBwX2lkIjo0MDUsImluc19pZCI6NDA0fQ.1AkqOy_lsiownTBNhVOUKc91uc9fDcAxfQZtpm3nj40")
            } else {
                DummyItem(Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat(),
                    alpha = 1f
                ))
            }
        }
        val pagingData = PagingData.from(fakeData)
        return MutableStateFlow(pagingData)
    }
}

@Composable
fun createStorylyView(
    item: StorylyInitItem?
): StorylyView? {
    item ?: return null
    println("TEST: createStorylyView: inside")
    val context = LocalContext.current
    val storylyView = remember(item) {
        StorylyView(context).apply {
            this.storylyInit = StorylyInit(item.token)
        }
    }
    return storylyView
}

@Composable
fun StorylyView(storylyView: StorylyView?) {
    storylyView ?: return

    AndroidView(modifier = Modifier
        .fillMaxWidth(),
        factory = { _ ->
            // Ensure the storylyView is removed from its parent if it has one
            (storylyView.parent as? ViewGroup)?.removeView(storylyView)
            storylyView
        },
        update = { view ->
        }
    )
}


interface PagingItem

data class DummyItem(
    val color: Color,
) : PagingItem

data class StorylyInitItem(
    val token: String,
) : PagingItem

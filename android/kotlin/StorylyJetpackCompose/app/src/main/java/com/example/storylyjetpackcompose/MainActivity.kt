package com.example.storylyjetpackcompose

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.appsamurai.storyly.StoryGroup
import com.appsamurai.storyly.StorylyDataSource
import com.appsamurai.storyly.StorylyInit
import com.appsamurai.storyly.StorylyListener
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val pagingList = dummyItems.collectAsLazyPagingItems()

                    val storylyViewItem = remember(pagingList.itemSnapshotList.items) {
                        pagingList.itemSnapshotList.items.firstOrNull { it is StorylyInitItem } as? StorylyInitItem
                    }
                    val storylyView = rememberStorylyView(storylyViewItem, object : StorylyListener {
                        override fun storylyLoaded(
                            storylyView: StorylyView,
                            storyGroupList: List<StoryGroup>,
                            dataSource: StorylyDataSource
                        ) {
                            println("TEST: StorylyListener: ${storylyView} - ${dataSource.value} - ${storyGroupList.size}")
                        }
                    })

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        items(pagingList.itemCount, key = { itemId -> itemId }) { itemId ->
                            when (val pagingItem = pagingList[itemId]) {
                                is StorylyInitItem -> {
                                    storylyView?.let {
                                        StorylyViewCompose(storylyView)
                                    }
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

    private fun createDummyFlow(): MutableStateFlow<PagingData<PagingItem>> {
        val fakeData: List<PagingItem> = List(20) {
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
fun rememberStorylyView(
    item: StorylyInitItem?,
    listener: StorylyListener,
): StorylyView? {
    val context = LocalContext.current
    val storylyView = remember(item) {
        println("TEST: createStorylyView: $item")
        item?.let {
            StorylyView(context).apply {
                this.storylyInit = StorylyInit(item.token)
                this.storylyListener = listener
            }
        }
    }
    return storylyView
}

@Composable
fun StorylyViewCompose(storylyView: StorylyView) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .defaultMinSize(minHeight = 1.dp),
        factory = { _ ->
            println("TEST: StorylyViewCompose: AndroidView: factory: ${storylyView}")
            (storylyView.parent as? ViewGroup)?.removeView(storylyView)
            storylyView
        },
        update = { view ->
            println("TEST: StorylyViewCompose: AndroidView: update: ${storylyView}")
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

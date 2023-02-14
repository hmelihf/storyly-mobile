package com.appsamurai.storylydemo

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.appsamurai.storyly.StorylyInit
import com.appsamurai.storyly.StorylyView
import com.appsamurai.storyly.moments.Config
import com.appsamurai.storyly.moments.StorylyMomentsManager
import com.appsamurai.storyly.storylylist.MomentsItem
import com.appsamurai.storylydemo.databinding.ActivityMainBinding
import com.appsamurai.storylydemo.styling_templates.ui.RoundImageView
import com.appsamurai.storylydemo.styling_templates.ui.dpToPixel
import com.bumptech.glide.Glide

class BasicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var ugc: StorylyMomentsManager

    private val createStoryView: MomentsItem by lazy {
        val container: LinearLayout = LinearLayout(this).also {
            it.orientation = LinearLayout.VERTICAL
            it.gravity = Gravity.CENTER_HORIZONTAL
            it.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            val imageContainer: FrameLayout = FrameLayout(this).also { frameLayout ->
                frameLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                val roundImageView: RoundImageView = RoundImageView(this).apply {
                    layoutParams = FrameLayout.LayoutParams(dpToPixel(80f).toInt(), dpToPixel(80f).toInt())
                    borderColor = arrayOf(Color.parseColor("#0DFFFFFF"), Color.parseColor("#0DFFFFFF"))
                    avatarBackgroundColor = Color.parseColor("#FF212121")
                }
                val imageView = AppCompatImageView(this).apply {
                    layoutParams = FrameLayout.LayoutParams(dpToPixel(20f).toInt(), dpToPixel(20f).toInt(), Gravity.CENTER)
                }
                frameLayout.addView(roundImageView)
                frameLayout.addView(imageView)
                frameLayout.setOnClickListener {
                    ugc.openStoryCreator()
                }
            }
            val textView = AppCompatTextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                setLines(2)
                textSize = 12f
                text = "New Story"
                setTextColor(Color.WHITE)
            }
            it.addView(imageContainer)
            it.addView(textView)
        }
        MomentsItem(container)
    }

    private val showMyStoriesView: MomentsItem by lazy {
        val wrapper: FrameLayout = FrameLayout(this).also {
            it.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            val container: LinearLayout = LinearLayout(this).also {
                it.orientation = LinearLayout.VERTICAL
                it.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                val userRoundImageView = RoundImageView(this).apply {
                    layoutParams = FrameLayout.LayoutParams(dpToPixel(80f).toInt(), dpToPixel(80f).toInt())
                    borderColor = arrayOf(
                        Color.parseColor("#FFFED169"), Color.parseColor("#FFFA7C20"), Color.parseColor("#FFC9287B"),
                        Color.parseColor("#FF962EC2"), Color.parseColor("#FFFED169")
                    )
                }
                val textView = AppCompatTextView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    setLines(2)
                    textSize = 12f
                    setTextColor(Color.WHITE)
                }
                it.addView(userRoundImageView)
                it.addView(textView, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                })
                it.setOnClickListener {
                    ugc.openUserStories()
                }
            }
            it.addView(container)
        }
        MomentsItem(wrapper)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.storylyView.storylyInit = StorylyInit("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NfaWQiOjU2NzAsImFwcF9pZCI6MTA1NTYsImluc19pZCI6MTExOTB9.2_HU2WCS5RvP86MNwOGcBNYTj-DLZRzOcOKsHdNbpLk")
        ugc = StorylyMomentsManager(
            this, Config(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NfaWQiOjU2NzAsImluc19pZCI6MSwiaWF0IjoxNjU3MDA4MDUwfQ.IKS1YF08rortq4usRs71N8h9k-c4S28OeZ0TdxXxTw4",
"krutqbT6g8N8IeN+wRS5EWeqrLGLkrmv3jk6na0nFncoiY0rVujEK0ThLsJQ2RO/NhOw09N1GmDsxT9rhNH//UwIDb9UwHKNXNHepqLwGKLMLO0bLYWX529MmoDvMUhfKn8rApMDLUe2ShybuxgFihGaokL/KI9x3Vr0e1XxsBs5glXOKD5JlHLTmn7uDnyb//IRBSNeui9ybj4/fbz/Z88jLxmjFXmz+MjZrhhTKv9N2I7VZdqbiG90uEOq+9ka7NXboIKjk7yo+JSwZ32cVRdK5xOUucBxrsTaRE4sstrZX6PU7DwwokZ5//a1iqbfHqNdIC78CRAwZ4tq0AdTZOgpI5vbeuIkyNb6gsv3eizaTBZbSwqUKZdskfiWKIOV/t5klz79jOAmua2jkVQWIm6ebF1id+avC5EWLNJwoWTPgEIVHLtTE5FeoSBvW8TkrBl8l8wqdKKXBiqZloK5QA=="
            )
        )

        binding.storylyView.setMomentsItem(listOf(createStoryView, showMyStoriesView))
    }
}

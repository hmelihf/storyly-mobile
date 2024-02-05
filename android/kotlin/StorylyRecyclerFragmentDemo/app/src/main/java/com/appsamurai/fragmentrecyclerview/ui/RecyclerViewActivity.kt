package com.appsamurai.fragmentrecyclerview.ui

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appsamurai.fragmentrecyclerview.R
import com.appsamurai.fragmentrecyclerview.databinding.ActivityRecyclerViewBinding
import com.appsamurai.fragmentrecyclerview.databinding.FragmentRecyclerViewBinding
import com.appsamurai.storyly.StorylyInit
import com.appsamurai.storyly.StorylyView

const val STORYLY_INSTANCE_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NfaWQiOjU1NiwiYXBwX2lkIjoxNjExOSwiaW5zX2lkIjoxOTI3M30.ptvyHD553yAorcVW8nBllAfWeX20X8cOgg1mACjlp_M"

class RecyclerViewFragment : Fragment() {

    private var _binding: FragmentRecyclerViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Now you can access your views using the binding object
        val context = requireContext()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = StorylyAdapter().apply { notifyDataSetChanged() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class StorylyAdapter: RecyclerView.Adapter<StorylyAdapter.ViewHolder>() {
        private val StorylyViewType = 0
        private val NonStorylyViewType = 1

        private val nonStorylyViewCount = 44
        private val storylyViewCount = 4

        // It's safer and network friendly creating StorylyView instance and using it inside RecyclerView instead of creating for each RecyclerView
        // item. StorylyView does a network request each initialization time. So, doing a creation and initialization process when each time view
        // is recycled performance might be affected. Keeping a StorylyView instance or several instances inside array and use them each time can
        // improve performance according to your case. Please note that, if the view is not recycled in your case(less items in RecyclerView,
        // more RecyclerView pool size), this improvement may not have an effect on your case.
        private val storylyView = StorylyView(requireActivity()).apply { storylyInit = StorylyInit(STORYLY_INSTANCE_TOKEN) }

        private inner class ViewHolder(view: View): RecyclerView.ViewHolder(view.apply { layoutParams = ViewGroup.LayoutParams(
            MATCH_PARENT, context.resources.getDimension(R.dimen.st_storyly_large_height).toInt()) })

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // Creating StorylyView inside onCreateViewHolder might cause rendering of view several times and network request as a plus.
            return when (viewType) {
                StorylyViewType -> {
                    // ViewHolder(StorylyView(this@RecyclerViewActivity).apply { /** storylyInit = StorylyInit(STORYLY_INSTANCE_TOKEN) */ })
                    ViewHolder(storylyView)
                }
                else -> ViewHolder(View(requireContext()).apply { setBackgroundColor(Color.rgb((30..200).random(), (30..200).random(), (30..200).random())) })
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                val activity = requireActivity()
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                val secondFragment = SecondFragment()
                val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.parent_holder, secondFragment)
                transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
                transaction.commit()
            }
        }

        override fun getItemCount(): Int = nonStorylyViewCount + storylyViewCount

        override fun getItemViewType(position: Int): Int = if (position % (nonStorylyViewCount / storylyViewCount + 1) == 0) StorylyViewType else NonStorylyViewType
    }
}

class SecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FrameLayout(requireContext()).apply {
            setBackgroundColor(Color.YELLOW)
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

}

class RecyclerViewActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fragment = RecyclerViewFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.parent_holder, fragment, RecyclerViewFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}
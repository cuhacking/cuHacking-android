package com.cuhacking.app.home.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cuhacking.app.R
import com.cuhacking.app.data.Result
import com.cuhacking.app.di.injector
import com.cuhacking.app.info.ui.InfoFragment
import com.cuhacking.app.ui.PageFragment
import com.cuhacking.app.ui.cards.CardAdapter
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

class HomeFragment : PageFragment(R.layout.info_fragment) {

    private val viewModel: HomeViewModel by viewModels { injector.homeViewModelFactory() }

    private val cardAdapter by lazy { CardAdapter() }

    private var tapTargetPrompt: MaterialTapTargetPrompt? = null

    companion object {
        fun newInstance() = InfoFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.recycler_view).adapter = cardAdapter
        viewModel.cards.observe(this, Observer(cardAdapter::submitList))

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_layout).apply {
            setOnRefreshListener { viewModel.refreshInfo() }
            setColorSchemeResources(R.color.colorPrimary)
        }

        viewModel.refreshInfo()

        viewModel.refreshState.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = it is Result.Loading
        })

        viewModel.showOnboarding.observe(this, Observer {
            if (it == true) {
                val backgroundColor = ColorUtils.setAlphaComponent(ContextCompat.getColor(requireContext(), R.color.colorPrimary), 0xF4)

                tapTargetPrompt = MaterialTapTargetPrompt.Builder(this)
                    .setTarget(R.id.profile)
                    .setIcon(R.drawable.ic_person)
                    .setPrimaryText(R.string.onboard_login_title)
                    .setSecondaryText(R.string.onboard_login_description)
                    .setBackgroundColour(backgroundColor)
                    .setIconDrawableColourFilter(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    .setPromptStateChangeListener { _, state ->
                        if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                            viewModel.dismissOnboarding()
                        }
                    }
                    .show()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        tapTargetPrompt?.dismiss()
    }
}
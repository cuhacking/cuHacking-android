package com.cuhacking.app.home.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cuhacking.app.R
import com.cuhacking.app.data.Result
import com.cuhacking.app.di.injector
import com.cuhacking.app.info.ui.InfoFragment
import com.cuhacking.app.info.ui.InfoFragmentDirections
import com.cuhacking.app.ui.cards.CardAdapter
import com.google.android.material.appbar.MaterialToolbar

class HomeFragment : Fragment(R.layout.info_fragment) {

    private val viewModel: HomeViewModel by viewModels { injector.homeViewModelFactory() }

    private val cardAdapter by lazy { CardAdapter() }

    companion object {
        fun newInstance() = InfoFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialToolbar>(R.id.toolbar).apply {
            setOnMenuItemClickListener(::onOptionsItemSelected)
        }

        view.findViewById<RecyclerView>(R.id.recycler_view).adapter = cardAdapter
        viewModel.cards.observe(this, Observer(cardAdapter::submitList))

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_layout)
        swipeRefreshLayout
            .setOnRefreshListener { viewModel.refreshInfo() }

        viewModel.refreshInfo()

        viewModel.refreshState.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = it is Result.Loading
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                findNavController().navigate(InfoFragmentDirections.login())
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
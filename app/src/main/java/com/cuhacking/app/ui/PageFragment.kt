package com.cuhacking.app.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cuhacking.app.R
import com.cuhacking.app.data.auth.AuthState
import com.cuhacking.app.data.auth.UserRole
import com.cuhacking.app.di.injector
import com.cuhacking.app.info.ui.InfoFragmentDirections
import com.google.android.material.appbar.MaterialToolbar

open class PageFragment(@LayoutRes layout: Int) : Fragment(layout) {
    protected val mainViewModel: MainViewModel by activityViewModels { injector.mainViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.apply {
            setOnMenuItemClickListener(::onOptionsItemSelected)
        }

        mainViewModel.authState.observe(this, Observer {
            it ?: return@Observer
            toolbar.menu.findItem(R.id.admin).isVisible =
                it is AuthState.Authenticated && it.role == UserRole.ADMIN
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                val authState = mainViewModel.authState.value
                if (authState is AuthState.Authenticated) {
                    findNavController().navigate(InfoFragmentDirections.actionGlobalProfile(authState.uid))
                } else {
                    findNavController().navigate(InfoFragmentDirections.login())
                }
            }
            R.id.admin -> findNavController().navigate(InfoFragmentDirections.scan())
        }

        return super.onOptionsItemSelected(item)
    }
}

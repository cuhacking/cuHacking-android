/*
 *    Copyright 2019 cuHacking
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.cuhacking.app.info.ui

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
import com.cuhacking.app.admin.ui.AdminActivityDirections
import com.cuhacking.app.data.Result
import com.cuhacking.app.di.injector
import com.cuhacking.app.ui.cards.CardAdapter
import com.google.android.material.appbar.MaterialToolbar

class InfoFragment : Fragment(R.layout.info_fragment) {

    private val viewModel: InfoViewModel by viewModels { injector.infoViewModelFactory() }

    private val infoCardAdapter by lazy { CardAdapter(viewModel) }

    companion object {
        fun newInstance() = InfoFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialToolbar>(R.id.toolbar).apply {
            setOnMenuItemClickListener(::onOptionsItemSelected)
        }

        view.findViewById<RecyclerView>(R.id.recycler_view).adapter = infoCardAdapter
        viewModel.cards.observe(this, Observer(infoCardAdapter::submitList))

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_layout)
        swipeRefreshLayout.isEnabled = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                findNavController().navigate(InfoFragmentDirections.login())
            }
            R.id.admin -> {
                findNavController().navigate(InfoFragmentDirections.scan())
            }
        }

        return super.onOptionsItemSelected(item)
    }

}

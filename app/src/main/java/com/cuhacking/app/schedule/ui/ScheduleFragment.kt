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

package com.cuhacking.app.schedule.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.app.R
import com.cuhacking.app.di.injector
import com.cuhacking.app.schedule.data.models.EventUiModel

class ScheduleFragment : Fragment(R.layout.schedule_fragment) {

    companion object {
        fun newInstance() = ScheduleFragment()
    }

    private val viewModel: ScheduleViewModel by viewModels { injector.scheduleViewModelFactory() }

    private val scheduleAdapter by lazy { ScheduleAdapter(findNavController()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = scheduleAdapter
        }

        viewModel.scheduleData.observe(this, Observer(::updateScheduleUi))
    }

    private fun updateScheduleUi(data: List<EventUiModel>?) {
        data ?: return

        view?.findViewById<RecyclerView>(R.id.recycler_view)?.apply {
            scheduleAdapter.submitList(data)
            if (data.isNotEmpty()) {
                addItemDecoration(ScheduleTimeDecoration(context, data))
            }
        }
    }
}

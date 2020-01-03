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

package com.cuhacking.app.profile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.app.R
import com.cuhacking.app.di.injector
import net.glxn.qrgen.android.QRCode

class ProfileActivity : AppCompatActivity(R.layout.activity_profile) {

    private val viewModel by viewModels<ProfileViewModel> { injector.profileViewModelFactory() }
    private val args by navArgs<ProfileActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        val adapter = ProfileAdapter()
        findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter

        viewModel.setUser(args.uid)
        viewModel.profileItems.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.logout -> {
                viewModel.logUserOut()
                Toast.makeText(this, R.string.logout_success, Toast.LENGTH_LONG).show()
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}

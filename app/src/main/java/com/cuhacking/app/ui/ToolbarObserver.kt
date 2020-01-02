package com.cuhacking.app.ui

import androidx.lifecycle.Observer
import com.cuhacking.app.data.auth.AuthState
import com.google.android.material.appbar.MaterialToolbar

class ToolbarObserver(private val toolbar: MaterialToolbar) : Observer<AuthState> {
    override fun onChanged(state: AuthState?) {
        state ?: return


    }
}
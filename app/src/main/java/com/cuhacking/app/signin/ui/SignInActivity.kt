package com.cuhacking.app.signin.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cuhacking.app.R
import com.cuhacking.app.di.injector
import com.cuhacking.app.profile.ui.ProfileActivity
import com.cuhacking.app.signin.data.model.SignInUiModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(R.layout.activity_sign_in) {

    private val viewModel: SignInViewModel by viewModels { injector.signInViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<MaterialToolbar>(R.id.material_toolbar).apply {
            setSupportActionBar(this)
        }
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        viewModel.userData.observe(this, Observer { uiModel ->
            when (uiModel) {
                is SignInUiModel.Success -> {
                    Toast.makeText(this, R.string.login_success, Toast.LENGTH_LONG).show()
                    finish()
                    startActivity(Intent(this, ProfileActivity::class.java).apply {
                        putExtra(
                            "uid",
                            uiModel.uid
                        )
                    })
                }
                is SignInUiModel.Failure -> {
                    Log.d("SignIn", "Failure!")
                    findViewById<TextInputLayout>(R.id.email_text_layout).isErrorEnabled = true

                    with(findViewById<TextInputLayout>(R.id.password_text_layout)) {
                        isErrorEnabled = true
                        error = context.getString(R.string.login_error)
                    }
                }
            }

            findViewById<MaterialButton>(R.id.sign_in_button).text =
                if (uiModel is SignInUiModel.Loading) "" else getString(
                    R.string.login
                )

            findViewById<ProgressBar>(R.id.loading_indicator).visibility =
                if (uiModel is SignInUiModel.Loading) View.VISIBLE else View.GONE
        })

        findViewById<EditText>(R.id.email_edit_text).setOnFocusChangeListener { _, hasFocus ->
            findViewById<TextInputLayout>(R.id.email_text_layout).error =
                if (!hasFocus && findViewById<EditText>(R.id.email_edit_text).text.toString() == "") {
                    getString(R.string.required)
                } else {
                    null
                }
        }

        findViewById<EditText>(R.id.password_edit_text).setOnFocusChangeListener { _, hasFocus ->
            findViewById<TextInputLayout>(R.id.password_text_layout).error =
                if (!hasFocus && findViewById<EditText>(R.id.password_edit_text).text.toString() == "") {
                    getString(R.string.required)
                } else {
                    null
                }
        }

        findViewById<EditText>(R.id.password_edit_text).setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit()
            }

            true
        }

        findViewById<MaterialButton>(R.id.sign_in_button).setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val email = findViewById<EditText>(R.id.email_edit_text).text.toString()
        val password = findViewById<EditText>(R.id.password_edit_text).text.toString()

        viewModel.performSignIn(email, password)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}

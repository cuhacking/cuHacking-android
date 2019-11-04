package com.cuhacking.app.signin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cuhacking.app.R
import com.cuhacking.app.di.injector
import com.cuhacking.app.signin.data.model.SignInUiModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(R.layout.activity_sign_in) {

    private val viewModel: SignInViewModel by viewModels { injector.signInViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.userData.observe(this, Observer { uiModel ->
            when (uiModel) {
                is SignInUiModel.Success -> {
                    Log.d("SignIn", "Success! ${uiModel.name}")
                    finish()
                }
                is SignInUiModel.Failure -> {
                    Log.d("SignIn", "Failure!")
                    findViewById<TextInputLayout>(R.id.email_text_layout).isErrorEnabled = true

                    with (findViewById<TextInputLayout>(R.id.password_text_layout)) {
                        isErrorEnabled = true
                        error = "Invalid Email or Password"
                    }
                }
            }
        })

        findViewById<MaterialButton>(R.id.sign_in_button).setOnClickListener {
            val email = findViewById<EditText>(R.id.email_edit_text).text.toString()
            val password = findViewById<EditText>(R.id.password_edit_text).text.toString()

            viewModel.performSignIn(email, password)
        }
    }
}

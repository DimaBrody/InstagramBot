package com.bot.insta.ui.login

import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import com.bot.insta.components.dependencies.prefs
import com.bot.insta.components.navigation.navigateMain
import com.bot.insta.internal.ok
import com.bot.insta.internal.setMultipleTextWatcher
import com.bot.insta.internal.text
import com.bot.insta.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : BaseFragment<LoginViewModel>(
    LoginViewModel::class
) {

    override fun View.createViews() {
        setupDefaults()
        setupObservers()
    }

    private fun View.setupDefaults() {
        login_button_login.setMultipleTextWatcher(
            login_edit_password,
            login_edit_username
        )

        login_button_fb.setMultipleTextWatcher(
            login_edit_password,
            login_edit_username
        )

        login_button_login.setOnClickListener {
            disableButtons()
            processLogin(false,
                login_edit_username, login_edit_password)
        }

        login_button_fb.setOnClickListener {
            disableButtons()
            processLogin(true,
                login_edit_username, login_edit_password)
        }
    }

    private fun processLogin(
        isFacebook: Boolean,
        usernameEditText: EditText,
        passwordEditText: EditText
    ) {
        val password = passwordEditText.text()
        val username = usernameEditText.text()

        mViewModel.requestLogin(isFacebook,username, password)
    }

    private fun View.setupObservers() {
        mViewModel.loginData.observe(fragment, Observer {
            if (it.status.ok()) {
                prefs.saveProfile(it)
                navigateMain()
            } else {
                login_desc_fail.text = it.message
                enableButtons()
            }
        })
    }

    private fun View.disableButtons() {
        login_button_login.isEnabled = false
        login_button_fb.isEnabled = false
    }

    private fun View.enableButtons() {
        login_button_login.isEnabled = true
        login_button_fb.isEnabled = true
    }
}
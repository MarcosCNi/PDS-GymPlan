package com.example.gymplan.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gymplan.R
import com.example.gymplan.databinding.FragmentResetPasswordBinding
import com.example.gymplan.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() = with(binding) {
        resetPasswordBtn.setOnClickListener {
            if (resetPasswordTextInput.editText?.text.isNullOrEmpty()){
                Toast.makeText(context, R.string.empty_text, Toast.LENGTH_SHORT).show()
            }else{
                viewModel.email = resetPasswordTextInput.editText?.text.toString()
                viewModel.resetPassword(context)
            }
        }
        backToLogin.setOnClickListener {
            findNavController().navigate(R.id.signInFragment)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentResetPasswordBinding = FragmentResetPasswordBinding.inflate(inflater, container, false)
}
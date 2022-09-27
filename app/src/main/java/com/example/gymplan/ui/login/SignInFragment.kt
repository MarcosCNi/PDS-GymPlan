package com.example.gymplan.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gymplan.R
import com.example.gymplan.databinding.FragmentSignInBinding
import com.example.gymplan.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() = with(binding) {
        signInBtn.setOnClickListener {
            viewModel.email = userEmail.editText.toString()
            viewModel.password = userPassword.editText.toString()
            if(viewModel.email.isNullOrEmpty() || viewModel.password.isNullOrEmpty()){
                Toast.makeText(context, R.string.empty_text, Toast.LENGTH_SHORT).show()
            }else{
                viewModel.signIn(context)
            }

        }
        goToSignUpBtn.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignInBinding = FragmentSignInBinding.inflate(inflater, container, false)
}
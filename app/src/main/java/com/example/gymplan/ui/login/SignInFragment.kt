package com.example.gymplan.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gymplan.R
import com.example.gymplan.databinding.FragmentSignInBinding
import com.example.gymplan.ui.base.BaseFragment
import com.example.gymplan.utils.gone
import com.example.gymplan.utils.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        checkUser()
    }

    private fun checkUser() {
        viewModel.user.observe(viewLifecycleOwner){ user ->
            if (user != null)
                findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun setupUI() = with(binding) {
        signInBtn.setOnClickListener {
            viewModel.email = userEmail.editText?.text.toString().trim()
            viewModel.password = userPassword.editText?.text.toString().trim()
            if(viewModel.email.isNullOrEmpty() || viewModel.password.isNullOrEmpty()){
                Toast.makeText(context, R.string.empty_text, Toast.LENGTH_SHORT).show()
            }else{
                viewModel.signIn(context)
            }

        }
        goToSignUpBtn.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }
        forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.resetPasswordFragment)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignInBinding = FragmentSignInBinding.inflate(inflater, container, false)
}
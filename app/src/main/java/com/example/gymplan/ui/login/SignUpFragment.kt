package com.example.gymplan.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gymplan.R
import com.example.gymplan.databinding.FragmentSignUpBinding
import com.example.gymplan.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() = with(binding) {
        signUpBtn.setOnClickListener {
            if (newPassword.editText?.text.toString() == confirmNewPassword.editText?.text.toString()){

                viewModel.password = newPassword.editText?.text.toString()
                viewModel.email = userEmail.editText?.text.toString()

                if(viewModel.email.isNullOrEmpty() || viewModel.password.isNullOrEmpty()){
                    Toast.makeText(context, R.string.empty_text, Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.signUp(context)
                    if(viewModel.email!!.contains("@"))
                    findNavController().navigate(R.id.signInFragment)
                }
            }else{
                Toast.makeText(context, R.string.different_password, Toast.LENGTH_SHORT).show()
            }
        }
        alreadyHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.signInFragment)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)
}
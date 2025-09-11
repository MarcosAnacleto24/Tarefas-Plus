package com.example.tarefas.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.tarefas.R
import com.example.tarefas.databinding.FragmentRegisterBinding
import com.example.tarefas.ui.FirebaseHelper
import com.example.tarefas.util.initToolbar
import com.example.tarefas.util.showBottomSheet


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        FirebaseHelper.getAuth()
        initListeners()

    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener {
            validateData()
        }
    }
    private fun validateData() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val passwordRepeat = binding.edtPasswordRepeat.text.toString().trim()
        
        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                if (passwordRepeat.isNotEmpty()) {
                   if (password == passwordRepeat) {
                       binding.progressBar.isVisible = true

                       registerUser(email, password)
                   } else {
                       showBottomSheet(message = getString(R.string.txt_register_password_repeat_error))
                   }
                } else {
                    showBottomSheet(message = getString(R.string.txt_register_password_repeat_empty) )
                }

            } else {
                showBottomSheet(message = getString(R.string.txt_register_password))
            }
        } else {
            showBottomSheet(message = getString(R.string.txt_register_email))
        }
    }

    private fun registerUser(email: String, password: String) {
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                   findNavController().navigate(R.id.action_global_homeFragment)
                } else {
                    binding.progressBar.isVisible = false
                   showBottomSheet(
                       message = getString(FirebaseHelper.messageFirebase(task.exception?.message.toString()))
                   )
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.tarefas.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tarefas.R
import com.example.tarefas.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.initToolbar(toolbar: Toolbar) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
}

fun Fragment.showBottomSheet(
    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: String,
    onClick: () -> Unit = {}
) {

    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    val binding: BottomSheetBinding =
        BottomSheetBinding.inflate(layoutInflater,null,false)

    binding.txtTitle.text = getText(titleDialog ?: R.string.txt_title_warning)
    binding.txtMessage.text = message
    binding.btnOK.text = getText(titleButton ?: R.string.btn_title_warning)
    binding.btnOK.setOnClickListener {
        onClick()
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.setContentView(binding.root)
    bottomSheetDialog.show()
}
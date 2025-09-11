package com.example.tarefas.ui

import com.example.tarefas.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database


class FirebaseHelper {

    companion object {

        fun getDatabase() = Firebase.database.reference

        fun getAuth() = Firebase.auth

        fun getIdUser() = getAuth().currentUser?.uid ?: ""

        fun isAuthentication() = getAuth().currentUser != null

        fun messageFirebase(error: String): Int {
           return when {
               error.contains("There is no user record corresponding to this identifier") -> {
                   R.string.account_not_register
               }

               error.contains("The email address is badly formatted") -> {
                   R.string.email_invalid_
               }

               error.contains("The password is invalid or the user does not have a password") -> {
                   R.string.password_invalid_
               }

               error.contains("The email address is already in use by another account") -> {
                   R.string.this_email_used
               }

               error.contains("Password should be at least 6 characters") -> {
                   R.string.enter_password_stronger
               }

               else -> {
                   R.string.error_save
               }



           }
        }


    }
}
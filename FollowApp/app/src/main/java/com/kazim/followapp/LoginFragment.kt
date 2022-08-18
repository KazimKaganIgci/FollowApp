package com.kazim.followapp


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kazim.followapp.databinding.LoginFragmentBinding

class LoginFragment:Fragment(R.layout.login_fragment) {
    private var binding:LoginFragmentBinding ?=null
    private lateinit var auth:FirebaseAuth
    private lateinit var fstore:FirebaseFirestore
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginFragmentBinding.bind(view)
        auth =Firebase.auth
        fstore=Firebase.firestore
       val currentUser =auth.currentUser

        if (currentUser!=null){
            val id =auth.currentUser!!.uid
            val ref =fstore.collection("Users").document(id)
            ref.get().addOnSuccessListener {
                if (it !=null){
                    if(it.getString("User") !=null){
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDetailsFragment())
                    }
                    if(it.getString("Admin")!=null){
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainRecyclerFragment())
                    }

                }

            }

        }
        binding!!.OnclickBtn.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        binding!!.registerbtn.setOnClickListener {

            val email = binding!!.emailtext.text.toString()
            val password = binding!!.passwordtext.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    checkUserAccessLevel(it.user!!.uid)

                }.addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }else{
                Toast.makeText(context, "Enter E-mail and Password", Toast.LENGTH_LONG).show()

            }
        }

    }

       private fun checkUserAccessLevel(uid: String) {
        val rf =fstore.collection("Users").document(uid)
         rf.get().addOnSuccessListener {
             if (it !=null){
                 if(it.getString("User") !=null){
                     findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDetailsFragment())

                     //admin
                 }
                 if(it.getString("Admin")!=null){
                     findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainRecyclerFragment())
                 }

             }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }




}
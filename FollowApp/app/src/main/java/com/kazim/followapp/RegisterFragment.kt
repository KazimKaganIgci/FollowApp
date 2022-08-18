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
import com.kazim.followapp.databinding.RegisterFragmentBinding
import kotlin.collections.HashMap

class RegisterFragment:Fragment(R.layout.register_fragment) {

    private lateinit var auth:FirebaseAuth
    private lateinit var fstore:FirebaseFirestore
    private var binding:RegisterFragmentBinding ?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=RegisterFragmentBinding.bind(view)
        auth =Firebase.auth
        fstore =Firebase.firestore

        val yonetici = binding!!.adminbox
        val personal =binding!!.userbox

        yonetici.setOnClickListener {
           if (yonetici.isChecked){
               personal.isChecked =false
           }

        }
        personal.setOnClickListener {
            if (personal.isChecked){
                yonetici.isChecked =false
            }

        }




        binding!!.girisekranibtn.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
        binding!!.registerbtn.setOnClickListener {
            val email = binding!!.emailtext.text.toString()
            val password =binding!!.passwordtext.text.toString()
            val apassword =binding!!.passwordagaintext.text.toString()
            val name =binding!!.Adtext.text.toString()


                if (password == apassword) {
                    if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() &&(personal.isChecked ||yonetici.isChecked)) {
                        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                            val user = auth.currentUser
                            if (user != null) {
                                val reference = fstore.collection("Users").document(user.uid)
                                //  var userInfo =HashMap<String,Any>()
                                if (personal.isChecked) {
                                    val userInfo: HashMap<String, Any> = hashMapOf(
                                        "Fullname" to name,
                                        "Email" to email,
                                        "User" to "1"
                                    )
                                    reference.set(userInfo)

                                }
                                if (yonetici.isChecked) {
                                    val userInfo: HashMap<String, Any> = hashMapOf(
                                        "Fullname" to name,
                                        "Email" to email,
                                        "Admin" to "1"

                                    )
                                    reference.set(userInfo)


                                }
                                if(yonetici.isChecked){
                                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToMainRecyclerFragment())
                                }
                                if (personal.isChecked){
                                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToDetailsFragment())
                                }
                            }
                            Toast.makeText(context, "Kayıt oldunuz", Toast.LENGTH_SHORT).show()

                           // findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                        }.addOnFailureListener {
                            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()

                        }


                    } else {
                        Toast.makeText(context, "Enter E-mail and Password", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    Toast.makeText(context, "Password is not the same", Toast.LENGTH_SHORT).show()

                }


        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }
}
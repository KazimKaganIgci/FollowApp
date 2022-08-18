package com.kazim.followapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kazim.followapp.databinding.ApproveFragmentBinding

class ApproveFragment:Fragment(R.layout.approve_fragment) {
    private var binding:ApproveFragmentBinding ?=null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=ApproveFragmentBinding.bind(view)

        binding!!.fab.setOnClickListener {
         FirebaseAuth.getInstance().signOut()
         findNavController().navigate(ApproveFragmentDirections.actionApproveFragmentToLoginFragment())
        }
    }










    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}
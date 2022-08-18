package com.kazim.followapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kazim.followapp.databinding.ImageFragmentBinding

class imageFragment:Fragment(R.layout.image_fragment) {
    private var binding:ImageFragmentBinding ?=null
    private lateinit var imageurl:String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding =ImageFragmentBinding.bind(view)
        arguments?.let {
            imageurl =imageFragmentArgs.fromBundle(it).image
        }
        Glide.with(context as Context).load(imageurl).into(binding!!.imageView6)

        binding!!.fab.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(imageFragmentDirections.actionImageFragmentToLoginFragment())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding =null
    }
}
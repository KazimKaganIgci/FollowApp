package com.kazim.followapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kazim.followapp.databinding.SelectFragmentBinding
import java.io.Serializable

class SelectFragment:Fragment(R.layout.select_fragment) {
    private var binding:SelectFragmentBinding ?=null
    private lateinit var data:Post
    private lateinit var image:String



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding =SelectFragmentBinding.bind(view)

        arguments?.let {
        data =SelectFragmentArgs.fromBundle(it).data
        image =data.url.toString()
        }
        Glide.with(context as Context).load(data.url).into(binding!!.imageView)
        binding!!.basliktext.text =data.baslik
        binding!!.konutext.text =data.konu
        binding!!.nametext.text=data.name

        binding!!.fab.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(SelectFragmentDirections.actionSelectFragmentToLoginFragment())
        }
        val action =SelectFragmentDirections.actionSelectFragmentToImageFragment(image)

        binding!!.imageView.setOnClickListener {
            Navigation.findNavController(it).navigate(action)
        }





    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null


    }
}
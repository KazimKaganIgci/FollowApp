package com.kazim.followapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kazim.followapp.databinding.MainrecyclerFragmentBinding

class RecyclerFragment:Fragment(R.layout.mainrecycler_fragment) {
    private var binding:MainrecyclerFragmentBinding?=null
    private lateinit var fStore:FirebaseFirestore
    private lateinit var postArrayList:ArrayList<Post>
    private lateinit var feedAdapter:DetailAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=MainrecyclerFragmentBinding.bind(view)
        fStore =Firebase.firestore
        postArrayList =ArrayList<Post>()


        getData()

        binding!!.recyclerViewArt.layoutManager=LinearLayoutManager(activity)
        feedAdapter=DetailAdapter(postArrayList)
        binding!!.recyclerViewArt.adapter =feedAdapter




       binding!!.fab.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            findNavController().navigate(RecyclerFragmentDirections.actionMainRecyclerFragmentToLoginFragment())

        }

    }

    private fun getData(){
        fStore.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show()
            }else{
                if (value !=null){
                    if(!value.isEmpty){
                       postArrayList.clear()

                        val docs =value.documents
                        for (doc in docs) {
                            val name = doc.get("Name") as String
                            val email = doc.get("Email") as String
                            val url = doc.get("DownloadUrl") as String
                            val konu = doc.get("Konu") as String
                            val baslik = doc.get("Baslik") as String


                            val post = Post(name, email, url, konu,baslik)
                            postArrayList.add(post)

                        }
                        feedAdapter.notifyDataSetChanged()


                    }


                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }
}
package com.kazim.followapp

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kazim.followapp.databinding.DetailsFragmentBinding
import java.util.*
import kotlin.collections.HashMap

class DetailsFragment:Fragment(R.layout.details_fragment) {
    private var binding:DetailsFragmentBinding?=null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedPicture: Uri?=null
    private lateinit var auth:FirebaseAuth
    private lateinit var fstore:FirebaseFirestore
    private lateinit var fStorage:FirebaseStorage
    private lateinit var name:String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding =DetailsFragmentBinding.bind(view)
        auth =Firebase.auth
        fstore=Firebase.firestore
        fStorage=Firebase.storage
        registerLauncher()


        binding!!.btn.setOnClickListener {
            val uuid =UUID.randomUUID()
            val imageName ="$uuid.jpg"
            val rf =fStorage.reference

            val data =fstore.collection("Users").document(auth.currentUser!!.uid)
            data.get().addOnSuccessListener {
                if (it !=null){
                    name = it.getString("Fullname").toString()

                }

            }


            val imagerf =rf.child("images").child(imageName)
            if (selectedPicture !=null){
                imagerf.putFile(selectedPicture!!).addOnSuccessListener {
                    val uploadimagerf=fStorage.reference.child("images").child(imageName)
                    uploadimagerf.downloadUrl.addOnSuccessListener {
                        val downloadurl =it.toString()
                        val fstorelist = HashMap<String,Any?>()
                        fstorelist.put("DownloadUrl",downloadurl)
                        fstorelist.put("Email",auth.currentUser!!.email!!.toString())
                        //fstorelist.put("Date",Timestamp.now())
                        fstorelist.put("Name",name)
                        fstorelist.put("Baslik",binding!!.basliktext.text.toString())
                        fstorelist.put("Konu",binding!!.konutext.text.toString())
                        fstorelist.put("date",Timestamp.now())






                        fstore.collection("Posts").add(fstorelist).addOnSuccessListener {
                            findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToApproveFragment())

                        }.addOnFailureListener {
                            Toast.makeText(context,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }
                    }


                }.addOnFailureListener{
                   Toast.makeText(context,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }

        }
        binding!!.imageView4.setOnClickListener {
            if (ContextCompat.checkSelfPermission(context as Activity,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(binding!!.root,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                        //request permission
                        permissionLauncher.launch((Manifest.permission.READ_EXTERNAL_STORAGE))
                    }.show()
                }else{
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }else{
                val intentToGallery =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                 activityResultLauncher.launch(intentToGallery)
            }



        }
        binding!!.fab.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToLoginFragment())
        }

    }
    private fun registerLauncher(){
        activityResultLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if (result.resultCode ==RESULT_OK){
                val intentFromResult =result.data
                if (intentFromResult !=null){
                  selectedPicture  =intentFromResult.data
                    selectedPicture?.let {
                        binding!!.imageView4.setImageURI(it)
                    }
                }
            }

        }
        permissionLauncher =registerForActivityResult(ActivityResultContracts.RequestPermission()){result->

            if (result){
                val intentToGallery =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)


            }else{
                Toast.makeText(context,"Permission needed",Toast.LENGTH_LONG).show()
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding =null
    }
}
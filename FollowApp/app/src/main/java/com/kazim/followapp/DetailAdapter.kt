package com.kazim.followapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kazim.followapp.databinding.RowLayoutBinding

class DetailAdapter(private val post:ArrayList<Post>): RecyclerView.Adapter<DetailAdapter.RecyclerHolder>() {
    class RecyclerHolder(val binding: RowLayoutBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val binding =RowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecyclerHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
       // holder.binding.basliktext.text =post[position].baslik
        holder.binding.hotmailtext.text =post[position].email
        //holder.binding.konutext.text =post[position].konu
        holder.binding.nametext.text=post[position].name

        Glide.with(holder.itemView.context).load(post[position].url).into(holder.binding.imageView)

        holder.itemView.setOnClickListener {
            val action =RecyclerFragmentDirections.actionMainRecyclerFragmentToSelectFragment2(post[position])
            Navigation.findNavController(it).navigate(action)

        }


    }

    override fun getItemCount(): Int {
        return post.size
    }
}
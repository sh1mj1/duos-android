package com.example.duos.ui.signup.localSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.Friend
import com.example.duos.data.entities.LargeLocal
import com.example.duos.databinding.ItemFragmentMyFriendListBinding
import com.example.duos.databinding.ItemSignupLargeLocalListBinding

class LargeLocalSearchRVAdapter(private val largeLocalList : List<LargeLocal>) : RecyclerView.Adapter<LargeLocalSearchRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LargeLocalSearchRVAdapter.ViewHolder {
        val binding: ItemSignupLargeLocalListBinding = ItemSignupLargeLocalListBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(largeLocalList[position], position)
    }

    override fun getItemCount(): Int = largeLocalList.size


    inner class ViewHolder(val binding: ItemSignupLargeLocalListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(largeLocal : LargeLocal, position: Int){

        }
    }
}
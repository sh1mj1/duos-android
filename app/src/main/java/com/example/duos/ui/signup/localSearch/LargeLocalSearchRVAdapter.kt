package com.example.duos.ui.signup.localSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.LocalCategory
import com.example.duos.databinding.ItemSignupLargeLocalListBinding

class LargeLocalSearchRVAdapter(private val localCategoryList : List<LocalCategory>) : RecyclerView.Adapter<LargeLocalSearchRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LargeLocalSearchRVAdapter.ViewHolder {
        val binding: ItemSignupLargeLocalListBinding = ItemSignupLargeLocalListBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(localCategoryList[position], position)
    }

    override fun getItemCount(): Int = localCategoryList.size


    inner class ViewHolder(val binding: ItemSignupLargeLocalListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(localCategory : LocalCategory, position: Int){

        }
    }
}
package com.example.duos.ui.signup.localSearch

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.R
import com.example.duos.data.entities.LocationCategoryList
import com.example.duos.databinding.ItemLocationCategoryListBinding

class LocationCategoryRVAdapter(private val locationCategoryList : List<LocationCategoryList>) : RecyclerView.Adapter<LocationCategoryRVAdapter.ViewHolder>() {

    var row_index : Int = 0

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onSetLocationList(locationCategory: LocationCategoryList)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LocationCategoryRVAdapter.ViewHolder {
        val binding: ItemLocationCategoryListBinding = ItemLocationCategoryListBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(locationCategoryList[position], position)
        val item = holder.itemView.findViewById<Button>(R.id.location_category_list_text_btn)
        if(row_index == position) {
           item.setBackgroundResource(R.drawable.local_selected_btn)
            item.setTextColor(ContextCompat.getColor(item.context, R.color.white))
        }
        else {
            item.setBackgroundResource(R.drawable.local_unselected_btn)
            item.setTextColor(ContextCompat.getColor(item.context, R.color.nero))
        }
    }

    override fun getItemCount(): Int = locationCategoryList.size


    inner class ViewHolder(val binding: ItemLocationCategoryListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(locationCategory : LocationCategoryList, position: Int){
            binding.locationCategoryListTextBtn.text = locationCategory.locationCategoryName

            itemView.setOnClickListener {
                mItemClickListener.onSetLocationList(locationCategory)
                row_index = position
                notifyDataSetChanged()
            }
        }
    }
}
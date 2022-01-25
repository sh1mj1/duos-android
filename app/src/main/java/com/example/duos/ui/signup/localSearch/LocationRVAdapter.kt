package com.example.duos.ui.signup.localSearch

import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.R
import com.example.duos.data.entities.LocationList
import com.example.duos.databinding.ItemLocationListBinding

class LocationRVAdapter(private val locationList : List<LocationList>) : RecyclerView.Adapter<LocationRVAdapter.ViewHolder>() {

    var row_index : Int = 0

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onChooseLocation(location: LocationList)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LocationRVAdapter.ViewHolder {
        val binding: ItemLocationListBinding = ItemLocationListBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        binding.locationListTextBtn.gravity = Gravity.CENTER_VERTICAL or Gravity.START
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(locationList[position], position)

        val item = holder.itemView.findViewById<Button>(R.id.location_list_text_btn)
        if(row_index == position) {
            item.setTextColor(ContextCompat.getColor(item.context, R.color.primary))
            item.typeface = Typeface.DEFAULT_BOLD
        }
        else {
            item.setTextColor(ContextCompat.getColor(item.context, R.color.nero))
            item.typeface = Typeface.DEFAULT
        }
    }

    override fun getItemCount(): Int = locationList.size


    inner class ViewHolder(val binding: ItemLocationListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(location : LocationList, position: Int){
            binding.locationListTextBtn.text = location.locationName

            itemView.setOnClickListener {
                mItemClickListener.onChooseLocation(location)
                row_index = position
                notifyDataSetChanged()
            }
        }
    }
}
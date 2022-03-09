import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.dailyMatching.SearchHistory
import com.example.duos.databinding.ItemDailyMatchingSearchBinding

class DailyMatchingSearchHistoryRVAdapter(val historyDeleteClickListener: (String) -> Unit) :
    ListAdapter<SearchHistory, DailyMatchingSearchHistoryRVAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemDailyMatchingSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(searchHistoryModel: SearchHistory) {
            binding.itemDailyMatchingRecentSearchTv.text = searchHistoryModel.keyword
            binding.itemDailyMatchingRecentSearchDeleteIv.setOnClickListener {
                historyDeleteClickListener(searchHistoryModel.keyword.orEmpty())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDailyMatchingSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
        Log.d("DailyMatchingSearchActivity", "onBindViewHolder의 currentList[position] ${currentList[position]}")
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SearchHistory>() {
            override fun areItemsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(
                oldItem: SearchHistory,
                newItem: SearchHistory
            ): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}
package com.dicoding.escore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.escore.data.remote.response.PredictionsItem
import com.dicoding.escore.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(private val onItemClicked: (String) -> Unit) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val items = mutableListOf<PredictionsItem>()

    // Fungsi untuk menambahkan item ke adapter dan urutkan berdasarkan createdAt
    fun setItems(newItems: List<PredictionsItem>) {
        items.clear()
        items.addAll(newItems)
        // Sortir data berdasarkan createdAt secara descending
        items.sortByDescending { it.createdAt }
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PredictionsItem) {
            binding.cardTitle.text = item.title
            binding.cardDate.text = formatDate(item.createdAt)
            binding.cardScore.text = item.predictedResult?.score

            // Set click listener
            binding.root.setOnClickListener {
                item.id?.let { id -> onItemClicked(id) }
            }
        }

        private fun formatDate(dateString: String?): String {
            if (dateString.isNullOrEmpty()) return "Unknown Date"
            return try {
                // Potong string untuk menghapus milidetik tambahan
                val trimmedDateString = dateString.substringBeforeLast(".") + "Z"

                // Format input date
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")

                // Format output date
                val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                val date = inputFormat.parse(trimmedDateString)
                date?.let { outputFormat.format(it) } ?: "Unknown Date"
            } catch (e: Exception) {
                "Invalid Date"
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

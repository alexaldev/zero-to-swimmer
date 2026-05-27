package com.alexallafi.app.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexallafi.app.presentation.SwimSessionListItem
import com.alexallafi.app.presentation.databinding.CellViewItemCollapsedBinding

class HistoryAdapter : ListAdapter<SwimSessionListItem.SwimSessionViewItem, HistoryAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CellViewItemCollapsedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: CellViewItemCollapsedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SwimSessionListItem.SwimSessionViewItem) {
            binding.sessionTitle.text = item.title
            binding.sessionMessage.text = item.message
            // History items are always completed
            binding.completedIcon.setImageResource(com.alexallafi.app.presentation.R.drawable.ic_check)
            // Disable click/interactions for history view
            binding.root.isClickable = false
            binding.collapseIcon.visibility = android.view.View.GONE
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<SwimSessionListItem.SwimSessionViewItem>() {
        override fun areItemsTheSame(oldItem: SwimSessionListItem.SwimSessionViewItem, newItem: SwimSessionListItem.SwimSessionViewItem) = 
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SwimSessionListItem.SwimSessionViewItem, newItem: SwimSessionListItem.SwimSessionViewItem) = 
            oldItem == newItem
    }
}

package com.alexallafi.app.presentation.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexallafi.app.presentation.SwimSessionListItem
import com.alexallafi.app.presentation.databinding.CellHistoryViewItemCollapsedBinding
import com.alexallafi.app.presentation.databinding.CellViewItemCollapsedBinding

class HistoryAdapter : ListAdapter<HistoryViewItem, HistoryAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            CellHistoryViewItemCollapsedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: CellHistoryViewItemCollapsedBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryViewItem) {
            binding.sessionTitle.text = item.sessionTitle
            binding.sessionMessage.text = item.completedAt
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<HistoryViewItem>() {
        override fun areItemsTheSame(
            oldItem: HistoryViewItem,
            newItem: HistoryViewItem,
        ) = oldItem.sessionTitle == newItem.sessionTitle

        override fun areContentsTheSame(
            oldItem: HistoryViewItem,
            newItem: HistoryViewItem,
        ) = oldItem == newItem
    }
}

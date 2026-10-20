package com.alexallafi.app.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexallafi.app.presentation.databinding.CellHistoryViewItemCollapsedBinding
import com.alexallafi.app.presentation.databinding.CellViewItemWeekHeaderBinding

class HistoryAdapter : ListAdapter<HistoryListItem, RecyclerView.ViewHolder>(DiffCallback) {
    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is HistoryListItem.MonthHeader -> TYPE_HEADER
            is HistoryListItem.SessionItem -> TYPE_ITEM
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = CellViewItemWeekHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }

            else -> {
                val binding = CellHistoryViewItemCollapsedBinding.inflate(inflater, parent, false)
                SessionViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        when (holder) {
            is HeaderViewHolder if item is HistoryListItem.MonthHeader -> holder.bind(item)
            is SessionViewHolder if item is HistoryListItem.SessionItem -> holder.bind(item)
        }
    }

    class HeaderViewHolder(
        private val binding: CellViewItemWeekHeaderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryListItem.MonthHeader) {
            binding.weekIdView.text = item.month
            binding.completedTextView.text = ""
        }
    }

    class SessionViewHolder(
        private val binding: CellHistoryViewItemCollapsedBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryListItem.SessionItem) {
            binding.sessionTitle.text = item.sessionTitle
            binding.sessionMessage.text = item.completedAt
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<HistoryListItem>() {
        override fun areItemsTheSame(
            oldItem: HistoryListItem,
            newItem: HistoryListItem,
        ): Boolean =
            when (oldItem) {
                is HistoryListItem.MonthHeader if newItem is HistoryListItem.MonthHeader -> {
                    oldItem.month == newItem.month
                }

                is HistoryListItem.SessionItem if newItem is HistoryListItem.SessionItem -> {
                    oldItem.entryId == newItem.entryId
                }

                else -> {
                    false
                }
            }

        override fun areContentsTheSame(
            oldItem: HistoryListItem,
            newItem: HistoryListItem,
        ): Boolean = oldItem == newItem
    }
}

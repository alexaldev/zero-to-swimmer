package com.alexallafi.app.presentation.trainingProgram

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.alexallafi.app.presentation.R
import com.alexallafi.app.presentation.databinding.CellViewItemCollapsedBinding
import com.alexallafi.app.presentation.databinding.CellViewItemExpandedBinding
import com.alexallafi.app.presentation.databinding.CellViewItemOverviewBinding
import com.alexallafi.app.presentation.databinding.CellViewItemWeekHeaderBinding

class SwimSessionsViewAdapter(
    private val context: Context,
    private val expandListener: (SwimSessionListItem.SwimSessionViewItem) -> Unit,
    private val collapseListener: (SwimSessionListItem.SwimSessionViewItem) -> Unit,
    private val onCompletedToggleListener: (SwimSessionListItem.SwimSessionViewItem) -> Unit,
    private val onFavoriteToggleListener: (SwimSessionListItem.SwimSessionViewItem) -> Unit,
    private val scrollToNextAvailableListener: () -> Unit,
) : ListAdapter<SwimSessionListItem, ViewHolder>(SwimSessionListItemDiffCallback()) {
    companion object {
        const val VIEW_TYPE_OVERVIEW = 419
        const val VIEW_TYPE_WEEK_HEADER = 420
        const val VIEW_TYPE_SESSION_COLLAPSED = 421
        const val VIEW_TYPE_SESSION_EXPANDED = 422
    }

    fun updateData(newItems: List<SwimSessionListItem>) {
        submitList(newItems)
    }

    override fun getItemViewType(position: Int): Int =
        when (val sessionViewItem = getItem(position)) {
            is SwimSessionListItem.WeekHeaderItem -> {
                VIEW_TYPE_WEEK_HEADER
            }

            is SwimSessionListItem.SwimSessionViewItem -> {
                if (sessionViewItem.isExpanded) {
                    VIEW_TYPE_SESSION_EXPANDED
                } else {
                    VIEW_TYPE_SESSION_COLLAPSED
                }
            }

            is SwimSessionListItem.ProgressOverviewViewItem -> {
                VIEW_TYPE_OVERVIEW
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_WEEK_HEADER -> {
                HeaderViewHolder(
                    CellViewItemWeekHeaderBinding.inflate(layoutInflater, parent, false),
                )
            }

            VIEW_TYPE_SESSION_EXPANDED -> {
                ExpandedSessionViewHolder(
                    CellViewItemExpandedBinding.inflate(layoutInflater, parent, false),
                )
            }

            VIEW_TYPE_SESSION_COLLAPSED -> {
                CollapsedSessionViewHolder(
                    CellViewItemCollapsedBinding.inflate(layoutInflater, parent, false),
                )
            }

            VIEW_TYPE_OVERVIEW -> {
                OverviewViewHolder(
                    CellViewItemOverviewBinding.inflate(layoutInflater, parent, false),
                )
            }

            else -> {
                throw IllegalArgumentException("Unknown viewType: $viewType")
            }
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        when (holder) {
            is HeaderViewHolder -> holder.bind(item as SwimSessionListItem.WeekHeaderItem)
            is CollapsedSessionViewHolder -> holder.bind(item as SwimSessionListItem.SwimSessionViewItem)
            is ExpandedSessionViewHolder -> holder.bind(item as SwimSessionListItem.SwimSessionViewItem)
            is OverviewViewHolder -> holder.bind(item as SwimSessionListItem.ProgressOverviewViewItem)
        }
    }

    class HeaderViewHolder(
        private val viewBinding: CellViewItemWeekHeaderBinding,
    ) : ViewHolder(viewBinding.root) {
        fun bind(item: SwimSessionListItem.WeekHeaderItem) {
            viewBinding.weekIdView.text = item.weekText
        }
    }

    inner class CollapsedSessionViewHolder(
        private val viewBinding: CellViewItemCollapsedBinding,
    ) : ViewHolder(viewBinding.root) {
        fun bind(item: SwimSessionListItem.SwimSessionViewItem) {
            with(viewBinding) {
                this.sessionTitle.text = item.title
                this.sessionMessage.text = item.message
                if (item.isCompleted) completedUI() else notCompletedUI()
                completedIcon.setOnClickListener { onCompletedToggleListener(item) }
                collapseIcon.setOnClickListener { expandListener(item) }
            }
        }

        private fun notCompletedUI() {
            val icon = R.drawable.pool
            viewBinding.completedIcon.setImageResource(icon)
            viewBinding.root.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        }

        private fun completedUI() {
            val icon = R.drawable.ic_check
            viewBinding.completedIcon.setImageResource(icon)
            viewBinding.root.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grey))
        }
    }

    inner class ExpandedSessionViewHolder(
        private val viewBinding: CellViewItemExpandedBinding,
    ) : ViewHolder(viewBinding.root) {
        fun bind(item: SwimSessionListItem.SwimSessionViewItem) {
            with(viewBinding) {
                this.sessionTitle.text = item.title
                this.sessionMessage.text = item.message
                val icon = if (item.isCompleted) R.drawable.ic_check else R.drawable.pool
                completedIcon.setImageResource(icon)
                completedIcon.setOnClickListener { onCompletedToggleListener(item) }
                this.setsView.text = item.swimRounds
                collapseIcon.setOnClickListener { collapseListener(item) }
                favorite.isSelected = item.isFavorite
                favorite.setOnClickListener { onFavoriteToggleListener(item) }
            }
        }
    }

    inner class OverviewViewHolder(
        private val viewBinding: CellViewItemOverviewBinding,
    ) : ViewHolder(viewBinding.root) {
        fun bind(item: SwimSessionListItem.ProgressOverviewViewItem) {
            viewBinding.totalCompletedValue.text = item.totalCompleted
            viewBinding.nextAvailableValue.text = item.nextAvailable
            viewBinding.scrollToAvailable.setOnClickListener { scrollToNextAvailableListener() }
        }
    }
}

private class SwimSessionListItemDiffCallback : DiffUtil.ItemCallback<SwimSessionListItem>() {
    override fun areItemsTheSame(
        oldItem: SwimSessionListItem,
        newItem: SwimSessionListItem,
    ): Boolean =
        when (oldItem) {
            is SwimSessionListItem.SwimSessionViewItem if newItem is SwimSessionListItem.SwimSessionViewItem -> {
                oldItem.id == newItem.id
            }

            is SwimSessionListItem.WeekHeaderItem if newItem is SwimSessionListItem.WeekHeaderItem -> {
                oldItem.weekText == newItem.weekText
            }

            is SwimSessionListItem.ProgressOverviewViewItem if newItem is SwimSessionListItem.ProgressOverviewViewItem -> {
                true
            }

            else -> {
                false
            }
        }

    override fun areContentsTheSame(
        oldItem: SwimSessionListItem,
        newItem: SwimSessionListItem,
    ): Boolean = oldItem == newItem
}

package com.alexallafi.app.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.alexallafi.app.presentation.databinding.CellViewItemCollapsedBinding
import com.alexallafi.app.presentation.databinding.CellViewItemExpandedBinding
import com.alexallafi.app.presentation.databinding.CellViewItemWeekHeaderBinding
import java.lang.IllegalArgumentException

class SwimSessionsViewAdapter(
    private val sessionItems: MutableList<SwimSessionListItem> = mutableListOf(),
    private val expandListener: (SwimSessionListItem.SwimSessionViewItem) -> Unit,
    private val collapseListener: (SwimSessionListItem.SwimSessionViewItem) -> Unit
): RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HEADER = 420
        const val VIEW_TYPE_SESSION_COLLAPSED = 421
        const val VIEW_TYPE_SESSION_EXPANDED = 422
    }

    fun updateData(newItems: List<SwimSessionListItem>) {
        this.sessionItems.clear()
        this.sessionItems.addAll(newItems)
        this.notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when(val sessionViewItem = sessionItems[position]) {
            is SwimSessionListItem.HeaderItem -> VIEW_TYPE_HEADER
            is SwimSessionListItem.SwimSessionViewItem -> {
                if (sessionViewItem.isExpanded) VIEW_TYPE_SESSION_EXPANDED
                else VIEW_TYPE_SESSION_COLLAPSED
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                CellViewItemWeekHeaderBinding.inflate(layoutInflater, parent, false)
            )
            VIEW_TYPE_SESSION_EXPANDED -> ExpandedSessionViewHolder(
                CellViewItemExpandedBinding.inflate(layoutInflater, parent, false)
            )
            VIEW_TYPE_SESSION_COLLAPSED -> CollapsedSessionViewHolder(
                CellViewItemCollapsedBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when(holder) {
            is HeaderViewHolder -> holder.bind(sessionItems[position] as SwimSessionListItem.HeaderItem)
            is CollapsedSessionViewHolder -> holder.bind(sessionItems[position] as SwimSessionListItem.SwimSessionViewItem)
            is ExpandedSessionViewHolder -> holder.bind(sessionItems[position] as SwimSessionListItem.SwimSessionViewItem)
        }
    }

    override fun getItemCount() = this.sessionItems.size

    inner class HeaderViewHolder(
        private val viewBinding: CellViewItemWeekHeaderBinding
    ) : ViewHolder(viewBinding.root) {
        fun bind(item: SwimSessionListItem.HeaderItem) {
            viewBinding.weekIdView.text = item.startText
            viewBinding.completedTextView.text = item.endText
        }
    }

    inner class CollapsedSessionViewHolder(
        private val viewBinding: CellViewItemCollapsedBinding
    ): ViewHolder(viewBinding.root) {
        fun bind(item: SwimSessionListItem.SwimSessionViewItem) {
            with(viewBinding) {
                this.sessionTitle.text = item.title
                this.sessionMessage.text = item.message
                if (item.isCompleted) this.completedIcon.setImageResource(R.drawable.ic_check)
                collapseIcon.setOnClickListener { expandListener(item) }
            }
        }
    }

    inner class ExpandedSessionViewHolder(
        private val viewBinding: CellViewItemExpandedBinding
    ) : ViewHolder(viewBinding.root) {
        fun bind(item: SwimSessionListItem.SwimSessionViewItem) {
            with(viewBinding) {
                this.sessionTitle.text = item.title
                this.sessionMessage.text = item.message
                if (item.isCompleted) this.completedIcon.setImageResource(R.drawable.ic_check)
                this.setsView.text = item.swimRounds
                collapseIcon.setOnClickListener { collapseListener(item) }
            }
        }
    }
}
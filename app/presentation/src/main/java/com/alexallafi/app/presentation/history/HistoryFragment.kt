package com.alexallafi.app.presentation.history

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alexallafi.app.presentation.R
import com.alexallafi.app.presentation.databinding.FragmentHistoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val viewBinding by viewBinding(FragmentHistoryBinding::bind)
    private val viewModel: HistoryViewModel by viewModel()
    private val historyAdapter = HistoryAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        viewBinding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.historyItems.observe(viewLifecycleOwner) { items ->
            historyAdapter.submitList(items)
            viewBinding.tvEmptyHistory.isVisible = items.isEmpty()
            viewBinding.rvHistory.isVisible = items.isNotEmpty()
        }
    }
}

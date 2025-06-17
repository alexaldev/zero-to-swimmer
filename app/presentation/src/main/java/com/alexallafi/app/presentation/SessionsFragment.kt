package com.alexallafi.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alexallafi.app.presentation.databinding.FragmentSwimSessionsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SessionsFragment: Fragment(R.layout.fragment_swim_sessions) {

    private val viewBinding by viewBinding(FragmentSwimSessionsBinding::bind)
    private val viewModel: SessionsViewModel by viewModel()

    private lateinit var adapter: SwimSessionsViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        adapter = SwimSessionsViewAdapter(
            requireContext(),
            collapseListener = {viewModel.onAction(SwimSessionAction.CollapseSession(it))},
            expandListener = {viewModel.onAction(SwimSessionAction.ExpandSession(it))},
            onCompletedToggleListener = {viewModel.onAction(SwimSessionAction.CompletedToggled(it))}
        )

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.sessionsList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.sessionsList.adapter = this.adapter

        viewModel.sessionViewItems.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }
    }

}
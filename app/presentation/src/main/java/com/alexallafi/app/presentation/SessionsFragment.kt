package com.alexallafi.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.presentation.databinding.FragmentSwimSessionsBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SessionsFragment: Fragment(R.layout.fragment_swim_sessions) {

    private val viewBinding by viewBinding(FragmentSwimSessionsBinding::bind)

    private val repository: SwimSessionsRepository by inject()
    private val viewItemsMapper: ViewItemsMapper by inject()

    private lateinit var adapter: SwimSessionsViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        adapter = SwimSessionsViewAdapter()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.sessionsList.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.sessionsList.adapter = this.adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository
                    .observeAll()
                    .collect { adapter.updateData(viewItemsMapper.mapToViewItems(it)) }
            }

            repository.getAll()
        }
    }

}
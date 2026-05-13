package com.alexallafi.app.presentation.nextSession

import android.R.attr.text
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alexallafi.app.presentation.R
import com.alexallafi.app.presentation.databinding.FragmentNextSessionBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NextSessionFragment : Fragment(R.layout.fragment_next_session) {
    private val viewBinding by viewBinding(FragmentNextSessionBinding::bind)
    private val viewModel: NextSessionViewModel by viewModel()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel
                .favoriteViewItem
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect {
                    viewBinding.tvFavoriteDetails.text = it.sessionSetsText
                }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel
                .nextViewItem
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect {
                    viewBinding.tvNextDetails.text = it.sessionSetsText
                }
        }
    }
}

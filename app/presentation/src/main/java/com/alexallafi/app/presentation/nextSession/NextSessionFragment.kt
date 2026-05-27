package com.alexallafi.app.presentation.nextSession

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
                .collect { nextSessionViewItem ->
                    viewBinding.tvNextDetails.text = nextSessionViewItem.sessionSetsText
                    viewBinding.tvNextDistance.text = nextSessionViewItem.totalDistanceText
                    viewBinding.btnMarkCompleted.setOnClickListener {
                        when {
                            nextSessionViewItem.showConfirmState -> viewModel.onAction(UserAction.ConfirmCompletion)
                            else -> viewModel.onAction(UserAction.MarkSessionAsCompleted(nextSessionViewItem.id))
                        }
                    }
                    viewBinding.btnCancelCompletion.setOnClickListener {
                        viewModel.onAction(UserAction.CancelCompletion)
                    }
                    viewBinding.btnMarkCompleted.text =
                        when (nextSessionViewItem.showConfirmState) {
                            true -> getString(R.string.confirm_completion)
                            false -> getString(R.string.mark_as_completed)
                        }
                    viewBinding.btnCancelCompletion.visibility =
                        if (nextSessionViewItem.showConfirmState) View.VISIBLE else View.GONE
                }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel
                .overviewViewItem
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { overview ->
                    viewBinding.tvNextSummary.text = overview?.nextAvailable ?: "-"
                }
        }
    }
}

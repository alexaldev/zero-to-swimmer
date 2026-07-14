package com.alexallafi.app.presentation.trainingProgram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alexallafi.app.presentation.databinding.FragmentSwimSessionsBinding
import com.alexallafi.app.presentation.designsystem.ZeroToSwimmerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SessionsFragment : Fragment() {
    private val viewModel: SessionsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                ZeroToSwimmerTheme {
                    TrainingSessionScreenRoot(viewModel)
                }
            }
        }
}

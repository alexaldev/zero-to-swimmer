package com.alexallafi.app.presentation.nextSession

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.alexallafi.app.presentation.designsystem.ZeroToSwimmerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class NextSessionFragment : Fragment() {
    private val viewModel: NextSessionViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                ZeroToSwimmerTheme {
                    NextSessionScreenRoot(viewModel)
                }
            }
        }
}

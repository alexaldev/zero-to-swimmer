package com.alexallafi.app.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alexallafi.app.presentation.R
import com.alexallafi.app.presentation.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val viewBinding by viewBinding(FragmentSettingsBinding::bind)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        viewBinding.btnResetAll.setOnClickListener {
            // Handle reset logic here
        }
    }
}

package com.alexallafi.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alexallafi.app.presentation.databinding.FragmentSwimSessionsBinding

class SessionsFragment: Fragment(R.layout.fragment_swim_sessions) {

    private val viewBinding by viewBinding(FragmentSwimSessionsBinding::bind)

}
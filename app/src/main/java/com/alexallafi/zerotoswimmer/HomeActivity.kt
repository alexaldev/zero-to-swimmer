package com.alexallafi.zerotoswimmer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alexallafi.app.presentation.SessionsFragment
import com.alexallafi.app.presentation.databinding.FragmentSwimSessionsBinding
import com.alexallafi.app.presentation.databinding.FragmentSwimSessionsBinding.bind
import com.alexallafi.zerotoswimmer.databinding.ActivityHomeBinding

class HomeActivity : FragmentActivity() {

    private val viewBinding by viewBinding { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(viewBinding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SessionsFragment())
            .commit()
    }
}
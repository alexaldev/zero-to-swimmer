package com.alexallafi.zerotoswimmer

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
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
        setupEdgeToEdge()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SessionsFragment())
            .commit()
    }

    private fun setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View>(android.R.id.content)
        ) { v: View, windowInsets: WindowInsetsCompat ->
            val insets: Insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets paddings to the view.
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

}
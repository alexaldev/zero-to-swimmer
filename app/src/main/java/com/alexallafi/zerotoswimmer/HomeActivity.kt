package com.alexallafi.zerotoswimmer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.alexallafi.app.presentation.SessionsFragment

class HomeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SessionsFragment())
            .commit()
    }
}
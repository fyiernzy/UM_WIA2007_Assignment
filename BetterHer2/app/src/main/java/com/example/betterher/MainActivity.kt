package com.example.betterher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = CardPagerAdapter(listOf(R.drawable.two))
    }
}

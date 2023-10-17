package com.labgames.projeto_it.telasiniciais

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.labgames.projeto_it.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        splashscreen()
    }

    private fun splashscreen(){
        window.statusBarColor = Color.parseColor("#004182")
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, TelaLogin::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}
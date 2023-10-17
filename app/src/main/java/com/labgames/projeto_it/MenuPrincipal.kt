package com.labgames.projeto_it

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.labgames.projeto_it.databinding.ActivityMainBinding
import com.labgames.projeto_it.databinding.ActivityMenuPrincipalBinding

class MenuPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#004182")

        binding.iconHome.setOnClickListener {
            Troca_de_Tela(MenuPrincipal::class.java)
        }
    }

    private fun Troca_de_Tela(next_tela: Class<*>){
        val intent = Intent(this, next_tela)
        startActivity(intent)
        finish()
    }
}
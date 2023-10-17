package com.labgames.projeto_it

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.labgames.projeto_it.databinding.ActivityMainBinding
import com.labgames.projeto_it.databinding.ActivityMenuPrincipalBinding
import com.labgames.projeto_it.telasiniciais.TelaLogin

class MenuPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding
    private val auth = FirebaseAuth.getInstance()
    private val BD = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        LeituraDados()
        window.statusBarColor = Color.parseColor("#004182")

        binding.iconHome.setOnClickListener {
            Troca_de_Tela(MenuPrincipal::class.java)
        }
        binding.iconDeslogar.setOnClickListener {
            deslogar()
            Troca_de_Tela(TelaLogin::class.java)
        }
    }

    private fun deslogar(){
        FirebaseAuth.getInstance().signOut()
    }

    private fun LeituraDados(){
        val UsuarioAtual = auth.currentUser?.uid.toString()
        BD.collection("InfoUsuarios").document(UsuarioAtual).addSnapshotListener { documento, error ->
            if (documento != null) {
                binding.NomeResponsavelTxt.text = "Responsável: " + documento.getString("responsavel");
                binding.NomeCrianATxt.text = "Nome da criança: " + documento.getString("nomecrian")
                binding.DataNasTxt.text = "Data de nascimento: " + documento.getString("datanascimento")
                binding.IdadeCria.text = "Idade: " + documento.getString("idade")
                binding.GeneroCrianATxt.text = "Gênero: " + documento.getString("sexo")
            }
        }

    }

    private fun Troca_de_Tela(next_tela: Class<*>){
        val intent = Intent(this, next_tela)
        startActivity(intent)
        finish()
    }
}
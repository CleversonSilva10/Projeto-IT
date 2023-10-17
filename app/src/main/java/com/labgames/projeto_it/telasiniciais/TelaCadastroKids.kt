package com.labgames.projeto_it.telasiniciais

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.labgames.projeto_it.databinding.ActivityTelaCadastroKidsBinding

class TelaCadastroKids : AppCompatActivity() {
    private lateinit var binding: ActivityTelaCadastroKidsBinding
    private val auth = FirebaseAuth.getInstance()
    private val BD = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaCadastroKidsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#004182")

        var escolhaUsuario: String = "" // Variavel que armazena grupo Menino/Menina

        binding.iconVoltar.setOnClickListener{
            Troca_de_Tela(TelaLogin::class.java)
        }
        binding.ButtonNovoCadastro.setOnClickListener {
            val nome_c = binding.EditNomeCrianACadastro.text.toString()
            val dataNas = binding.EditDataNascimentoCrianACadastro.text.toString()
            finalizarcadastro(it, nome_c, dataNas, escolhaUsuario)
        }
        binding.menino.setOnClickListener{
            Mensagens("Opção escolhida: Menino", it, Color.parseColor("#FF4868"))
            escolhaUsuario = "Menino"
        }
        binding.menina.setOnClickListener {
            Mensagens("Opção escolhida: Menina", it, Color.parseColor("#FF4868"))
            escolhaUsuario = "Menina"
        }
    }

    private fun finalizarcadastro(view: View, nome: String, dataNas: String, escolhaUsuario: String){
        when {
            nome.isEmpty() || dataNas.isEmpty() -> {
                Mensagens("Atenção: Preencha todos os campos", view, Color.parseColor("#118DF0"))
            }else ->{
                SalvarInfoUsuarios(nome, dataNas, escolhaUsuario)
                Troca_de_Tela(TelaLogin::class.java)
            }
        }
    }

    private fun SalvarInfoUsuarios(nome: String, dataNas: String, escolhaUsuario: String){
        val usuarioatual = auth.currentUser?.uid.toString()
        val dadousuario = hashMapOf(
            "Nome Criança" to nome,
            "Data de Nascimento" to dataNas,
            "Sexo" to escolhaUsuario
        )
        BD.collection("InfoUsuarios").document(usuarioatual).update(dadousuario as Map<String, Any>)
    }

    private fun Mensagens(mensagem: String, view: View, cor: Int){
        val snack = Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)
        snack.setBackgroundTint(cor)
        snack.show()
    }

    private fun Troca_de_Tela(next_tela: Class<*>){
        val intent = Intent(this, next_tela)
        startActivity(intent)
        finish()
    }
}
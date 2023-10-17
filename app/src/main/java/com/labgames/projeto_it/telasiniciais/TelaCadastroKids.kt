package com.labgames.projeto_it.telasiniciais

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.labgames.projeto_it.databinding.ActivityTelaCadastroKidsBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

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
            escolhaUsuario = "Masculino"
        }
        binding.menina.setOnClickListener {
            Mensagens("Opção escolhida: Menina", it, Color.parseColor("#FF4868"))
            escolhaUsuario = "Feminino"
        }
    }

    private fun AnalisarData(dataNas: String, view: View): Boolean{
        val regex = """^\d{2}/\d{2}/\d{4}$""".toRegex()
        if(regex.matches(dataNas)){
            return true
        }
        Mensagens("Atenção: Data de nascimento inválida - Formato: Data/Mês/Ano", view, Color.parseColor("#118DF0"))
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calcularIdade(dataNascimentoStr: String): String {
        val formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dataNascimento = LocalDate.parse(dataNascimentoStr, formatoData)
        val hoje = LocalDate.now()

        val idade = hoje.year - dataNascimento.year

        return idade.toString()
    }

    private fun finalizarcadastro(view: View, nome: String, dataNas: String, escolhaUsuario: String){
        when {
            nome.isEmpty() || dataNas.isEmpty() -> {
                Mensagens("Atenção: Preencha todos os campos", view, Color.parseColor("#118DF0"))
            }
            AnalisarData(dataNas, view) -> {
                if(escolhaUsuario != "") {
                    SalvarInfoUsuarios(nome, dataNas, calcularIdade(dataNas), escolhaUsuario)
                    Troca_de_Tela(TelaLogin::class.java)
                }
                Mensagens("Atenção: Selecione o gênero da criança", view, Color.parseColor("#118DF0"))
            }
        }
    }

    private fun SalvarInfoUsuarios(nome: String, dataNas: String, idadeAtual: String, escolhaUsuario: String){
        val usuarioatual = auth.currentUser?.uid.toString()
        val dadousuario = hashMapOf(
            "nomecrian" to nome,
            "datanascimento" to dataNas,
            "idade" to idadeAtual,
            "sexo" to escolhaUsuario
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
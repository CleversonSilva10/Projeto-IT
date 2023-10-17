package com.labgames.projeto_it.telasiniciais

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.labgames.projeto_it.MenuPrincipal
import com.labgames.projeto_it.databinding.ActivityTelaLoginBinding

class TelaLogin : AppCompatActivity() {

    private lateinit var binding: ActivityTelaLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#004182")

        binding.ButtonNovoCadastro.setOnClickListener {
            Troca_de_Tela(TelaCadastro::class.java)
        }
        binding.ButtonEntrar.setOnClickListener {
            login(it)
        }
    }

    private fun login(view: View){
        val email = binding.EditUsuario.text.toString()
        val senha = binding.EditSenha.text.toString()

        when {
            email.isEmpty() || senha.isEmpty() -> {
                Mensagens("Atenção: Preencha todos os campos", view, Color.parseColor("#118DF0"))
            }else -> {
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener {
                    if (it.isSuccessful){
                        Troca_de_Tela(MenuPrincipal::class.java)
                    }
                }.addOnFailureListener {
                    when (it){
                        is FirebaseAuthInvalidCredentialsException -> Mensagens("E-mail Inválido: Digite novamente!", view, Color.parseColor("#118DF0"))
                        is FirebaseNetworkException -> Mensagens("Sem conexão com a internet!", view, Color.parseColor("#118DF0"))
                        else -> {
                            Mensagens("Credenciais inválidas", view, Color.parseColor("#118DF0"))
                        }
                    }
                }
            }
        }
    }

    private fun Troca_de_Tela(next_tela: Class<*>) {
        val intent = Intent(this, next_tela)
        startActivity(intent)
        finish()
    }

    private fun Mensagens(mensagem: String, view: View, cor: Int){
        val snack = Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)
        snack.setBackgroundTint(cor)
        snack.show()
    }
}
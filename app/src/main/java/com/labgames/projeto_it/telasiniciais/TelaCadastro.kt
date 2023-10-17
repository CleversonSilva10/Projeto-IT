package com.labgames.projeto_it.telasiniciais

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.labgames.projeto_it.databinding.ActivityTelaCadastroBinding

class TelaCadastro : AppCompatActivity() {

    private lateinit var binding: ActivityTelaCadastroBinding
    private val auth = FirebaseAuth.getInstance()
    private val BD = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#004182")

        var escolhaUsuario: String = "" // Variavel que armazena grupo Saúde/Pai/Mae

        binding.iconVoltar.setOnClickListener {
            Troca_de_Tela(TelaLogin::class.java)
        }

        binding.ButtonProsseguir.setOnClickListener {
            val nome = binding.EditNomeResponsavelCadastro.text.toString()
            val email = binding.EditEmailResponsavelCadastro.text.toString()
            val senha = binding.EditSenhaResponsavelCadastro.text.toString()
            val confirme_senha = binding.EditConfirmeSenhaResponsavelCadastro.text.toString()
            val telefone = binding.EditTelefoneResponsavelCadastro.text.toString()
            novo_cadastro(it, nome, email, senha, confirme_senha, telefone, escolhaUsuario)
        }
        binding.saude.setOnClickListener{
            Mensagens("Opção escolhida: Área da Saúde", it, Color.parseColor("#FF4868"))
            escolhaUsuario = "Área da Saúde"
        }
        binding.pai.setOnClickListener {
            Mensagens("Opção escolhida: Pai", it, Color.parseColor("#FF4868"))
            escolhaUsuario = "Pai"
        }
        binding.mae.setOnClickListener {
            Mensagens("Opção escolhida: Mãe", it, Color.parseColor("#FF4868"))
            escolhaUsuario = "Mãe"
        }
    }

    private fun Mensagens(mensagem: String, view: View, cor: Int){
        val snack = Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)
        snack.setBackgroundTint(cor)
        snack.show()
    }

    private fun novo_cadastro(view: View, nome: String, email: String, senha: String, confirme_senha: String, telefone: String,
                              escolhaUsuario: String){
        when {
            nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirme_senha.isEmpty() || telefone.isEmpty() -> {
                Mensagens("Atenção: Preencha todos os campos", view, Color.parseColor("#118DF0"))
            }
            senha.length < 6 -> {
                Mensagens("Senha: Mínimo de 6 caracters", view, Color.parseColor("#118DF0"))
            }
            confirme_senha.length < 6 -> {
                Mensagens("Confirme sua senha: Mínimo de 6 caracters", view, Color.parseColor("#118DF0"))
            }
            senha != confirme_senha -> {
                Mensagens("Campos de senha não estão iguais, digite novamente", view, Color.parseColor("#118DF0"))
            }
            senha == confirme_senha -> {
                authEmail(nome, email, senha, telefone, escolhaUsuario, view)
            }
        }
    }

    private fun SalvarInfoUsuarios(nome: String, email: String, telefone: String, escolhaUsuario: String){
        val usuarioatual = auth.currentUser?.uid.toString()
        val dadousuario = hashMapOf(
            "Nome do Responsável" to nome,
            "E-mail" to email,
            "Telefone do Responsável" to telefone,
            "Perfil do Responsável" to escolhaUsuario
        )
            BD.collection("InfoUsuarios").document(usuarioatual).set(dadousuario)
    }

    private fun authEmail(nome: String, email: String, senha: String, telefone: String, escolhaUsuario: String, view: View){
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener{
            if (it.isSuccessful){
                Mensagens("Passo 1: Realizado com sucesso", view, Color.parseColor("#FF4868"))
                Handler().postDelayed({
                    SalvarInfoUsuarios(nome, email, telefone, escolhaUsuario)
                    Troca_de_Tela(TelaCadastroKids::class.java)
                }, 3000)
            }
        }.addOnFailureListener {
            when (it){
                is FirebaseAuthWeakPasswordException -> Mensagens("Senha inválida: Mínimo de 6 caracters", view, Color.parseColor("#118DF0"))
                is FirebaseAuthInvalidCredentialsException -> Mensagens("E-mail Inválido: Digite novamente!", view, Color.parseColor("#118DF0"))
                is FirebaseAuthUserCollisionException -> Mensagens("Conta já existente!", view, Color.parseColor("#118DF0"))
                is FirebaseNetworkException -> Mensagens("Sem conexão com a internet!", view, Color.parseColor("#118DF0"))
                else -> "Não foi possivel o cadastro"
            }
        }
    }

    private fun Troca_de_Tela(next_tela: Class<*>){
        val intent = Intent(this, next_tela)
        startActivity(intent)
        finish()
    }
}
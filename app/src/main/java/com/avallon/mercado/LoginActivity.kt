package com.avallon.mercado

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var progressBar: ProgressBar
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                showLoading(false)
                Toast.makeText(this, "Erro no login com Google: ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            showLoading(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar se já está logado antes de inflar o layout
        auth = Firebase.auth
        if (auth.currentUser != null) {
            startProdutosActivity()
            return
        }

        setContentView(R.layout.activity_login)
        setupGoogleSignIn()
        setupViews()
    }

    private fun setupViews() {
        progressBar = findViewById(R.id.progressBar)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        // Login com email/senha
        findViewById<Button>(R.id.loginButton).setOnClickListener {
            loginWithEmail()
        }

        // Cadastro com email/senha
        findViewById<Button>(R.id.registerButton).setOnClickListener {
            registerWithEmail()
        }

        // Login com Google
        findViewById<SignInButton>(R.id.googleSignInButton).setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun loginWithEmail() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Preencha email e senha", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    startProdutosActivity()
                } else {
                    Toast.makeText(this, "Erro no login: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerWithEmail() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Preencha email e senha", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    startProdutosActivity()
                } else {
                    Toast.makeText(this, "Erro no cadastro: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
        showLoading(true)
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    startProdutosActivity()
                } else {
                    Toast.makeText(this, "Erro na autenticação: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun startProdutosActivity() {
        startActivity(Intent(this, ProdutosActivity::class.java))
        finish()
    }
}

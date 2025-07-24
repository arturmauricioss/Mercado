package com.exemplo.meuapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        // Configurar Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Botão de login
        findViewById<SignInButton>(R.id.signInButton).setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
        }

        // Verificar se já está logado
        if (auth.currentUser != null) {
            startProdutosActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Falha no login: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startProdutosActivity()
                } else {
                    Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun startProdutosActivity() {
        startActivity(Intent(this, ProdutosActivity::class.java))
        finish()
    }
}

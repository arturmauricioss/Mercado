package com.avallon.mercado.util

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object StorageUtil {
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun uploadImage(context: Context, imageUri: Uri): String = suspendCoroutine { continuation ->
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("Usuário não logado")
        val imageName = "produtos/${userId}/${UUID.randomUUID()}.jpg"
        val imageRef = storage.reference.child(imageName)

        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            val uploadTask = imageRef.putStream(inputStream)
            
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    continuation.resume(downloadUri.toString())
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("Upload falhou"))
                }
            }
        }
    }
}

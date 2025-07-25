package com.avallon.mercado

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MercadoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Habilita persistência offline do Realtime Database
        Firebase.database.setPersistenceEnabled(true)
    }
}

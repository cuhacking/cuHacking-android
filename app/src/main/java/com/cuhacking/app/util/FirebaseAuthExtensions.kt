package com.cuhacking.app.util

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseUser

suspend fun FirebaseUser.getBearerAuth(): String? {
    val token = Tasks.await(getIdToken(false)).token
    return when {
        token != null -> "Bearer $token"
        else -> null
    }
}
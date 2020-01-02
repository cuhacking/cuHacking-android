package com.cuhacking.app.data.auth

sealed class AuthState {
    data class Authenticated(val role: UserRole) : AuthState()
    object Unauthenticated : AuthState()
}

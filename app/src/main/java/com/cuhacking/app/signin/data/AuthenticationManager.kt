package com.cuhacking.app.signin.data

import com.cuhacking.app.Database
import com.cuhacking.app.data.CoroutinesDispatcherProvider
import com.cuhacking.app.data.Result
import com.cuhacking.app.data.db.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationManager @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: Database,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    suspend fun authenticateUser(email: String, password: String): Result<User> =
        withContext(dispatchers.io) {
            // Allow only a single user to be signed in at a time
            if (auth.currentUser != null) {
                signOut(auth.currentUser!!)
            }

            return@withContext try {
                val result = Tasks.await(auth.signInWithEmailAndPassword(email, password))
                val user = result.user!!

                database.userQueries.insert(
                    user.uid,
                    user.displayName ?: "",
                    user.email ?: "",
                    "red",
                    "Carleton University",
                    true,
                    Date().time
                )

                Result.Success(database.userQueries.getById(user.uid).executeAsOne())
            } catch (e: Exception) {
                Result.Error<User>(e)
            }
        }

    suspend fun signOut(user: FirebaseUser) = withContext(dispatchers.io) {
        database.userQueries.delete(user.uid)
        auth.signOut()
    }
}
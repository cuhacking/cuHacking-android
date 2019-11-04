package com.cuhacking.app.data

sealed class Result<out T : Any> {
    abstract val data: T?

    data class Success<out T : Any>(override val data: T) : Result<T>()
    data class Loading<out T : Any>(override val data: T? = null) : Result<T>()
    data class Error<out T : Any>(val exception: Throwable, override val data: T? = null) :
        Result<T>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error<*> -> "Error[exception=$exception, data=$data]"
            is Loading<*> -> "Loading[oldData=$data]"
        }
    }
}
package com.openclaw.zenith.core.common.result

/**
 * Lightweight result wrapper used across module boundaries. Avoids the JVM
 * [kotlin.Result] (which is awkward in sealed-when chains) and gives us a
 * stable, serialisable shape for orchestrator/AI flows.
 */
sealed class Outcome<out T> {
    data class Success<T>(
        val value: T,
    ) : Outcome<T>()

    data class Failure(
        val error: Throwable,
    ) : Outcome<Nothing>()

    inline fun <R> map(transform: (T) -> R): Outcome<R> =
        when (this) {
            is Success -> Success(transform(value))
            is Failure -> this
        }

    inline fun <R> flatMap(transform: (T) -> Outcome<R>): Outcome<R> =
        when (this) {
            is Success -> transform(value)
            is Failure -> this
        }

    fun getOrNull(): T? = (this as? Success)?.value

    fun isSuccess(): Boolean = this is Success
}

@Suppress("TooGenericExceptionCaught")
inline fun <T> outcomeOf(block: () -> T): Outcome<T> =
    try {
        Outcome.Success(block())
    } catch (t: Throwable) {
        Outcome.Failure(t)
    }

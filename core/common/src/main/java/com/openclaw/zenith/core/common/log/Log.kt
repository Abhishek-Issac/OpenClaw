package com.openclaw.zenith.core.common.log

import timber.log.Timber

/**
 * Thin facade over Timber so callers don't pin a dep on it directly.
 * Swap the implementation here if we ever migrate.
 */
object Log {
    fun d(
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) {
        Timber.tag(tag).d(throwable, message)
    }

    fun i(
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) {
        Timber.tag(tag).i(throwable, message)
    }

    fun w(
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) {
        Timber.tag(tag).w(throwable, message)
    }

    fun e(
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) {
        Timber.tag(tag).e(throwable, message)
    }
}

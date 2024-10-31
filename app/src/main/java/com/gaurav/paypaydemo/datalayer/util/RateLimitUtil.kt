package com.gaurav.paypaydemo.datalayer.util

import javax.inject.Inject

class RateLimiter
@Inject
constructor() {

    @Synchronized
    fun shouldFetch(timeout: Long, lastFetched: Long?): Boolean {
        val now = now()
        if (lastFetched == null) {
            return true
        }
        val diff = now - lastFetched
        if (diff > timeout) {
            return true
        }
        return false
    }

    private fun now() = System.currentTimeMillis()

}

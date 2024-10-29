package com.gaurav.paypaydemo.datalayer.util

import java.util.concurrent.TimeUnit

class RateLimiter(val timeout: Long) {

    @Synchronized
    fun shouldFetch(lastFetched: Long?): Boolean {
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

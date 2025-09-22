package org.cryptotrader.api.library.component.dpop

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

/**
 * Stops someone from reusing the exact same DPoP proof.
 *
 * Plain English:
 * - Each DPoP proof carries a one-time random id called jti (think “just this instance”).
 * - We remember recent jti values for a short period. If we see the same one again, we reject it.
 *
 * Implementation note: This is an in-memory map ideal for local/dev. Use Redis in production.
 */
@Component
class DpopReplayCache(
    @Value("\${security.auth.dpop.replay-ttl-seconds:120}") private val ttlSeconds: Long
) {
    private val cache = ConcurrentHashMap<String, Long>()

    fun isReplay(jwtId: String?): Boolean {
        if (jwtId.isNullOrBlank()) {
            return true
        }
        this.evictExpired()
        val now: Long = Instant.now().epochSecond
        val previousEntry: Long? = this.cache.putIfAbsent(jwtId, now + this.ttlSeconds)
        return previousEntry != null
    }

    private fun evictExpired() {
        val now: Long = Instant.now().epochSecond
        val storesIterator = this.cache.entries.iterator()
        while (storesIterator.hasNext()) {
            val entry = storesIterator.next()
            if (entry.value < now) {
                storesIterator.remove()
            }
        }
    }
}
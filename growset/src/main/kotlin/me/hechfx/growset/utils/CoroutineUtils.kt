package me.hechfx.growset.utils

import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.*
import java.util.UUID

object CoroutineUtils {
    val utilsDispatcher = CoroutineScope(Dispatchers.Default + SupervisorJob())
    val timeCache = Caffeine.newBuilder()
        .build<UUID, TimeType>()

    sealed class TimeType {
        data class Timeout(val callback: suspend () -> Unit) : TimeType()
        data class Interval(val callback: suspend () -> Unit) : TimeType()
    }

    suspend fun setTimeout(time: Long, callback: suspend () -> Unit) {
        val id = UUID.randomUUID()
        timeCache.put(id, TimeType.Timeout(callback))

        utilsDispatcher.launch {
            delay(time)
            timeCache.getIfPresent(id)?.let {
                if (it is TimeType.Timeout) {
                    it.callback()
                    timeCache.invalidate(id)
                }
            }
        }
    }

    suspend fun setInterval(time: Long, callback: suspend () -> Unit): UUID {
        val id = UUID.randomUUID()
        timeCache.put(id, TimeType.Interval(callback))

        utilsDispatcher.launch {
            while (true) {
                delay(time)
                timeCache.getIfPresent(id)?.let {
                    if (it is TimeType.Interval) {
                        it.callback()
                    }
                }
            }
        }

        return id
    }

    fun clearInterval(id: UUID) {
        timeCache.invalidate(id)
    }
}